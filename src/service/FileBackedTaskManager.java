package service;

import converter.TaskConverter;
import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this(Manager.getDefaultHistory(), file);
    }

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
        file = new File("resources/task.csv");
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.loadFromFile();
        return manager;
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public Task create(Task task) {
        super.create(task);
        save();
        return task;
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void delete(int id) {
        super.delete(id);
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public Subtask createSubTask(Subtask subtask) {
        super.createSubTask(subtask);
        save();
        return subtask;
    }

    @Override
    public Subtask updateSubTask(Subtask subtask) {
        super.updateSubTask(subtask);
        save();
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    private Task fromString(String value) {
        final String[] valueTask = value.split(",");

        int id = Integer.parseInt(valueTask[0]);
        String title = valueTask[2];
        String description = valueTask[4];
        Status status = Status.valueOf(valueTask[3]);

        TaskType taskType = TaskType.valueOf(valueTask[1]);
        Task task = null;
        switch (taskType) {
            case TASK:
                task = new Task(id, title, description, status);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(valueTask[5]);
                task = new Subtask(id, title, description, status, epicId);
                break;
            case EPIC:
                task = new Epic(id, title, description);
                break;

        }
        return task;
    }

    private void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("id,type,title,status,description,epic");
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.append(TaskConverter.toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.append(TaskConverter.toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : subTasks.entrySet()) {
                writer.append(TaskConverter.toString(entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath());
        }
    }

    private void loadFromFile() {
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                final Task task = fromString(line);
                final int id = task.getId();
                if (task.getTypeTask() == TaskType.TASK) {
                    tasks.put(id, task);
                } else if (task.getTypeTask() == TaskType.SUBTASK) {
                    subTasks.put(id, (Subtask) task);
                } else if (task.getTypeTask() == TaskType.EPIC) {
                    epics.put(id, (Epic) task);
                }
                if (maxId < id) {
                    maxId = id;
                }
            }
            for (Subtask subtask : subTasks.values()) {
                Epic epic = epics.get(subtask.getEpicId());
                epic.addTask(subtask.getId());
            }
            seq = maxId;
        } catch (IOException e) {
            throw new ManagerSaveException("Нет такого файла" + file);
        }
    }
}
