package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Task task = new Task("Задача", "легкая", Status.NEW);
        Task task1 = new Task("Задача", "легкая", Status.NEW);
        Task task2 = new Task("Задача", "легкая", Status.NEW);
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(3, historyManager.getHistory().size());
    }

}
