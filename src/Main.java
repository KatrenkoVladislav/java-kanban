import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.Manager;
import service.TaskManager;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Manager.getDefault();

        Task task1 = manager.create(new Task("Задача1", "Обычная", Status.NEW));
        Task task2 = manager.create(new Task("Задача2", "Обычная", Status.NEW));

        Epic epic1 = manager.createEpic(new Epic("Эпик1", "Особый"));
        Subtask subtask1 = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.DONE, epic1.getId()));
        Subtask subtask2 = manager.createSubTask(new Subtask("Подзадача2", "Легкая", Status.DONE, epic1.getId()));
        Subtask subtask3 = manager.createSubTask(new Subtask("Подзадача3", "Легкая", Status.DONE, epic1.getId()));

        Epic epic2 = manager.createEpic(new Epic("Эпик2", "Особый"));

        TaskManager taskManagerReloud = FileBackedTaskManager.loadFromFile(new File("task.csv"));
        if (manager.get(1).getId() == taskManagerReloud.get(1).getId()) {
            System.out.println("Задачи совпадают");
        }


    }

}
