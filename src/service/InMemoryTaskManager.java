package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subTasks;
    private HashMap<Integer, Epic> epics;
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
        tasks.clear();
    }

    @Override
    public Task get(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
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
        tasks.remove(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTask() {
        for (Epic epic : epics.values()) {
            epic.removeAllTask();
            updateEpicStatus(epic.getId());
        }
        subTasks.clear();
    }

    @Override
    public void deleteSubTask(int id) {
        Subtask subtask = subTasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeTask(id);
        subTasks.remove(id);
        updateEpicStatus(epic.getId());
    }

    @Override
    public Subtask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
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
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setTitle(epic.getTitle());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void deleteAllEpic() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (int subtask : epic.getSubtasks()) {
            subTasks.remove(subtask);
        }
        epics.remove(id);
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
        return historyManager.getHistory();
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
