import enums.Status;
import enums.Types;
import manager.FileBackedTasksManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private FileBackedTasksManager fileBackedTaskManager;

    private Task task;

    private Epic epic;

    private Subtask subtask;

    @BeforeEach
    public void beforeEach() {

        manager = new FileBackedTasksManager(new File("src/resources/testFile.csv"));

        fileBackedTaskManager = new FileBackedTasksManager(new File("src/resources/testFile.csv"));

        task = new Task(
                1,
                Types.TASK,
                "Task_№1",
                Status.NEW,
                "Описание Task_№1",
                LocalDateTime.of(2022, Month.APRIL, 1, 10, 10),
                Duration.ofMinutes(30));

        fileBackedTaskManager.creationTask(task);

        epic = new Epic(
                1,
                Types.EPIC,
                "Epic_№1",
                Status.NEW,
                "Описание Epic_№1",
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        fileBackedTaskManager.creationEpic(epic);

        subtask = new Subtask(
                1,
                Types.SUBTASK,
                "Subtask_№1",
                Status.NEW,
                "Описание Subtask_№1 Epic_№1",
                LocalDateTime.now(),
                Duration.ofMinutes(60),
                epic.getId());

        fileBackedTaskManager.creationSubtask(subtask);
    }

    @Test
    public void shouldCorrectlyLoadDataFromFile() {
        fileBackedTaskManager.getTaskById(task.getId());
        fileBackedTaskManager.getEpicById(epic.getId());
        fileBackedTaskManager.getSubtaskById(subtask.getId());

        FileBackedTasksManager.loadFromFile(new File("src/resources/testFile.csv"));

        assertNotNull(fileBackedTaskManager.findHistory());
        assertEquals(List.of(task), fileBackedTaskManager.getTasks());
        assertEquals(List.of(epic), fileBackedTaskManager.getEpics());
        assertEquals(List.of(subtask), fileBackedTaskManager.getSubtasks());
        assertEquals(List.of(task, epic, subtask), fileBackedTaskManager.findHistory());

    }

    @Test
    public void shouldCorrectlyLoadDataFromFileWithEmptyHistory() {
        fileBackedTaskManager.creationTask(task);
        fileBackedTaskManager.creationEpic(epic);
        fileBackedTaskManager.creationSubtask(subtask);

        FileBackedTasksManager.loadFromFile(new File("src/resources/testFile.csv"));
        assertEquals(Collections.EMPTY_LIST, fileBackedTaskManager.findHistory());
    }
}
