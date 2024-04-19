
import model.Task;
import model.Status;
import model.Subtask;
import model.Epic;
import service.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Manager.getDefault();

        Epic epic = manager.createEpic(new Epic("Эпик","Особый"));
        Subtask subtask = manager.createSubTask(new Subtask("Подзадача1","Легкая",Status.DONE,epic.getId()));
        manager.deleteSubTask(2);

    }

}
