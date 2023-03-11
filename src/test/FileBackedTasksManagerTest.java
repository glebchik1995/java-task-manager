package test;

import enumTask.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    public static final File file = new File("sprint.csv");
    private FileBackedTasksManager fileBackedTaskManager;

    @BeforeEach
    protected void beforeEach() {
        try {
            manager = new FileBackedTasksManager(file);
            fileBackedTaskManager = new FileBackedTasksManager(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    protected void afterEach() {
        try {
            Files.delete(file.toPath());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    protected void shouldTrueLoadDataFromFile() {

        Task task = new Task("Сходить в магазин", "Уложиться в 2 тыс.руб.",
                Status.NEW, LocalDateTime.of(2021, Month.MAY, 15, 20, 5),
                Duration.ofMinutes(10));
        fileBackedTaskManager.creationTask(task);


        Epic epic = new Epic("Сделать 7-й проект в Я.Практикуме", "Сдать любой ценой!",
                Status.NEW, LocalDateTime.of(2022, Month.JANUARY, 5, 15, 10),
                Duration.ofMinutes(30));
        fileBackedTaskManager.creationEpic(epic);

        Subtask subtask = new Subtask("Разобраться в комментариях ревьюера", "Исправить ошибки",
                Status.NEW, 2, LocalDateTime.now(), Duration.ofMinutes(60));
        fileBackedTaskManager.creationSubtask(subtask);


        fileBackedTaskManager.getTaskById(task.getId());
        fileBackedTaskManager.getSubtaskById(subtask.getId());
        fileBackedTaskManager.getEpicById(epic.getId());

        FileBackedTasksManager.loadFromFile(new File("sprint.csv"));


        assertNotNull(fileBackedTaskManager.history());
        assertEquals(new ArrayList<>(Map.of(task.getId(), task).values()),
                new ArrayList<>(fileBackedTaskManager.getTasks()));

        assertEquals(new ArrayList<>(Map.of(epic.getId(), epic).values()),
                new ArrayList<>(fileBackedTaskManager.getEpics()));
        assertEquals(new ArrayList<>(Map.of(subtask.getId(), subtask).values()),
                new ArrayList<>(fileBackedTaskManager.getSubtasks()));
        assertEquals(List.of(task, subtask, epic), fileBackedTaskManager.history());

    }

    @Test
    protected void shouldTrueLoadDataFromFileWithEmptyHistory() {

        Task task = new Task("Сходить в магазин", "Уложиться в 2 тыс.руб.",
                Status.NEW, LocalDateTime.of(2021, Month.MAY, 15, 20, 5),
                Duration.ofMinutes(10));
        fileBackedTaskManager.creationTask(task);


        Epic epic = new Epic("Сделать 7-й проект в Я.Практикуме", "Сдать любой ценой!",
                Status.NEW, LocalDateTime.of(2022, Month.JANUARY, 5, 15, 10),
                Duration.ofMinutes(30));
        fileBackedTaskManager.creationEpic(epic);

        Subtask subtask = new Subtask("Разобраться в комментариях ревьюера", "Исправить ошибки",
                Status.NEW, 2, LocalDateTime.now(), Duration.ofMinutes(60));
        fileBackedTaskManager.creationSubtask(subtask);

        FileBackedTasksManager.loadFromFile(new File(String.valueOf(file)));

        assertEquals(Collections.EMPTY_LIST, fileBackedTaskManager.history());
    }
    @Test
    public void shouldSaveAndLoadIfTasksOrEpicsOrSubtasksIsEmpty() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        fileManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
        assertEquals(Collections.EMPTY_LIST, manager.getSubtasks());
    }
}
