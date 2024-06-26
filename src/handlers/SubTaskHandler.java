package handlers;

import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import model.Subtask;
import server.HttpMethod;
import service.TaskManager;

import java.nio.charset.StandardCharsets;

public class SubTaskHandler extends BaseHttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("Началась обработка Задачи");
        try (exchange) {

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] pathParts = path.split("/");
            try {
                switch (method) {
                    case HttpMethod.GET:
                        if (pathParts.length == 2) {
                            writeResponse(exchange, getGson().toJson(taskManager.getAllSubTask()), 200);
                        } else if (pathParts.length == 3) {
                            writeResponse(exchange, getGson().toJson(taskManager.getSubTask(getIdFromPath(pathParts[2]))), 200);
                        }
                        break;
                    case HttpMethod.POST:
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Subtask subTask = getGson().fromJson(body, Subtask.class);

                        try {
                            taskManager.getSubTask(subTask.getId());
                            taskManager.updateSubTask(subTask);
                            writeResponse(exchange, "Подзадача обновлена", 201);
                        } catch (NotFoundException e) {
                            taskManager.createSubTask(subTask);
                            writeResponse(exchange, "Подзадача создана", 201);
                        }
                        break;
                    case HttpMethod.DELETE:
                        int id = getIdFromPath(pathParts[2]);
                        if (id == -1) {
                            writeResponse(exchange, "Нет данных с таким номером", 404);
                        } else {
                            taskManager.deleteSubTask(id);
                            sendResponseCode(exchange);
                        }
                        break;
                }
            } catch (Exception e) {
                exception(exchange, e);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
