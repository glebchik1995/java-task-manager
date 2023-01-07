package manager;

import model.Task;
import model.Subtask;
import model.Epic;

import java.util.List;

public interface TaskManager {

    /**
     * Tasks
     */

    List<Task> getTasks();

    void removeTasks();

    Task getTask(int id);

    void creationTask(Task task);

    void updateTask(Task task);

    void removeTask(int id);

    /**
     * Epics
     */

    List<Epic> getEpics();

    void removeEpics();

    Epic getEpic(int id);

    int creationEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpic(int id);

    /**
     * Subtasks
     */

    List<Subtask> getSubtaskByEpicId(int id);

    List<Subtask> getSubtasks();

    void removeSubtasks();

    Subtask getSubtask(int id);

    int creationSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void removeSubtask(int id);
    List<Integer> updateSubtasks(Epic epic);
}

