package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        this.generatorId = generatorId;
    }

    /**
     * Tasks
     */
    @Override
    // Получение списка всех задач
    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        for (Integer task : tasks.keySet()) {
            list.add(tasks.get(task));
        }
        return list;
    }

    @Override
    // Удаление всех задач
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    // Получение задачи по индетификатору
    public Task getTask(int id) {
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
    public void removeTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    /**
     * Epics
     */
    @Override
    // Получение списка всех Эпиков
    public List<Epic> getEpics() {
        List<Epic> list = new ArrayList<>();
        for (Integer task : epics.keySet()) {
            list.add(epics.get(task));
        }
        return list;
    }

    @Override
    // Удаление всех Эпиков
    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    // Получение Эпиков по индетификатору
    public Epic getEpic(int id) {
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
        Epic oldEpic = epics.get(epic.getId());
        for (Integer idSubtask : oldEpic.getSubtasksId()) {
            epics.remove(idSubtask);
        }
    }

    @Override
    // Удаление Эпика по индетификатору
    public void removeEpic(int id) {
        for (Integer key : subtasks.keySet()) {
            if (Objects.equals(subtasks.get(key).getEpicId(), id)) {
                subtasks.remove(key);
                if (subtasks.size() <= 1) {
                    subtasks.clear();
                    break;
                }
            }
        }
        epics.remove(id);
    }

    private void updateEpicStatus(int epicId) {

        int NewCount = 0;
        int DoneCount = 0;
        int subtaskCount = epics.get(epicId).getSubtasksId().size();

        for (int subtaskId : epics.get(epicId).getSubtasksId()) {
            if (subtasks.get(subtaskId).getStatus() == NEW) {
                NewCount++;
            } else if (subtasks.get(subtaskId).getStatus() == DONE) {
                DoneCount++;
            }
        }
        if (NewCount == subtaskCount || subtaskCount == 0) {
            Epic epic = epics.get(epicId);
            epic.setStatus(NEW);
            epics.put(epicId, epic);
        } else if (DoneCount == subtaskCount) {
            Epic epic = epics.get(epicId);
            epic.setStatus(DONE);
            epics.put(epicId, epic);
        } else {
            Epic epic = epics.get(epicId);
            epic.setStatus(IN_PROGRESS);
            epics.put(epicId, epic);
        }
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
        List<Subtask> list = new ArrayList<>();
        for (Integer task : subtasks.keySet()) {
            list.add(subtasks.get(task));
        }
        return list;
    }

    @Override
    // Удаление всех Подзадач
    public void removeSubtasks() {
        subtasks.clear();
    }

    @Override
    // Получение Подзадач по индетификатору
    public Subtask getSubtask(int id) {
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
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    // Удаление Подзадачи по индетификатору
    public void removeSubtask(int id) {
        subtasks.remove(id);
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

    public int getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(int generatorId) {
        this.generatorId = generatorId;
    }
}