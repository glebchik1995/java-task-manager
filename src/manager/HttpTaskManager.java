package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.DurationAdapter;
import json.LocalDateTimeTypeAdapter;
import http.KVTaskClient;

import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient client;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);
    }


    @Override
    public void save() {
        client.save("task", gson.toJson(getTasks()));
        client.save("epic", gson.toJson(getEpics()));
        client.save("subtask", gson.toJson(getSubtasks()));
        client.save("history",gson.toJson(history()));
    }

    public void load() {
        client.load("task");
        client.load("epic");
        client.load("subtask");
        client.load("history");
    }
}
