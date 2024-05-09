package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryTaskManagerTest {
    TaskManager manager = Manager.getDefault();

    @DisplayName("Проверяем метод создания задачи и добавление её в таблицу")
    @Test
    public void shouldCreateAndAddInHashmapByTask() {
        Task task = new Task("Задача", "Легкая", Status.NEW);
        manager.create(task);
        assertEquals(1, task.getId());
        assertNotNull(task);
        assertNotNull(manager.getAll());
    }

    @DisplayName("Обновляем поля задачи и сохраняем обновленную задачу в таблицу")
    @Test
    public void shouldUpdateAndSaveInHashmapByTask() {
        Task task = new Task("Задача", "Легкая", Status.NEW);
        manager.create(task);
        task.setStatus(Status.DONE);
        task.setDescription("Обновление");
        manager.update(task);
        Task task1 = manager.get(1);
        assertEquals(task, task1);
    }

    @DisplayName("Удаляем все задачи из таблицы")
    @Test
    public void shouldDeleteAllTaskInHashmap() {
        Task task = manager.create(new Task("Задача1", "Легкая", Status.NEW));
        Task task1 = manager.create(new Task("Задача2", "Легкая", Status.DONE));
        manager.deleteAll();
        assertEquals(0, manager.getAll().size());
    }

    @DisplayName("Удаляем задачу из таблицы по id")
    @Test
    public void shouldDeleteTaskForIdInHashmap() {
        Task task = manager.create(new Task("Задача1", "Легкая", Status.NEW));
        manager.delete(1);
        assertNull(manager.get(1));
    }

    @DisplayName("Получаем задачу из таблицы по id")
    @Test
    public void shouldGetTaskByIdFromHashmap() {
        Task task = manager.create(new Task("Задача1", "Легкая", Status.NEW));
        Task task1 = manager.get(1);
        assertEquals(task1, task);
        assertNotNull(manager.get(1));
    }

    @DisplayName("Получаем все задачи из таблицы")
    @Test
    public void shouldGetAllTaskFromHashmap() {
        Task task = manager.create(new Task("Задача1", "Легкая", Status.NEW));
        Task task1 = manager.create(new Task("Задача1", "Легкая", Status.NEW));
        manager.getAll();
        assertEquals(2, manager.getAll().size());
    }

    @DisplayName("Проверяем метод создания подзадачи и добавление её в таблицу")
    @Test
    public void shouldCreateAndAddInHashmapBySubtask() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача", "Легкая", Status.DONE, epic.getId()));
        assertNotNull(manager.getEpic(epic.getId()));
        assertEquals(2, subtask.getId());
        assertNotNull(manager.getAllSubTask());
        assertNotNull(manager.getSubTaskForEpic(epic));
        assertEquals(Status.DONE, epic.getStatus());
    }

    @DisplayName("Обновляем поля подзадачи и сохраняем обновленную задачу в таблицу")
    @Test
    public void shouldUpdateAndSaveInHashmapBySubtask() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача", "Легкая", Status.DONE, epic.getId()));
        subtask.setStatus(Status.NEW);
        subtask.setTitle("Subtask");
        subtask.setDescription("Обновленная");
        manager.updateSubTask(subtask);
        assertEquals(subtask, manager.getSubTask(2));
    }

    @DisplayName("Удаляем все подзадачи из таблицы,эпика")
    @Test
    public void shouldDeleteAllSubtaskInHashmapInEpic() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.DONE, epic.getId()));
        Subtask subtask1 = manager.createSubTask(new Subtask("Подзадача2", "Легкая", Status.DONE, epic.getId()));
        manager.deleteAllSubTask();
        assertEquals(0, manager.getSubTaskForEpic(epic).size());
        assertEquals(0, manager.getAllSubTask().size());
    }

    @DisplayName("Удаляем подзадачу из таблицы по id")
    @Test
    public void shouldDeleteSubtaskByIdFromInHashmap() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.DONE, epic.getId()));
        manager.deleteSubTask(2);
        assertNull(manager.getSubTask(2));
    }

    @DisplayName("Получаем подзадачу из таблицы по id")
    @Test
    public void shouldGetSubtaskByIdFromHashmap() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.DONE, epic.getId()));
        Subtask subtask1 = manager.getSubTask(2);
        assertEquals(subtask1, subtask);
        assertNotNull(manager.getSubTask(2));
    }

    @DisplayName("Получаем все подзадачи из таблицы")
    @Test
    public void shouldGetAllSubtaskFromHashmap() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.DONE, epic.getId()));
        Subtask subtask1 = manager.createSubTask(new Subtask("Подзадача2", "Легкая", Status.DONE, epic.getId()));
        manager.getAllSubTask();
        assertEquals(2, manager.getAllSubTask().size());
    }

    @DisplayName("Проверяем метод создания эпика и добавление её в таблицу")
    @Test
    public void shouldCreateAndAddInHashmapByEpic() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        assertNotNull(manager.getAllEpic());
    }

    @DisplayName("Обновляем поля эпика и сохраняем обновленную задачу в таблицу")
    @Test
    public void shouldUpdateAndSaveInHashmapByEpic() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        epic.setStatus(Status.NEW);
        epic.setTitle("Epic");
        epic.setDescription("Обновленный");
        manager.updateEpic(epic);
        assertEquals(epic, manager.getEpic(1));
    }

    @DisplayName("Удаляем все эпики из таблицы")
    @Test
    public void shouldDeleteAllEpicInHashmap() {
        Epic epic = manager.createEpic(new Epic("Эпик1", "Особый"));
        Epic epic1 = manager.createEpic(new Epic("Эпик2", "Особый"));
        manager.deleteAllEpic();
        assertEquals(0, manager.getAllEpic().size());
    }

    @DisplayName("Удаляем эпик из таблицы по id")
    @Test
    public void shouldDeleteEpicByIdFromInHashmap() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        manager.deleteEpic(1);
        assertNull(manager.getEpic(1));
    }

    @DisplayName("Получаем эпик из таблицы по id")
    @Test
    public void shouldGetEpicByIdFromHashmap() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.DONE, epic.getId()));
        Subtask subtask1 = manager.getSubTask(2);
        assertEquals(subtask1, subtask);
        assertNotNull(manager.getSubTask(2));
    }

    @DisplayName("Получаем все эпики из таблицы")
    @Test
    public void shouldGetAllEpicFromHashmap() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Epic epic1 = manager.createEpic(new Epic("Эпик", "Особый"));
        manager.getAllEpic();
        assertEquals(2, manager.getAllEpic().size());
    }

    @DisplayName("Получаем все подзадачи определённого эпика")
    @Test
    public void shouldGetAllSubtaskByEpic() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.NEW, epic.getId()));
        Subtask subtask1 = manager.createSubTask(new Subtask("Подзадача2", "Легкая", Status.DONE, epic.getId()));
        manager.getSubTaskForEpic(epic);
        assertEquals(2, manager.getSubTaskForEpic(epic).size());
    }

    @DisplayName("Получаем историю задач")
    @Test
    public void shouldGetTaskHistory() {
        Task task = manager.create(new Task("Задача1", "Легкая", Status.NEW));
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.NEW, epic.getId()));
        Subtask subtask1 = manager.createSubTask(new Subtask("Подзадача2", "Легкая", Status.DONE, epic.getId()));
        manager.get(1);
        manager.getEpic(2);
        manager.getSubTask(3);
        manager.getSubTask(4);
        assertEquals(4, manager.getHistory().size());
    }

    @DisplayName("Проверяем статус эпика по изменению статуса подзадачи")
    @Test
    public void shouldCheckStatusEpicByChangStatusSubtask() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.NEW, epic.getId()));
        Subtask subtask1 = manager.createSubTask(new Subtask("Подзадача2", "Легкая", Status.NEW, epic.getId()));
        subtask1.setStatus(Status.DONE);
        manager.updateSubTask(subtask1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}