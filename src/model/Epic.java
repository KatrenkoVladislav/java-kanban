package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private transient ArrayList<Integer> subTasksId = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(int id, String title, String description, LocalDateTime startTime, int duration) {
        super(id, title, description, Status.NEW, startTime, duration);
    }

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }

    public Epic(String title, String description, LocalDateTime localDateTime, int duration) {
        super(title, description, Status.NEW, localDateTime, duration);
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
                ", status=" + status + '\'' +
                ", startTime" + getStartTime() + '\'' +
                ", duration" + getDuration() + '\'' +
                ", endTime" + endTime + '\'' +
                "} SubTask=" + subTasksId +
                '}';
    }

    public void setSubTasksId(ArrayList<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }
}
