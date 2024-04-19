package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Manager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    TaskManager manager = Manager.getDefault();

    @DisplayName("Проверяем добавление подзадачи в лист")
    @Test
    public void shouldAssSubtaskInList() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Сложный"));
        epic.addTask(2);
        assertEquals(1, manager.getSubTaskForEpic(epic).size());
    }

    @DisplayName("Проверяем удаление подзадачи в лист")
    @Test
    public void shouldDeleteSubtaskInList() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Сложный"));
        epic.addTask(2);
        epic.removeTask(2);
        assertEquals(0, manager.getSubTaskForEpic(epic).size());
    }

    @DisplayName("Проверяем удаление всех подзадачи в лист")
    @Test
    public void shouldDeleteAllSubtaskInList() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Сложный"));
        epic.addTask(2);
        epic.addTask(3);
        epic.removeAllTask();
        assertEquals(0, manager.getSubTaskForEpic(epic).size());
    }

}
