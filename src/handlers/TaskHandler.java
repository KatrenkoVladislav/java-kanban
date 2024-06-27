package handlers;

import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import model.Task;
import server.HttpMethod;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] params = path.split("/");
            try {
                switch (method) {
                    case HttpMethod.GET:
                        if (params.length == 2) {

                            writeResponse(exchange, getGson().toJson(taskManager.getAll()), 200);

                        } else if (params.length == 3) {

                            writeResponse(exchange, getGson().toJson(taskManager.get(getIdFromPath(params[2]))), 200);
                        }
                        break;
                    case HttpMethod.POST:
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Task task = getGson().fromJson(body, Task.class);
                        try {
                            taskManager.get(task.getId());
                            taskManager.update(task);
                            writeResponse(exchange, "Задача обновлена", 201);
                        } catch (NotFoundException e) {
                            taskManager.create(task);
                            writeResponse(exchange, "Задача создана", 201);
                        }
                        break;
                    case HttpMethod.DELETE:
                        int id = getIdFromPath(params[2]);

                        if (id == -1) {
                            writeResponse(exchange, "Нет данных с таким номером", 404);
                        } else {
                            taskManager.delete(id);
                            sendResponseCode(exchange);
                        }
                        break;
                }
            } catch (Exception exception) {
                exception(exchange, exception);
            }
    }
}
