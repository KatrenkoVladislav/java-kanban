package service;

import exception.ManagerIOException;
import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subTasks;
    protected final Map<Integer, Epic> epics;
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected final HistoryManager historyManager;
    protected int seq = 0;


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }


    @Override
    public ArrayList<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAll() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        }
        tasks.clear();
    }

    @Override
    public Task get(int id) {
        final Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задачи с таким id-" + id + " нет");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Task create(Task task) {
        if (task == null) {
            throw new ManagerIOException("Такой задачи не существует");
        }
        task.setId(generateId());
        checkTaskTime(task);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void update(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            throw new ManagerIOException("Такой задачи не существует");
        }
        checkTaskTime(task);
        prioritizedTasks.remove(savedTask);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void delete(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTask() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(subTasks.get(id));
        }
        for (Epic epic : epics.values()) {
            epic.removeAllTask();
            calculateEpic(epic.getId());
        }
        subTasks.clear();
    }

    @Override
    public void deleteSubTask(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(subTasks.get(id));
        Subtask subtask = subTasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeTask(id);
        subTasks.remove(id);
        calculateEpic(epic.getId());
    }

    @Override
    public Subtask getSubTask(int id) {
        final Subtask subtask = subTasks.get(id);
        if (subtask == null) {
            throw new NotFoundException("Подзачи с таким id-" + id + " нет");
        }
        historyManager.add(subTasks.get(id));
        return subtask;
    }

    @Override
    public Subtask createSubTask(Subtask subtask) {
        if (subtask == null) {
            throw new ManagerIOException("Такой подзадачи не существует");
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            throw new ManagerIOException("Такого эпика не существует");
        }
        subtask.setId(generateId());
        checkTaskTime(subtask);
        prioritizedTasks.add(subtask);
        subTasks.put(subtask.getId(), subtask);
        epic.addTask(subtask.getId());
        calculateEpic(epic.getId());
        return subtask;
    }

    @Override
    public Subtask updateSubTask(Subtask subtask) {
        Subtask origin = subTasks.get(subtask.getId());
        if (origin == null) {
            throw new ManagerIOException("Такой подзадачи не существует");
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            throw new ManagerIOException("Такого эпика не существует");
        }
        checkTaskTime(subtask);
        prioritizedTasks.remove(origin);
        prioritizedTasks.add(subtask);
        subTasks.put(subtask.getId(), subtask);
        epic.addTask(subtask.getId());
        calculateEpic(epic.getId());
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            throw new ManagerIOException("Такого эпика не существует");
        }
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        calculateEpic(epic.getId());
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic saved = epics.get(epic.getId());
        if (saved == null) {
            throw new ManagerIOException("Такого эпика не существует");
        }
        epic.setSubTasksId(saved.getSubtasks());
        epic.setStatus(saved.getStatus());
        epics.put(epic.getId(), epic);
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Эпика с таким id-" + id + " нет");
        }
        historyManager.add(epics.get(id));
        return epic;
    }

    @Override
    public void deleteAllEpic() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(subTasks.get(id));
        }
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteEpic(int id) {
        historyManager.remove(id);
        Epic epic = epics.remove(id);
        for (int subtask : epic.getSubtasks()) {
            historyManager.remove(subtask);
            prioritizedTasks.remove(subTasks.get(subtask));
            subTasks.remove(subtask);
        }
    }

    @Override
    public ArrayList<Subtask> getSubTaskForEpic(Epic epic) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int idSubTask : epic.getSubtasks()) {
            subtasks.add(subTasks.get(idSubTask));
        }
        return subtasks;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTask() {
        return new ArrayList<>(prioritizedTasks);
    }


    private int generateId() {
        return ++seq;
    }

    private void calculateEpic(int epicId) {
        Epic epic = epics.get(epicId);
        updateEpicStatus(epic);
        updateEpicTime(epic);
    }

    private void updateEpicStatus(Epic epic) {
        boolean allDone = true;
        boolean allNew = true;
        if (epic.getSubtasks() == null || epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (int subtask : epic.getSubtasks()) {
            Subtask subtask1 = subTasks.get(subtask);
            if (subtask1.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subtask1.getStatus() != Status.NEW) {
                allNew = false;
            }
        }
        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }

    private void updateEpicTime(Epic epic) {
        Duration durationEpic = Duration.ofMinutes(0);
        LocalDateTime epicStartTime = LocalDateTime.now();
        LocalDateTime epicEndTime = LocalDateTime.now();
        for (int subtask : epic.getSubtasks()) {
            Subtask subtask1 = subTasks.get(subtask);
            if (subtask1.getStartTime().isBefore(epicStartTime)) {
                epicStartTime = subtask1.getStartTime();
            }
            durationEpic = durationEpic.plus(subtask1.getDuration());
            if (subtask1.getEndTime().isAfter(epicEndTime)) {
                epicEndTime = subtask1.getEndTime();
            }

        }
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(durationEpic);
    }


    private void checkTaskTime(Task task) {
        prioritizedTasks.stream()
                .filter(t -> t.getId() != task.getId())
                .filter(t -> !validateTask(t, task))
                .findFirst()
                .ifPresent(t -> {
                    throw new ValidationException("Время занято. Пересечение задач " + t.getTitle() + " " + task.getTitle());
                });

    }

    private boolean validateTask(Task t, Task task) {
        return (task.getEndTime().isBefore(t.getStartTime()) || (task.getStartTime().isAfter(t.getEndTime())));
    }
}
