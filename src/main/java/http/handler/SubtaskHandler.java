package http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Util.DurationAdapter;
import Util.LocalDateTimeTypeAdapter;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static Util.Constant.*;

public class SubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public SubtaskHandler(TaskManager taskManager) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Соединение прошло успешно!");
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        URI requestURI = exchange.getRequestURI();

        switch (requestMethod) {
            case GET -> {
                if (requestURI.toString().contains("?id=")) {
                    String[] splitStrings = path.split("/");
                    String placeInArray = splitStrings[3];
                    Subtask subTask = taskManager.getSubtaskById(Integer.parseInt(placeInArray));
                    String body = gson.toJson(subTask);
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(body.getBytes(DEFAULT_CHARSET));
                    }
                    exchange.close();
                    break;
                }
                if (requestURI.toString().contains("?idEpic=")) {
                    String[] splitStrings = path.split("/");
                    String placeInArray = splitStrings[3];
                    List<Subtask> subtasksByEpic = taskManager.getSubtaskByEpicId(Integer.parseInt(placeInArray));
                    String body = gson.toJson(subtasksByEpic);
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                    }
                    exchange.close();
                    break;
                }
                List<Subtask> allSubtasks = taskManager.getSubtasks();
                String body = gson.toJson(allSubtasks);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                }
                exchange.close();
            }
            case POST -> {
                InputStream inputBody = exchange.getRequestBody();
                String stringBody = new String(inputBody.readAllBytes());
                Subtask subTask = gson.fromJson(stringBody, Subtask.class);
                taskManager.creationSubtask(subTask);
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            }
            case PUT -> {
                InputStream inputBody1 = exchange.getRequestBody();
                String stringBody1 = new String(inputBody1.readAllBytes());
                Subtask subTask = gson.fromJson(stringBody1, Subtask.class);
                taskManager.updateSubtask(subTask);
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            }
            case DELETE -> {
                if (requestURI.toString().contains("?id=")) {
                    String[] splitStrings = path.split("/");
                    String placeInArray = splitStrings[3];
                    taskManager.deleteEpicById(Integer.parseInt(placeInArray));
                    exchange.sendResponseHeaders(201, 0);
                    exchange.close();
                    break;
                }
                taskManager.deleteSubtasks();
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            }
            default -> {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
            }
        }
    }
}
