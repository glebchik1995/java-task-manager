package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Status;
import model.Task;
import model.Subtask;
import model.Epic;


public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final Map<Integer, Epic> epics = new HashMap<>();

    @Override
    public Map<Integer, Task> getTasksMap() {
        return tasks;
    }

    @Override
    public Map<Integer, Epic> getEpicsMap() {
        return epics;
    }

    @Override
    public Map<Integer, Subtask> getSubtasksMap() {
        return subtasks;
    }

    public HistoryManager historyManager = Managers.getDefaultHistory();
    private int generatorId = 0;

    /**
     * Получение списка истории задач
     */
    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    /**
     * Получение списка всех задач
     */
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }


    /**
     * // Удаление всех задач
     */
    @Override
    public void deleteTasks() {
        for (Integer task : tasks.keySet()) {
            historyManager.remove(task);
        }
        tasks.clear();

    }

    /**
     * // Получение задачи по индетификатору
     */
    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    /**
     * // Создание Задачи
     */
    @Override
    public void creationTask(Task task) {
        task.setId(++generatorId);
        tasks.put(task.getId(), task);
    }

    /**
     * // Обновление Задачи
     */
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * // Удаление задачи по индетификатору
     */
    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    /**
     * Epics
     */
    @Override
    // Получение списка всех Эпиков
    public List<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    // Удаление всех Эпиков
    public void deleteEpics() {
        for (Integer epic : epics.keySet()) {
            historyManager.remove(epic);
        }
        for (Integer subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    // Получение Эпиков по индетификатору
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    //Создание Эпика
    public int creationEpic(Epic epic) {
        epic.setId(++generatorId);
        epic.setStatus(Status.NEW);
        List<Integer> list = updateSubtasks(epic);
        epic.setSubtasksId(list);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    // Обновление Эпика
    public void updateEpic(Epic epic) {

        final Epic epicSaved = epics.get(epic.getId());
        if (epicSaved == null) {
            return;
        }
        epicSaved.setTitle(epic.getTitle());
        epicSaved.setDescription(epic.getDescription());

    }

    @Override
    // Удаление Эпика по индетификатору
    public void deleteEpicById(int id) {
        final Epic epic = epics.remove(id);
        historyManager.remove(id);
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subs = epic.getSubtasksId();
        if (subs.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        Status status = null;
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }
            if (status == subtask.getStatus()
                    && status != Status.IN_PROGRESS) {
                continue;
            }
            epic.setStatus(Status.IN_PROGRESS);
            return;
        }
        epic.setStatus(status);
    }

    /**
     * Subtasks
     */
    @Override
    // Получение списка Подзадачи по индитификатору Эпика
    public List<Subtask> getSubtaskByEpicId(int id) {
        List<Subtask> subtask = new ArrayList<>();
        for (Integer currentSubtask : epics.get(id).getSubtasksId()) {
            subtask.add(subtasks.get(currentSubtask));
        }
        return subtask;
    }

    @Override
    // Получение списка всех Подзадач
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
//    // Удаление всех подзадач
    public void deleteSubtasks() {
        for (Integer subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            updateEpicStatus(epic);
        }
    }

    @Override
    // Получение Подзадач по индетификатору
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    // Создание Подзадачи

    public void creationSubtask(Subtask subtask) {
        subtask.setId(++generatorId);
        subtasks.put(subtask.getId(), subtask);
        List<Integer> subtaskList = updateSubtasks(epics.get(subtask.getEpicId()));
        Epic epic = epics.get(subtask.getEpicId());
        subtaskList.add(subtask.getId());
        epic.setSubtasksId(subtaskList);
        epics.put(subtask.getEpicId(), epic);

    }

    @Override
    //  Обновление Подзадачи
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateSubtasks(epic);
        updateEpicStatus(epic);
    }

    @Override
    // Удаление Подзадачи по индетификатору
    public void deleteSubtaskById(int id) {
        if (this.subtasks.containsKey(id)) {
            Subtask subtask = this.getSubtaskById(id);
            int ids = this.subtasks.get(id).getEpicId();
            Epic epic = this.getEpicById(ids);
            epic.getSubtasksId().remove(subtask);
            this.subtasks.remove(id);
            historyManager.remove(id);
        }

    }

    @Override
    //  Обновление подзадачи
    public List<Integer> updateSubtasks(Epic epic) {
        List<Integer> SubtaskIds = new ArrayList<>();
        for (Integer subtaskID : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subtaskID);
            SubtaskIds.add(subtask.getId());
        }
        epic.setSubtasksId(SubtaskIds);
        return epic.getSubtasksId();
    }

}