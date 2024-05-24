package service;

public class Manager {
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();

    }
}
