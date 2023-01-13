package manager;

import model.Task;
import model.Subtask;
import model.Epic;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    /**
     * HistoryTask
     */
    List<Task> history();

    /**
     * Tasks
     */

    HashMap<Integer, Task> getTasks();

    void deleteTasks();

    Task getTaskById(int id);

    void creationTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    /**
     * Epics
     */

    HashMap<Integer, Epic> getEpics();

    void deleteEpics();

    Epic getEpicById(int id);

    int creationEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    /**
     * Subtasks
     */

    List<Subtask> getSubtaskByEpicId(int id);

    HashMap<Integer, Subtask> getSubtasks();

    void deleteSubtasks();

    Subtask getSubtaskById(int id);

    int creationSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    List<Integer> updateSubtasks(Epic epic);
}

