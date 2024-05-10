package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    HistoryManager historyManager = new InMemoryHistoryManager();

    @DisplayName("Проверяем добавление подзадачи в лист")
    @Test
    public void shouldAddTaskInList() {
        Task task = new Task("Задача", "легкая", Status.NEW);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }

    @DisplayName("Проверяем наполненность листа задачами")
    @Test
    public void shouldFullListByTask() {
        Task task = new Task(1, "Задача", "легкая", Status.NEW);
        Task task1 = new Task(2, "Задача", "легкая", Status.NEW);
        Task task2 = new Task(3, "Задача", "легкая", Status.NEW);
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(3, historyManager.getHistory().size());

    }

    @DisplayName("Проверям удаление Node из таблицы")
    @Test
    public void shouldDeleteNodeByMap() {
        Task task = new Task(1, "Задача1", "легкая", Status.NEW);
        historyManager.add(task);
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size());
    }

    @DisplayName("Проверям удаление старой задачи с одинаковый id из истории")
    @Test
    public void shouldDeleteOldTaskByHistory() {
        Task task = new Task(1, "Задача1", "легкая", Status.NEW);
        Task task1 = new Task(1, "Задача2", "легкая", Status.NEW);
        historyManager.add(task);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size());
    }

    @DisplayName("Проверям удаление первой задачи из истории")
    @Test
    public void shouldDeleteFirstTaskByHistory() {
        Task task1 = new Task(1, "Задача1", "легкая", Status.NEW);
        Task task2 = new Task(2, "Задача2", "легкая", Status.NEW);
        Task task3 = new Task(3, "Задача3", "легкая", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(1);
        assertEquals(historyManager.getHistory(), List.of(task2, task3));
    }

    @DisplayName("Проверям удаление задачи находящиеся в середине истории")
    @Test
    public void shouldDeleteMiddleTaskByHistory() {
        Task task1 = new Task(1, "Задача1", "легкая", Status.NEW);
        Task task2 = new Task(2, "Задача2", "легкая", Status.NEW);
        Task task3 = new Task(3, "Задача3", "легкая", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);
        assertEquals(historyManager.getHistory(), List.of(task1, task3));
    }
}
