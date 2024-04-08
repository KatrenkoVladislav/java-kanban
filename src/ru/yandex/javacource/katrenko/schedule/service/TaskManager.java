package ru.yandex.javacource.katrenko.schedule.service;


import ru.yandex.javacource.katrenko.schedule.model.Epic;
import ru.yandex.javacource.katrenko.schedule.model.Status;
import ru.yandex.javacource.katrenko.schedule.model.Subtask;
import ru.yandex.javacource.katrenko.schedule.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subTasks;
    private HashMap<Integer, Epic> epics;

    private int seq = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }


    public ArrayList<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAll() {
        tasks.clear();
    }

    public Task get(int id) {
        return tasks.get(id);
    }

    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public void update(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void delete(int id) {
        tasks.remove(id);
    }

    public ArrayList<Subtask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllSubTask() {
        for (Epic epic : epics.values()) {
            epic.removeAllTask();
            updateEpicStatus(epic.getId());
        }
        subTasks.clear();
    }

    public void deleteSubTask(int id) {
        Subtask subtask = subTasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeTask(id);
        subTasks.remove(id);
        updateEpicStatus(epic.getId());
    }

    public Subtask getSubTask(int id) {
        return subTasks.get(id);
    }

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

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
        return epic;
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setTitle(epic.getTitle());
        saved.setDescription(epic.getDescription());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void deleteAllEpic() {
        subTasks.clear();
        epics.clear();
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (int subtask : epic.getSubtasks()) {
            subTasks.remove(subtask);
        }
        epics.remove(id);
    }

    public ArrayList<Subtask> getSubTaskForEpic(Epic epic) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int idSubTask : epic.getSubtasks()) {
            subtasks.add(subTasks.get(idSubTask));
        }
        return subtasks;
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
