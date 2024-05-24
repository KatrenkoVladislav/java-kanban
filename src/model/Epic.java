package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW);
    }
    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }


    public ArrayList<Integer> getSubtasks() {
        return subTasksId;
    }

    public void addTask(Integer subtask) {
        subTasksId.add(subtask);
    }

    public void removeAllTask() {
        subTasksId.clear();
    }

    public void removeTask(int subtask) {
        Integer subtask1 = subtask;
        subTasksId.remove(subtask1);
    }

    @Override
    public Integer getEpicId() {
        return null;
    }

    @Override
    public TaskType getTypeTask() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + status +
                "} SubTask=" + subTasksId +
                '}';
    }

    public void setSubTasksId(ArrayList<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }
}
