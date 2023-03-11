package test;

import enumTask.Status;
import task.Task;
import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    private int id = 0;

    private Task createTask() {
        return new Task("Тест", "История", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0));
    }

    @BeforeEach
    protected void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    protected void shouldAddTasksToHistory() {
        Task simpleTask1 = createTask();
        Task simpleTask2 = createTask();
        Task simpleTask3 = createTask();
        simpleTask1.setId(++id);
        simpleTask2.setId(++id);
        simpleTask3.setId(++id);
        historyManager.add(simpleTask1);
        historyManager.add(simpleTask2);
        historyManager.add(simpleTask3);
        List<Task> list = List.of(simpleTask1, simpleTask2, simpleTask3);
        assertEquals(list, historyManager.getHistory());
    }

    @Test
    protected void shouldRemoveTask() {
        Task simpleTask1 = createTask();
        Task simpleTask2 = createTask();
        Task simpleTask3 = createTask();
        simpleTask1.setId(++id);
        simpleTask2.setId(++id);
        simpleTask3.setId(++id);
        historyManager.add(simpleTask1);
        historyManager.add(simpleTask2);
        historyManager.add(simpleTask3);
        List<Task> list = List.of(simpleTask2, simpleTask3);
        historyManager.remove(simpleTask1.getId());
        assertEquals(list, historyManager.getHistory());
    }

    @Test
    protected void shouldRemoveOneTask() {
        Task simpleTask1 = createTask();
        Task simpleTask2 = createTask();
        simpleTask1.setId(++id);
        simpleTask2.setId(++id);
        List<Task> list = List.of(simpleTask2);
        historyManager.add(simpleTask1);
        historyManager.add(simpleTask2);
        historyManager.remove(simpleTask1.getId());
        assertEquals(list, historyManager.getHistory());
    }

    @Test
    protected void shouldHistoryIsEmpty() {
        Task simpleTask1 = createTask();
        Task simpleTask2 = createTask();
        Task simpleTask3 = createTask();
        simpleTask1.setId(++id);
        simpleTask2.setId(++id);
        simpleTask3.setId(++id);
        historyManager.remove(simpleTask1.getId());
        historyManager.remove(simpleTask2.getId());
        historyManager.remove(simpleTask3.getId());
        assertEquals(Collections.EMPTY_LIST, historyManager.getHistory());
    }

    @Test
    protected void HistoryShouldBeWithFalseID() {
        Task simpleTask1 = createTask();
        simpleTask1.setId(++id);
        List<Task> list = List.of(simpleTask1);
        historyManager.add(simpleTask1);
        historyManager.remove(0);
        assertEquals(list, historyManager.getHistory());
    }
}