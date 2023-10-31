package http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Util.DurationAdapter;
import Util.LocalDateTimeTypeAdapter;
import manager.TaskManager;
import model.Task;

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

public class TaskHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager taskManager) {
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
                    Task task = taskManager.getTaskById(Integer.parseInt(placeInArray));
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream outputStream = exchange.getResponseBody()) {
                        outputStream.write(gson.toJson(task).getBytes(DEFAULT_CHARSET));
                    }
                    exchange.close();
                    break;
                }
                if (path.contains("history")) {
                    List<Task> tasks = taskManager.getPrioritizedTasks();
                    String body = gson.toJson(tasks);
                    exchange.sendResponseHeaders(200, 0);
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(body.getBytes(DEFAULT_CHARSET));
                    }
                    exchange.close();
                }
                List<Task> allTasks = taskManager.getTasks();
                String body = gson.toJson(allTasks);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(body.getBytes(DEFAULT_CHARSET));
                }
                exchange.close();
            }
            case POST -> {
                InputStream inputBody = exchange.getRequestBody();
                String stringBody = new String(inputBody.readAllBytes());
                Task task = gson.fromJson(stringBody, Task.class);
                taskManager.creationTask(task);
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            }
            case PUT -> {
                InputStream inputBody = exchange.getRequestBody();
                String stringBody = new String(inputBody.readAllBytes());
                Task task = gson.fromJson(stringBody, Task.class);
                taskManager.updateTask(task);
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            }
            case DELETE -> {
                if (requestURI.toString().contains("?id=")) {
                    String[] splitStrings = path.split("/");
                    String placeInArray = splitStrings[3];
                    taskManager.deleteTaskById(Integer.parseInt(placeInArray));
                    exchange.sendResponseHeaders(201, 0);
                    exchange.close();
                    break;
                }
                taskManager.deleteTasks();
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

