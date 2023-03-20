package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import json.DurationAdapter;
import json.LocalDateTimeTypeAdapter;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public HistoryHandler(TaskManager taskManager) {
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
        if ("GET".equals(requestMethod)) {
            if (path.contains("history")) {
                List<Task> tasks = taskManager.getPrioritizedTasks();
                String body = gson.toJson(tasks);
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream responseBody = exchange.getResponseBody()) {
                    responseBody.write(body.getBytes(DEFAULT_CHARSET));
                }
                exchange.close();
            }
        }
    }
}
