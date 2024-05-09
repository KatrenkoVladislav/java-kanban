package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subTasks;
    private final Map<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private int seq = 0;

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
        }
        tasks.clear();
    }

    @Override
    public Task get(int id) {
        final Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void update(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void delete(int id) {
        historyManager.remove(id);
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
        }
        for (Epic epic : epics.values()) {
            epic.removeAllTask();
            updateEpicStatus(epic.getId());
        }
        subTasks.clear();
    }

    @Override
    public void deleteSubTask(int id) {
        historyManager.remove(id);
        Subtask subtask = subTasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeTask(id);
        subTasks.remove(id);
        updateEpicStatus(epic.getId());
    }

    @Override
    public Subtask getSubTask(int id) {
        final Subtask subtask = subTasks.get(id);
        historyManager.add(subTasks.get(id));
        return subtask;
    }

    @Override
    public Subtask createSubTask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subtask.setId(generateId());
        subTasks.put(subtask.getId(), subtask);
        epic.addTask(subtask.getId());
        updateEpicStatus(epic.getId());
        return subtask;
    }

    @Override
    public Subtask updateSubTask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subTasks.put(subtask.getId(), subtask);
        epic.addTask(subtask.getId());
        updateEpicStatus(epic.getId());
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
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
        }
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        for (int subtask : epic.getSubtasks()) {
            historyManager.remove(subtask);
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

    private int generateId() {
        return ++seq;
    }

    private void updateEpicStatus(int epicId) {
        boolean allDone = true;
        boolean allNew = true;
        Epic epic = epics.get(epicId);
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
}
