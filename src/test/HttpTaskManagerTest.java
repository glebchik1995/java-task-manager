import enumTask.Status;
import http.KVServer;
import manager.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest {

    HttpTaskManager manager;
    protected KVServer server;

    @BeforeEach
    public void beforeEach() throws IOException {
        server = new KVServer();
        server.start();
        manager = new HttpTaskManager("http://localhost:8080/");

    }

    @AfterEach
    void serverStop() {

        server.stop();
    }

    @Test
    void loadFromServerTest() {
        Task task = new Task("Сходить в магазин", "Уложиться в 2 тыс.руб.",
                Status.NEW, LocalDateTime.of(2021, Month.MAY, 15, 20, 5),
                Duration.ofMinutes(10));
        manager.creationTask(task);


        Epic epic = new Epic("Сделать 7-й проект в Я.Практикуме", "Сдать любой ценой!",
                Status.NEW, LocalDateTime.of(2022, Month.JANUARY, 5, 15, 10),
                Duration.ofMinutes(30));
        manager.creationEpic(epic);

        Subtask subtask = new Subtask("Разобраться в комментариях ревьюера", "Исправить ошибки",
                Status.NEW, 2, LocalDateTime.now(), Duration.ofMinutes(60));
        manager.creationSubtask(subtask);

        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());

        manager.load();

        List<Task> tasks = manager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        List<Task> history = manager.history();

        assertNotNull(history);
        assertEquals(3, history.size());

    }

    @Test
    void SaveOnServerTest() {

        Task task = new Task("Сходить в магазин", "Уложиться в 2 тыс.руб.",
                Status.NEW, LocalDateTime.of(2021, Month.MAY, 15, 20, 5),
                Duration.ofMinutes(10));
        manager.creationTask(task);


        Epic epic = new Epic("Сделать 7-й проект в Я.Практикуме", "Сдать любой ценой!",
                Status.NEW, LocalDateTime.of(2022, Month.JANUARY, 5, 15, 10),
                Duration.ofMinutes(30));
        manager.creationEpic(epic);

        Subtask subtask = new Subtask("Разобраться в комментариях ревьюера", "Исправить ошибки",
                Status.NEW, 2, LocalDateTime.now(), Duration.ofMinutes(60));
        manager.creationSubtask(subtask);

        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());

        manager.save();

        List<Task> tasks = manager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        List<Task> history = manager.history();

        assertNotNull(history);
        assertEquals(3, history.size());

    }

}
