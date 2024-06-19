package service;

import java.io.File;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    public FileBackedTaskManager createManager() {
        File file = new File("resources/task.csv");
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        return new FileBackedTaskManager(historyManager, file);
    }
}
