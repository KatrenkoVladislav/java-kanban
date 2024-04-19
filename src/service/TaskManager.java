package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAll();

    void deleteAll();

    Task get(int id);

    Task create(Task task);

    void update(Task task);

    void delete(int id);

    List<Subtask> getAllSubTask();

    void deleteAllSubTask();

    void deleteSubTask(int id);

    Subtask getSubTask(int id);

    Subtask createSubTask(Subtask subtask);

    Subtask updateSubTask(Subtask subtask);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    List<Epic> getAllEpic();

    Epic getEpic(int id);

    void deleteAllEpic();

    void deleteEpic(int id);

    List<Subtask> getSubTaskForEpic(Epic epic);

    List<Task> getHistory();
}
