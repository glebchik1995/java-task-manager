import enums.Status;
import enums.Types;
import model.Task;
import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    private Task t1;

    private Task t2;

    private Task t3;


    @BeforeEach
    void beforeEach() {

        historyManager = new InMemoryHistoryManager();

        t1 = new Task(
                1,
                Types.TASK,
                "Task_№1",
                Status.NEW,
                "Описание Task_№1",
                LocalDateTime.of(2022, Month.APRIL, 1, 10, 10),
                Duration.ofMinutes(30));

        t2 = new Task(
                2,
                Types.TASK,
                "Task_№2",
                Status.NEW,
                "Описание Task_№2",
                LocalDateTime.of(2022, Month.MAY, 2, 11, 11),
                Duration.ofMinutes(45));

        t3 = new Task(
                3,
                Types.TASK,
                "Task_№3",
                Status.NEW,
                "Описание Task_№3",
                LocalDateTime.of(2022, Month.JUNE, 2, 12, 12),
                Duration.ofMinutes(60));
    }

    @Test
    void shouldAddTasksToHistory() {
        historyManager.add(t1);
        historyManager.add(t2);
        historyManager.add(t3);
        assertEquals(List.of(t1, t2, t3), historyManager.getHistory());
    }

    @Test
    void shouldRemoveTask() {

        historyManager.add(t1);
        historyManager.add(t2);
        historyManager.add(t3);

        historyManager.remove(t1.getId());

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(List.of(t2, t3), historyManager.getHistory());

    }


    @Test
    void shouldHistoryIsEmpty() {
        historyManager.remove(t1.getId());
        historyManager.remove(t2.getId());
        historyManager.remove(t3.getId());
        assertEquals(Collections.EMPTY_LIST, historyManager.getHistory());
    }

    @Test
    void shouldNotRemoveTaskFromHistory() {
        historyManager.add(t1);
        historyManager.remove(100);
        assertEquals(List.of(t1), historyManager.getHistory());
        assertEquals(1, historyManager.getHistory().size());
    }
}