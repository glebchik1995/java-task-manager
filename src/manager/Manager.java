package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import model.StatusEnum;
import model.Task;
import model.Subtask;
import model.Epic;

import static model.StatusEnum.*;

public class Manager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private int generatorId;

    public Manager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.generatorId = 1;
    }

    /**
     * Tasks
     */
    @Override
    // Получение списка всех задач
    public List<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    // Удаление всех задач
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    // Получение задачи по индетификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    // Создание Задачи
    public void creationTask(Task task) {
        task.setId(++generatorId);
        tasks.put(task.getId(), task);
    }

    @Override
    // Обновление Задачи
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    // Удаление задачи по индетификатору
    public void deleteTask(int id) {
        if (this.tasks.containsKey(id)) {
            tasks.remove(id);
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
        epics.clear();
        subtasks.clear();
    }

    @Override
    // Получение Эпиков по индетификатору
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    @Override
    //Создание Эпика
    public int creationEpic(Epic epic) {
        epic.setId(++generatorId);
        epic.setStatus(NEW);
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
        for (Integer subtask : epic.getSubtasksId()) {
            subtasks.remove(subtask);
        }
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subs = epic.getSubtasksId();
        if (subs.isEmpty()) {
            epic.setStatus(NEW);
            return;
        }
        StatusEnum status = null;
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }
            if (status == subtask.getStatus()
                    && status != IN_PROGRESS) {
                continue;
            }
            epic.setStatus(IN_PROGRESS);
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
        List<Subtask> list = new ArrayList<>();
        for (Integer currentSubtask : epics.get(id).getSubtasksId()) {
            list.add(subtasks.get(currentSubtask));
        }
        return list;
    }

    @Override
    // Получение списка всех Подзадач
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
//    // Удаление всех подзадач
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            updateEpicStatus(epic);
        }
    }

    @Override
    // Получение Подзадач по индетификатору
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    @Override
    // Создание Подзадачи
    public int creationSubtask(Subtask subtask) {
        subtask.setId(++generatorId);
        subtasks.put(subtask.getId(), subtask);
        List<Integer> subtaskList = updateSubtasks(epics.get(subtask.getEpicId()));
        Epic epic = epics.get(subtask.getEpicId());
        subtaskList.add(subtask.getId());
        epic.setSubtasksId(subtaskList);
        epics.put(subtask.getEpicId(), epic);
        return subtask.getId();
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