package handlers;

import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import model.Epic;
import server.HttpMethod;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка Эпика");

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String[] pathParts = path.split("/");
        try {
            switch (method) {
                case HttpMethod.GET:

                    if (pathParts.length == 2) {
                        writeResponse(exchange, getGson().toJson(taskManager.getAllEpic()), 200);

                    } else if (pathParts.length == 3) {

                        writeResponse(exchange, getGson().toJson(taskManager.getEpic(getIdFromPath(pathParts[2]))), 200);
                    } else if (pathParts.length > 3) {

                        writeResponse(exchange, getGson().toJson(taskManager.getSubTaskForEpic(taskManager.getEpic(getIdFromPath(pathParts[2])))), 200);
                    }
                    break;
                case HttpMethod.POST:

                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = getGson().fromJson(body, Epic.class);
                    if (epic.getSubtasks() == null) {
                        epic.setSubTasksId(new ArrayList<>());
                    }
                    try {
                        taskManager.getEpic(epic.getId());
                    } catch (NotFoundException e) {
                        taskManager.createEpic(epic);
                        writeResponse(exchange, "Эпик создан", 201);
                    }

                    break;
                case HttpMethod.DELETE:

                    int id = getIdFromPath(pathParts[2]);
                    if (id == -1) {
                        writeResponse(exchange, "Такого эпика нет", 404);
                    } else {
                        Epic epic2 = taskManager.getEpic(id);
                        List<Integer> listSubTasks = epic2.getSubtasks();
                        listSubTasks.clear();
                        taskManager.deleteEpic(id);
                        sendResponseCode(exchange);
                    }
                    break;
            }
        } catch (Exception e) {
            exception(exchange, e);
        }
    }
}