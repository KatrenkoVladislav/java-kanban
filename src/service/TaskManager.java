package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Subtask> subTasks;
    HashMap<Integer, Epic> epics;

    private int seq = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    private int generateId() {
        return ++seq;
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

        tasks.put(task.getId(), task);
    }

    public void delete(int id) {
        tasks.remove(id);
    }

    public ArrayList<Subtask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllSubTask() {
        subTasks.clear();
    }

    public void deleteSubTask(int id) {
        subTasks.remove(id);
    }

    public Subtask getSubTask(int id) {
        return subTasks.get(id);
    }

    public Subtask createSubTask(Subtask subtask) {
        subtask.setId(generateId());
        Epic epic = epics.get(subtask.getEpic().getId());
        subTasks.put(subtask.getId(), subtask);
        epic.addTask(subtask);
        epic.updateStatus();
        return subtask;
    }

    public Subtask updateSubTask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpic().getId());
        epic.addTask(subtask);
        epic.updateStatus();
        return subtask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        epic.updateStatus();
        return epic;
    }

    public Epic updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.setTitle(epic.getTitle());
        saved.setDescription(epic.getDescription());
        return epic;
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void deleteAllEpic() {
        epics.clear();
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        epic.removeAllTask();
        epics.remove(id);
    }

    public ArrayList<Subtask> getSubTaskForEpic(Epic epic) {
        return epic.getSubtasks();
    }
}
