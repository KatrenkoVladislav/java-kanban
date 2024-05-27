package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class FileBackedTaskManagerTest {

    @DisplayName("Проверяем сохранение и загрузку задачи в файл")
    @Test
    public void saveAndLoadingTaskInFile() {
        //TaskManager manager = Manager.getDefault();
        TaskManager manager = new FileBackedTaskManager(Manager.getDefaultHistory(), new File("test/resources/test.csv"));
        Task task1 = manager.create(new Task("Задача", "Легкая", Status.NEW));
        TaskManager managerLoading = FileBackedTaskManager.loadFromFile(new File("test.csv"));
        Task task2 = managerLoading.get(1);
        assertEquals(task1, task2);
    }
}
