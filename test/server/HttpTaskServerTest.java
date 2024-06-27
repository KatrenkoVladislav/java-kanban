package server;

import com.google.gson.Gson;
import handlers.BaseHttpHandler;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Manager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskServerTest {
    TaskManager manager = Manager.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();

    @BeforeEach
    public void start() {
        TaskManager manager = Manager.getDefault();
        manager.deleteAll();
        manager.deleteAllEpic();
        server.start();

    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @DisplayName("Добавляем и проверяем сохранение задачи")
    @Test
    public void shouldSaveTask() throws IOException, InterruptedException {

        Task task = new Task("Задача", "Описание",
                Status.NEW, LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());


        List<Task> tasksFromManager = manager.getAll();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }


    @DisplayName("Добавляем и проверяем сохранение подзадачи")
    @Test
    public void shouldSaveSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание"
                , LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createEpic(epic);

        Subtask subTask = new Subtask("Подзадача", "описание2",
                Status.NEW, epic.getId(), LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        String subTaskJson = gson.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Subtask> tasksFromManager = manager.getAllSubTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Подзадача", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @DisplayName("Проверяем вызов существующей задачи")
    @Test
    public void shouldGetTasks() throws IOException, InterruptedException {

        Task task = new Task("Задача", "Описание",
                Status.NEW, LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.create(task);

        Task task2 = new Task("Задача2", "описание2",
                Status.NEW, LocalDateTime.of(2024, 6, 13, 18, 38, 20), 15);
        manager.create(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAll();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
        assertEquals("Задача2", tasksFromManager.get(1).getTitle(), "Некорректное имя задачи");
    }

    @DisplayName("Проверяем вызов существующих эпиков")
    @Test
    public void shouldGetEpics() throws IOException, InterruptedException {

        Epic epic = new Epic("Эпик", "Описание",
                LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createEpic(epic);

        Epic epic2 = new Epic("Эпик2", "Описание2",
                LocalDateTime.of(2024, 6, 13, 18, 38, 20), 15);
        manager.createEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());


        List<Epic> tasksFromManager = manager.getAllEpic();

        assertNotNull(tasksFromManager, "Эпики не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Эпик", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
        assertEquals("Эпик2", tasksFromManager.get(1).getTitle(), "Некорректное имя задачи");
    }

    @DisplayName("Проверяем получение всех подзадачи")
    @Test
    public void shouldGetSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание",
                LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createEpic(epic);

        Subtask subTask = new Subtask("Подзадача", "Описание2",
                Status.NEW, epic.getId(), LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createSubTask(subTask);

        Subtask subTask2 = new Subtask("Подзадача2", "Описание3",
                Status.NEW, epic.getId(), LocalDateTime.of(2024, 6, 13, 18, 38, 20), 15);
        manager.createSubTask(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Subtask> tasksFromManager = manager.getAllSubTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Подзадача", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
        assertEquals("Подзадача2", tasksFromManager.get(1).getTitle(), "Некорректное имя задачи");
    }

    @DisplayName("Проверяем вызов задачи по ID")
    @Test
    public void shouldGetTaskByID() throws IOException, InterruptedException {

        Task task = new Task("Задача", "Описание",
                Status.NEW, LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.create(task);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAll();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");

    }

    @DisplayName("Проверяем вызов эпика по ID")
    @Test
    public void shouldGetEpicById() throws IOException, InterruptedException {

        Epic epic = new Epic("Эпик", "Описание",
                LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createEpic(epic);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());


        List<Epic> tasksFromManager = manager.getAllEpic();

        assertNotNull(tasksFromManager, "Эпики не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Эпик", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");

    }

    @DisplayName("Проверяем получение подзадачи по ID")
    @Test
    public void shouldGetSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание",
                LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createEpic(epic);

        Subtask subTask = new Subtask("Эпик2", "Описание",
                Status.NEW, epic.getId(), LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createSubTask(subTask);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Subtask> tasksFromManager = manager.getAllSubTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Эпик2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");

    }

    @DisplayName("Проверяем удаление эпика по ID")
    @Test
    public void shouldDeleteEpicById() throws IOException, InterruptedException {

        Epic epic = new Epic("Эпик", "Описание",
                LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createEpic(epic);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());


        List<Epic> tasksFromManager = manager.getAllEpic();

        assertEquals(0, tasksFromManager.size(), "Некорректное количество эпиков");

    }

    @DisplayName("Проверяем удаление задачи по ID")
    @Test
    public void shouldDeleteTaskById() throws IOException, InterruptedException {

        Task task = new Task("Задача", "Описание",
                Status.NEW, LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.create(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
        List<Task> tasksFromManager = manager.getAll();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество эпиков");

    }

    @DisplayName("Проверяем удаление подзадачи по ID")
    @Test
    public void shouldDeleteSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание",
                LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createEpic(epic);

        Subtask subTask = new Subtask("Подзадача", "Описание 2",
                Status.NEW, epic.getId(), LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.createSubTask(subTask);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
        List<Subtask> tasksFromManager = manager.getAllSubTask();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @DisplayName("Проверяем обновление задачи")
    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {

        Task task = new Task("Задача", "Описание",
                Status.NEW, LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.create(task);


        Task task1 = new Task(task.getId(), "Задача2", "Описание2",
                Status.NEW, LocalDateTime.of(2024, 3, 13, 18, 38, 20), 15);

        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAll();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @DisplayName("Проверяем обновление подзадачи")
    @Test
    public void shouldUpdateSubTask() throws IOException, InterruptedException {

        Epic epic = new Epic("Эпик", "Описание", LocalDateTime.of(2024, 5, 13, 18, 38, 20), 15);
        manager.createEpic(epic);

        Subtask subTask = new Subtask("Подзадача", "Описание 2",
                Status.NEW, epic.getId(), LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.create(subTask);


        Subtask subTask2 = new Subtask("Подзадача2", "Описание 3",
                Status.NEW, epic.getId(), LocalDateTime.of(2024, 8, 13, 18, 38, 20), 15);

        String taskJson = gson.toJson(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Subtask> tasksFromManager = manager.getAllSubTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Подзадача2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание",
                Status.NEW, LocalDateTime.of(2024, 5, 13, 18, 38, 20), 15);
        manager.create(task);
        Task task2 = new Task("Задача2", "Описание",
                Status.NEW, LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.create(task2);

        manager.get(task.getId());
        manager.get(task2.getId());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getHistory();
        assertEquals(expectedTasks.size(), 2);
    }

    @Test
    void getPrioritizedTask() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание",
                Status.NEW, LocalDateTime.of(2024, 5, 13, 18, 38, 20), 15);
        manager.create(task);
        Task task2 = new Task("Задача2", "Описание",
                Status.NEW, LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        manager.create(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getPrioritizedTask();
        assertEquals(expectedTasks.size(), 2);
    }
}
