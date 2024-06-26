package model;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String title, String description, Status status, int epicId, LocalDateTime startTime, int duration) {
        super(id, title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, int epicId, LocalDateTime startTime, int duration) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTypeTask() {
        return TaskType.SUBTASK;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + status + '\'' +
                ", startTime" + getStartTime() + '\'' +
                ", duration" + getDuration() + '\'' +
                ", endTime" + getEndTime() +
                '}';
    }
}
