package manager;

import model.Task;
import model.Subtask;
import model.Epic;

import java.util.List;

public interface TaskManager {

    int generateId();

    /**
     * HistoryTask
     */
    List<Task> findHistory();

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

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    void timeReconciliation(Task task);

    List<Task> getPrioritizedTasks();
}

