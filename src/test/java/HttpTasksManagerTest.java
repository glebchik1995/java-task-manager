import enums.Status;
import enums.Types;
import exception.ManagerSaveException;
import http.client.KVTaskClient;
import http.server.KVServer;
import manager.HttpTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static Util.Constant.URL;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTasksManagerTest {

    private Task task;

    private Epic epic;

    private Subtask subtask;

    private KVTaskClient client;

    private KVServer server;

    private HttpTaskManager manager;

    @BeforeEach
    public void beforeEach() throws IOException {

        server = new KVServer();

        server.start();

        manager = new HttpTaskManager(URL);

        client = new KVTaskClient(URL);

        task = new Task(
                1,
                Types.TASK,
                "Task_№1",
                Status.NEW,
                "Описание Task_№1",
                LocalDateTime.of(2022, Month.APRIL, 1, 10, 10),
                Duration.ofMinutes(30));

        manager.creationTask(task);

        epic = new Epic(
                1,
                Types.EPIC,
                "Epic 1",
                Status.NEW,
                "Описание Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        manager.creationEpic(epic);

        subtask = new Subtask(
                1,
                Types.SUBTASK,
                "Subtask 1 Epic 1",
                Status.NEW,
                "Описание Subtask 1 Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(60),
                epic.getId());

        manager.creationSubtask(subtask);
    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    @Test
    void loadFromServerWhenItEmpty() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    client.load("task");
                    client.load("epic");
                    client.load("subtask");
                });
        assertEquals("Загрузка закончилась неудачно. Причина:HTTP/1.1 header parser received no bytes",
                exception.getMessage());
    }

    @Test
    void loadFromServer() {

        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());


        manager.load();

        List<Task> tasks = manager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        List<Task> history = manager.findHistory();

        assertNotNull(history);
        assertEquals(3, history.size());

    }
}

