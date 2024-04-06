import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = taskManager.create(new Task("Задача 1", "Чудо", Status.NEW));
        Task task1 = taskManager.create(new Task("Задача 2", "Класс", Status.IN_PROGRESS));


        Epic epic = taskManager.createEpic(new Epic("Эпик 1", "Сложный"));
        Subtask subtask = taskManager.createSubTask(new Subtask("Сабтаск 1", "Среднее", Status.NEW, epic));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("Сабтаск 2", "средне", Status.DONE, epic));
        taskManager.deleteSubTask(4);

        Epic epic1 = taskManager.createEpic(new Epic("Эпик 2", "Супер сложный"));
        Subtask subtask2 = taskManager.createSubTask(new Subtask("Сабтаск 3", "Ультра", Status.NEW, epic1));

        System.out.println(taskManager.getAll());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getSubTaskForEpic(epic));

    }

}
