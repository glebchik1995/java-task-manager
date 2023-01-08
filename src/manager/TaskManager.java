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

    void deleteTasks();

    Task getTaskById(int id);

    void creationTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    /**
     * Epics
     */

    List<Epic> getEpics();

    void deleteEpics();

    Epic getEpicById(int id);

    int creationEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    /**
     * Subtasks
     */

    List<Subtask> getSubtaskByEpicId(int id);

    List<Subtask> getSubtasks();

    void deleteSubtasks();

    Subtask getSubtaskById(int id);

    int creationSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);
    List<Integer> updateSubtasks(Epic epic);
}

