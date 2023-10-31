import enums.Status;
import enums.Types;
import http.server.HttpTaskServer;
import http.server.KVServer;
import manager.HttpTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static Util.Constant.URL;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        HttpTaskManager manager = new HttpTaskManager(URL);
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();

        Task task1 = new Task(
                1,
                Types.TASK,
                "Task 1",
                Status.NEW,
                "Описание Task 1",
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        Task task2 = new Task(
                2,
                Types.TASK,
                "Task 2",
                Status.NEW,
                "Описание Task 2",
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        Task task3 = new Task(
                3,
                Types.TASK,
                "Task 3",
                Status.NEW,
                "Описание Task 3",
                LocalDateTime.now(),
                Duration.ofMinutes(45));

        manager.creationTask(task1);
        manager.creationTask(task2);
        manager.creationTask(task3);

        Epic epic1 = new Epic(
                1,
                Types.EPIC,
                "Epic 1",
                Status.NEW,
                "Описание Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Epic epic2 = new Epic(
                2,
                Types.EPIC,
                "Epic 2",
                Status.NEW,
                "Описание Epic 2",
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Epic epic3 = new Epic(
                3,
                Types.EPIC,
                "Epic 3",
                Status.NEW,
                "Описание Epic 3",
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        manager.creationEpic(epic1);
        manager.creationEpic(epic2);
        manager.creationEpic(epic3);

        Subtask subtaskOneEpicOne = new Subtask(
                1,
                Types.SUBTASK,
                "Subtask 1 Epic 1",
                Status.NEW,
                "Описание Subtask 1 Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(60),
                epic1.getId());
        Subtask subtaskTwoEpicOne = new Subtask(
                2,
                Types.SUBTASK,
                "Subtask 2 Epic 1",
                Status.NEW,
                "Описание Subtask 2 Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(120),
                epic1.getId());
        Subtask subtaskTreeEpicOne = new Subtask(
                3,
                Types.SUBTASK,
                "Subtask 3 Epic 1",
                Status.NEW,
                "Описание Subtask 3 Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(60),
                epic1.getId());
        Subtask subtaskOneEpicTwo = new Subtask(
                1,
                Types.SUBTASK,
                "Subtask 1 Epic 2",
                Status.NEW,
                "Описание Subtask 1 Epic 2",
                LocalDateTime.now(),
                Duration.ofMinutes(300),
                epic2.getId());
        Subtask subtaskTwoEpicTwo = new Subtask(
                2,
                Types.SUBTASK,
                "Subtask 2 Epic 2",
                Status.NEW,
                "Описание Subtask 2 Epic 2",
                LocalDateTime.now(),
                Duration.ofMinutes(60),
                epic2.getId());

        manager.creationSubtask(subtaskOneEpicOne);
        manager.creationSubtask(subtaskTwoEpicOne);
        manager.creationSubtask(subtaskTreeEpicOne);
        manager.creationSubtask(subtaskOneEpicTwo);
        manager.creationSubtask(subtaskTwoEpicTwo);

        manager.getTaskById(0);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(9);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getEpicById(4);
        manager.getEpicById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(8);
        manager.getSubtaskById(9);
        manager.getSubtaskById(10);
        manager.getTaskById(0);
        manager.getSubtaskById(6);
        manager.getSubtaskById(8);
        manager.deleteTaskById(0);
        manager.deleteSubtaskById(10);

        manager.load();
    }
}
