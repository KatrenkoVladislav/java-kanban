package service;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createManager() {
        return manager = (InMemoryTaskManager) Manager.getDefault();
    }
}
