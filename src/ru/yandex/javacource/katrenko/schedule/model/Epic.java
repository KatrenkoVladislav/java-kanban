package ru.yandex.javacource.katrenko.schedule.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksId = new ArrayList<>();

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
        subTasksId.remove(subtask);
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
}
