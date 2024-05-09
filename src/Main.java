
import model.Task;
import model.Status;
import model.Subtask;
import model.Epic;
import service.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Manager.getDefault();

        Task task1 = manager.create(new Task("Задача1","Обычная",Status.NEW));
        Task task2 = manager.create(new Task("Задача2","Обычная",Status.NEW));

        Epic epic1 = manager.createEpic(new Epic("Эпик1","Особый"));
        Subtask subtask1 = manager.createSubTask(new Subtask("Подзадача1","Легкая",Status.DONE,epic1.getId()));
        Subtask subtask2 = manager.createSubTask(new Subtask("Подзадача2","Легкая",Status.DONE,epic1.getId()));
        Subtask subtask3 = manager.createSubTask(new Subtask("Подзадача3","Легкая",Status.DONE,epic1.getId()));

        Epic epic2 = manager.createEpic(new Epic("Эпик2","Особый"));

        manager.getEpic(epic2.getId());
        System.out.println(manager.getHistory());
        manager.get(task2.getId());
        System.out.println(manager.getHistory());
        manager.getEpic(epic1.getId());
        System.out.println(manager.getHistory());
        manager.getSubTask(subtask2.getId());
        System.out.println(manager.getHistory());
        manager.getSubTask(subtask1.getId());
        System.out.println(manager.getHistory());
        manager.getEpic(epic2.getId());
        System.out.println(manager.getHistory());
        manager.getSubTask(subtask3.getId());
        System.out.println(manager.getHistory());
        manager.get(task1.getId());
        System.out.println(manager.getHistory());
        manager.get(task1.getId());
        System.out.println(manager.getHistory());
        manager.delete(task2.getId());
        System.out.println(manager.getHistory());
        manager.deleteEpic(epic1.getId());
        System.out.println(manager.getHistory());


    }

}
