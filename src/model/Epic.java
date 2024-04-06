package model;

import java.util.ArrayList;

public class Epic extends Task {
private ArrayList<Subtask> subTasks = new ArrayList<>();
    public Epic(String title,String description){

        super(title,description,Status.NEW);
    }

    public ArrayList<Subtask> getSubtasks() {
    return subTasks;
    }
    public void addTask(Subtask subtask){
      subTasks.add(subtask);
    }
    public void removeAllTask(){
    subTasks.clear();
    }
    public void removeTask(Subtask subtask){
    subTasks.remove(subtask.getId());
    }
    public void updateStatus(){
       boolean allDone = true;
       boolean allNew = true;
       if (subTasks.isEmpty()){
           status = Status.NEW;
       }
       for (Subtask subtask: subTasks){
           if (subtask.getStatus() != Status.DONE){
               allDone = false;
           }
           if (subtask.getStatus() != Status.NEW){
               allNew = false;
           }
       }
       if (allDone){
           status = Status.DONE;
       }else if (allNew){
           status = Status.NEW;
       }else {
           status = Status.IN_PROGRESS;
       }

    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + status + '\'' +
                "} SubTask=" + subTasks +
                 '}';
    }
}
