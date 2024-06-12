package converter;

import model.Task;

public class TaskConverter {
    public static String toString(Task task) {
        return task.getId() + "," + task.getTypeTask() + "," + task.getTitle() + "," + task.getStatus() +
                "," + task.getDescription() + "," + task.getEpicId() + "," + task.getStartTime() + "," + task.getDuration();
    }
}
