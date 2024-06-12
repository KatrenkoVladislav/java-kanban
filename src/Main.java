import model.Epic;
import model.Status;
import model.Subtask;
import service.Manager;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Manager.getDefault();


/*        Task task1 = new Task("Задача1", "Обычная", Status.NEW);
        Task task2 = new Task("Задача2", "Легкая", Status.NEW,
                LocalDateTime.of(2024, 6, 9, 19, 0, 0), Duration.ofMinutes(54));
        manager.create(task1);
        manager.create(task2);
      */
        /*Task task = manager.create(new Task("Временная","обычная",Status.NEW,
                LocalDateTime.of(2024,6,6,22,40,0), Duration.ofMinutes(10)));*/
        //System.out.println(task1);
       /* task1.setStartTime(LocalDateTime.of(2024,6,6,23,0,0));
        task1.setDuration(Duration.ofMinutes(10));
        manager.update(task1);
        Task task2 = manager.create(new Task("Задача2", "Обычная", Status.NEW));*/

        Epic epic1 = manager.createEpic(new Epic("Эпик1", "Особый",
                LocalDateTime.of(2024, 6, 5, 20, 0), Duration.ofMinutes(0)));
        Subtask subtask1 = manager.createSubTask(new Subtask("Подзадача1", "Легкая", Status.DONE, epic1.getId(),
                LocalDateTime.of(2024, 6, 6, 21, 20), Duration.ofMinutes(30)));
        Subtask subtask2 = manager.createSubTask(new Subtask("Подзадача2", "Легкая", Status.DONE, epic1.getId(),
                LocalDateTime.of(2024, 6, 6, 21, 0), Duration.ofMinutes(30)));
        Subtask subtask3 = manager.createSubTask(new Subtask("Подзадача3", "Легкая", Status.DONE, epic1.getId()));
        System.out.println(epic1);

        Epic epic2 = manager.createEpic(new Epic("Эпик2", "Особый"));

        /*System.out.println(manager.getPrioritizedTask());
        TaskManager taskManagerReloud = FileBackedTaskManager.loadFromFile(new File("task.csv"));
        if (manager.get(1).getId() == taskManagerReloud.get(1).getId()) {
            System.out.println("Задачи совпадают");
        }*/


    }

}
