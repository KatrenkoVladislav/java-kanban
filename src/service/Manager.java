package service;

import java.io.File;

public class Manager {
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("resources/task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();

    }
}
