import enumTask.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    protected Task createTaskTest() {
        return new Task("TITLE", "DESCRIPTION", Status.NEW,
                LocalDateTime.of(2020, Month.JANUARY, 5, 6, 7),
                Duration.ofMinutes(40));
    }

    protected Epic createEpicTest() {

        return new Epic("TITLE", "DESCRIPTION", Status.NEW,
                LocalDateTime.of(2022, Month.MAY, 17, 18, 19),
                Duration.ofMinutes(50));
    }

    protected Subtask createSubtaskTest() {
        return new Subtask("TITLE", "DESCRIPTION", Status.NEW,
                1, LocalDateTime.of(2022, Month.MAY, 2, 3, 4),
                Duration.ofMinutes(60));
    }

    /**
     * Tasks ↓ ↓ ↓
     */

    @Test
    protected void shouldGetAllTasks() {
        Task taskNew = new Task("TASK_TITLE_NEW", "TASK_DESCRIPTION_NEW", Status.NEW,
                LocalDateTime.of(2020, Month.JANUARY, 5, 6, 7),
                Duration.ofMinutes(40));
        Task taskInProgress = new Task("TASK_TITLE_IN_PROGRESS", "TASK_DESCRIPTION_IN_PROGRESS",
                Status.IN_PROGRESS, LocalDateTime.of(2021, Month.MARCH, 7, 8, 9),
                Duration.ofMinutes(40));
        Task taskDone = new Task("TASK_TITLE_DONE", "TASK_DESCRIPTION_DONE",
                Status.DONE, LocalDateTime.of(2022, Month.APRIL, 9, 10, 11),
                Duration.ofMinutes(40));
        taskNew.setId(1);
        taskInProgress.setId(2);
        taskDone.setId(3);

        manager.creationTask(taskNew);
        manager.creationTask(taskInProgress);
        manager.creationTask(taskDone);

        List<Task> allTasks;
        allTasks = List.copyOf(manager.getTasks());

        List<Task> allTasksTest = List.of(taskNew, taskInProgress, taskDone);

        assertNotNull(allTasks);
        assertEquals(allTasksTest.size(), allTasks.size());

        for (int i = 0; i < allTasks.size(); i++) {
            Task task = allTasks.get(i);
            Task taskTest = allTasksTest.get(i);
            assertEquals(taskTest, task);

        }
    }

    @Test
    protected void shouldDeleteAllTasks() {
        Task task = createTaskTest();
        manager.creationTask(task);
        manager.deleteTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }


    @Test
    protected void shouldGetTaskById() {
        Task taskTest1 = createTaskTest();
        manager.creationTask(taskTest1);
        taskTest1.setId(1);
        Task taskTest2 = createTaskTest();
        manager.creationTask(taskTest2);
        taskTest2.setId(1);
        assertNotNull(manager.getTaskById(1));
        assertEquals(manager.getTaskById(1), taskTest2);
    }

    @Test
    protected void shouldCreationTask() {
        Task task = createTaskTest();
        manager.creationTask(task);
        final List<Task> tasks = manager.getTasks();
        final List<Task> list = List.of(task);
        assertNotNull(task.getStatus());
        assertEquals(list, tasks);
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    protected void shouldDoNotUpdateTaskIfTaskEqualsNull() {
        Task task = createTaskTest();
        manager.creationTask(task);
        manager.updateTask(null);
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    protected void shouldUpdateStatusTaskInProgress() {
        Task task = createTaskTest();
        manager.creationTask(task);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus());
    }

    @Test
    protected void shouldUpdateStatusTaskInDone() {
        Task task = createTaskTest();
        manager.creationTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, manager.getTaskById(task.getId()).getStatus());
    }

    @Test
    protected void shouldDeleteTaskById() {
        Task task = createTaskTest();
        manager.creationTask(task);
        manager.deleteTaskById(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    public void shouldNotDeleteTaskIfNotTrueId() {
        Task taskTest = createTaskTest();
        manager.creationTask(taskTest);
        manager.deleteTaskById(100000);
        final List<Task> list = List.of(taskTest);
        assertEquals(list, manager.getTasks());
    }

    /**
     * Epics ↓ ↓ ↓
     */

    @Test
    protected void shouldGetAllEpics() {
        Epic epicNew = new Epic("EPIC_TITLE_NEW", "EPIC_DESCRIPTION_NEW", Status.NEW,
                LocalDateTime.of(2020, Month.JANUARY, 5, 6, 7),
                Duration.ofMinutes(40));
        Epic epicInProgress = new Epic("EPIC_TITLE_IN_PROGRESS", "EPIC_DESCRIPTION_IN_PROGRESS",
                Status.IN_PROGRESS, LocalDateTime.of(2021, Month.MARCH, 7, 8, 9),
                Duration.ofMinutes(40));
        Epic epicDone = new Epic("EPIC_TITLE_DONE", "EPIC_DESCRIPTION_DONE",
                Status.DONE, LocalDateTime.of(2022, Month.APRIL, 9, 10, 11),
                Duration.ofMinutes(40));
        epicNew.setId(1);
        epicInProgress.setId(2);
        epicDone.setId(3);

        manager.creationTask(epicNew);
        manager.creationTask(epicInProgress);
        manager.creationTask(epicDone);

        List<Task> allTasks;
        allTasks = List.copyOf(manager.getTasks());

        List<Task> allTasksTest = List.of(epicNew, epicInProgress, epicDone);

        assertNotNull(allTasks);
        assertEquals(allTasksTest.size(), allTasks.size());

        for (int i = 0; i < allTasks.size(); i++) {
            Task epic = allTasks.get(i);
            Task epicTest = allTasksTest.get(i);
            assertEquals(epicTest, epic);

        }
    }

    @Test
    protected void shouldDeleteAllEpics() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        manager.deleteEpics();
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    protected void getEpicById() {
        Epic epicTest1 = createEpicTest();
        manager.creationEpic(epicTest1);
        epicTest1.setId(1);
        Epic epicTest2 = createEpicTest();
        manager.creationEpic(epicTest2);
        epicTest2.setId(2);
        assertNotNull(manager.getEpicById(1));
        assertEquals(manager.getEpicById(2), epicTest2);
    }

    @Test
    protected void shouldCreationEpic() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        final List<Epic> epics = manager.getEpics();
        final List<Epic> list = List.of(epic);
        assertNotNull(epic.getStatus());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubtasksId());
        assertEquals(list, epics);
    }

    @Test
    protected void shouldReturnNullWhenCreateEpicEqualsNull() {
        Epic epic = createEpicTest();
        manager.creationTask(epic);
        manager.updateTask(null);
        assertEquals(epic, manager.getTaskById(epic.getId()));
    }

    @Test
    protected void shouldUpdateStatusEpicInProgress() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    protected void shouldUpdateEpicStatusInDone() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    protected void shouldDeleteEpicById() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        System.out.println(manager.getEpics());
        manager.deleteEpicById(epic.getId());
        System.out.println(epic.getId());
        System.out.println(manager.getEpics());
        System.out.println();
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    protected void shouldNotDeleteEpicIfNotTrueId() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        manager.deleteEpicById(100000);
        final List<Epic> list = List.of(epic);
        assertEquals(list, manager.getEpics());
    }

    /**
     * Subtasks ↓ ↓ ↓
     */

    @Test
    protected void shouldGetSubtaskByEpicId() {
    }

    @Test
    protected void shouldGetAllSubtasks() {
        Subtask subtaskNew = new Subtask("SUBTASK_TITLE_NEW", "SUBTASK_DESCRIPTION_NEW",
                Status.NEW, 1, LocalDateTime.of(2020, Month.JANUARY, 5, 6, 7),
                Duration.ofMinutes(40));
        Subtask subtaskInProgress = new Subtask("SUBTASK_TITLE_IN_PROGRESS", "SUBTASK_DESCRIPTION_IN_PROGRESS",
                Status.IN_PROGRESS, 1, LocalDateTime.of(2021, Month.MARCH, 7, 8, 9),
                Duration.ofMinutes(40));
        Subtask subtaskDone = new Subtask("SUBTASK_TITLE_DONE", "SUBTASK_DESCRIPTION_DONE",
                Status.DONE, 1, LocalDateTime.of(2022, Month.APRIL, 9, 10, 11),
                Duration.ofMinutes(40));

        manager.creationTask(subtaskNew);
        manager.creationTask(subtaskInProgress);
        manager.creationTask(subtaskDone);

        List<Task> allTasks;
        allTasks = List.copyOf(manager.getTasks());

        List<Task> allTasksTest = List.of(subtaskNew, subtaskInProgress, subtaskDone);

        assertNotNull(allTasks);
        assertEquals(allTasksTest.size(), allTasks.size());

        for (int i = 0; i < allTasks.size(); i++) {
            Task epic = allTasks.get(i);
            Task epicTest = allTasksTest.get(i);
            assertEquals(epicTest, epic);
        }
    }

    @Test
    protected void shouldDeleteAllSubtasks() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        Subtask subtask = createSubtaskTest();
        manager.creationSubtask(subtask);
        manager.deleteSubtasks();
        assertTrue(epic.getSubtasksId().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }


    @Test
    protected void shouldCreationSubtask() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        Subtask subtask = createSubtaskTest();
        manager.creationSubtask(subtask);
        final List<Subtask> subtasks = manager.getSubtasks();
        final List<Subtask> listSub = List.of(subtask);
        final List<Integer> listSubID = List.of(subtask.getId());
        assertNotNull(subtask.getStatus());
        assertEquals(epic.getId(), subtask.getEpicId());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(listSub, subtasks);
        assertEquals(listSubID, epic.getSubtasksId());
    }

    @Test
    protected void shouldNotDeleteSubtaskIfNotTrueId() {
        Epic epic = createEpicTest();
        manager.creationEpic(epic);
        Subtask subtask = createSubtaskTest();
        manager.creationSubtask(subtask);
        System.out.println(subtask);
        System.out.println(manager.getSubtasks());
        manager.deleteSubtaskById(100000);
        final List<Subtask> listSub = List.of(subtask);
        final List<Integer> listSubID = List.of(subtask.getId());
        assertEquals(listSub, manager.getSubtasks());
        assertEquals(listSubID, manager.getEpicById(epic.getId()).getSubtasksId());
    }

    @Test
    protected void shouldReturnNullWhenCreateSubtaskEqualsNull() {
        Subtask subtask = manager.creationSubtask(null);
        assertNull(subtask);
    }


    @Test
    protected void shouldDeleteSubtasksById() {
        Subtask subtask = createSubtaskTest();
        manager.creationSubtask(subtask);
        manager.deleteEpicById(subtask.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getSubtasks());
    }

    @Test
    protected void shouldReturnEmptyHistory() {
        assertEquals(Collections.EMPTY_LIST, manager.history());
    }

    @Test
    protected void shouldReturnEmptyHistoryIfTasksIsEmpty() {
        manager.getTaskById(666);
        manager.getSubtaskById(333);
        manager.getEpicById(777);
        assertTrue(manager.history().isEmpty());
    }

    @Test
    public void shouldReturnHistoryWithTasks() {
        Task taskDeleteKnow = createTaskTest();
        manager.creationTask(taskDeleteKnow);
        manager.deleteTasks();
        assertEquals(Collections.EMPTY_LIST, manager.history());
        Task taskTest = createTaskTest();
        Epic epicTest = createEpicTest();
        manager.creationTask(taskTest);
        manager.creationEpic(epicTest);
        manager.getTaskById(taskTest.getId());
        manager.getEpicById(epicTest.getId());
        List<Task> list = manager.history();
        assertEquals(2, list.size());
    }
}