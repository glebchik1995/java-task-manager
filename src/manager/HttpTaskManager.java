package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import enumTask.Types;
import http.KVTaskClient;
import json.DurationAdapter;
import json.LocalDateTimeTypeAdapter;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    protected KVTaskClient client;
    private final Gson gson;


    public HttpTaskManager(int port) {
        client = new KVTaskClient(port);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @Override
    public void save() {
        String allTasks = gson.toJson(tasks);
        client.save("tasks/task", allTasks);
        String allEpics = gson.toJson(tasks);
        client.save("tasks/epic", allEpics);
        String allSubTasks = gson.toJson(getSubtasks());
        client.save("tasks/subtask", allSubTasks);
        String history = gson.toJson(history());
        client.save("tasks/history", history);
        String prioritizedTask = gson.toJson(getPrioritizedTasks());
        client.save("tasks", prioritizedTask);
    }

    protected void addTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            final int id = task.getId();
            if (id > counterId) {
                counterId = id;
            }
            Types type = task.getType();
            if (type == Types.TASK) {
                this.tasks.put(id, task);
                prioritizedTasks.add(task);
            } else if (type == Types.SUBTASK) {
                subtasks.put(id, (Subtask) task);
                prioritizedTasks.add(task);
            } else if (type == Types.EPIC) {
                epics.put(id, (Epic) task);
            }
        }


    }

    public void load() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);


        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);


        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addTasks(subtasks);


        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());


        for (Integer taskId : history) {
            historyManager.add(getTaskById(taskId));
        }





//        String gsonTasks = client.load("tasks/task");
//        Type typeTask = new TypeToken<Map<Integer, Task>>() {
//        }.getType();
//        Map<Integer, Task> task = gson.fromJson(gsonTasks, typeTask);
//        tasks.putAll(task);
//
//        String gsonEpics = client.load("tasks/epic");
//        Type typeEpic = new TypeToken<Map<Integer, Epic>>() {
//        }.getType();
//        Map<Integer, Epic> epic = gson.fromJson(gsonEpics, typeEpic);
//        epics.putAll(epic);
//
//        String gsonSubTasks = client.load("tasks/subtask");
//        Type typeSubTask = new TypeToken<Map<Integer, Subtask>>() {
//        }.getType();
//        Map<Integer, Subtask> subtask = gson.fromJson(gsonSubTasks, typeSubTask);
//        subtasks.putAll(subtask);
//
//        String gsonHistory = client.load("tasks/history");
//        Type typeHistory = new TypeToken<List<Task>>() {
//        }.getType();
//        List<Task> historyOfTasks = gson.fromJson(gsonHistory, typeHistory);
//        history().addAll(historyOfTasks);
//
//        String prioritizedTasks = client.load("tasks");
//        Type typePriorityTask = new TypeToken<List<Task>>() {
//        }.getType();
//        List<Task> priorityTask = gson.fromJson(prioritizedTasks, typePriorityTask);
//        getPrioritizedTasks().addAll(priorityTask);

    }
}
