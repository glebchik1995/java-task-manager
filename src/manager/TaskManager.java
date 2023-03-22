package manager;

import task.Task;
import task.Subtask;
import task.Epic;

import java.util.List;

public interface TaskManager {

    /**
     * HistoryTask
     */
    List<Task> history();

    /**
     * Tasks
     */

    List<Task> getTasks();

    void deleteTasks();

    Task getTaskById(int id);

    void creationTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    /**
     * Epics
     */

    List<Epic> getEpics();

    void deleteEpics();

    Epic getEpicById(int id);

    void creationEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    void updateStatusEpic(Epic epic);

    void updateTimeEpic(Epic epic);

    /**
     * Subtasks
     */

    List<Subtask> getSubtaskByEpicId(int id);

    List<Subtask> getSubtasks();

    void deleteSubtasks();

    Subtask getSubtaskById(int id);

    Subtask creationSubtask(Subtask subtask);

//    Map<Integer, Epic> getEpicsMap();
//
//    Map<Integer, Task> getTasksMap();
//
//    Map<Integer, Subtask> getSubtasksMap();

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    void timeReconciliation(Task task);

    List<Task> getPrioritizedTasks();
}

