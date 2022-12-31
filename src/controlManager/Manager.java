package controlManager;

import java.util.*;

import model.Task;
import model.Subtask;
import model.Status;
import model.Epic;

public class Manager {
    HashMap<Integer, Task> tasksHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicsHashMap = new HashMap<>();
    HashMap<Integer, Subtask> subTasksHashMap = new HashMap<>();
    protected int idGenerator = 0;

    /**
     * Tasks
     */

    //Получение списка всех задач.
    public List<Task> getTasks() {
        return new ArrayList<>(tasksHashMap.values());
    }

    // Удаление всех задач.
    public void removeTasks() {
        if (!tasksHashMap.isEmpty()) {
            tasksHashMap.clear();
        } else
            System.out.println("Список задач пуст");
    }

    // Получение по идентификатору.
    public Task getTask(int id) {
        final Task task = tasksHashMap.get(id);
        return task;
    }

    // Создание. Сам объект должен передаваться в качестве параметра.
    public void creationTask(Task task) {
        task.setId(++idGenerator);
        tasksHashMap.put(task.getId(), task);
    }

    // Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        if (!tasksHashMap.containsKey(task.getId())) {
            return;
        }
        tasksHashMap.put(task.getId(), task);
    }

    // Удаление по идентификатору.
    public void removeTask(int id) {
        if (!tasksHashMap.isEmpty()) {
            tasksHashMap.remove(id);
        } else
            System.out.println("Список задач пуст");
    }

    /**
     * Epics
     */
    // Получение списка всех эпиков
    public List<Epic> getEpics() {
        return new ArrayList<>(epicsHashMap.values());
    }

    // Удаление всех эпиков.
    public void removeEpics() {
        if (!tasksHashMap.isEmpty()) {
            epicsHashMap.clear();
        } else
            System.out.println("Список эпиков пуст");
    }
    // Получение по идентификатору.
    public Epic getEpic(int id) {
        final Epic epic = epicsHashMap.get(id);
        return epic;
    }

    // Создание. Сам объект должен передаваться в качестве параметра.
    public void creationEpic(Epic epic) {
        epic.setId(++idGenerator);
        epic.setStatus(Status.NEW);
        epicsHashMap.put(epic.getId(), epic);
    }

    // Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateEpic(Epic epic) {
        final Epic epicSaved = epicsHashMap.get(epic.getId());
        if (epicSaved == null) {
            return;
        }
        epicSaved.setTitle(epic.getTitle());
        epicSaved.setDescription(epic.getDescription());
    }

    // Удаление по идентификатору.
    public void removeEpic(int id) {
        if (epicsHashMap.containsKey(id)) {
            for (Iterator<Map.Entry<Integer, Subtask>> iterator = subTasksHashMap.entrySet().iterator();
                 iterator.hasNext(); ) {
                Map.Entry<Integer, Subtask> entry = iterator.next();
                if (entry.getValue().getEpicId() == id) {
                    iterator.remove();
                }
            }
            epicsHashMap.remove(id);
        } else
            System.out.println("Эпик с таким идентификатором не найден");
    }

    /**
     * Subtasks
     */

    // Получение списка всех подклассов.
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subTasksHashMap.values());
    }

    // Удаление всех подзадач.
    public void removeSubtasks() {
        if (!tasksHashMap.isEmpty()) {
            subTasksHashMap.clear();
        } else
            System.out.println("Список подзадач пуст");
    }
    // Получение по идентификатору.
    public Subtask getSubtask(int id) {
        final Subtask subtask = subTasksHashMap.get(id);
        return subtask;
    }

    // Создание. Сам объект должен передаваться в качестве параметра.
    public void creationSubtask(Subtask subtask) {
        subtask.setId(++idGenerator);
        subTasksHashMap.put(subtask.getId(), subtask);
    }

    // Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateSubtask(Subtask subtask) {
        if (subTasksHashMap.containsKey(subtask.getId())) {
            subTasksHashMap.put(subtask.getId(), subtask);
        }
    }

    // Удалени подзадачи по идентификатору.
    public void removeSubtask(int id) {
        if (!subTasksHashMap.isEmpty()) {
            subTasksHashMap.remove(id);
        }
    }

    // Получение списка всех подзадач определённого эпика
    public List<Subtask> getSubtaskEpic(Epic epic) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();
        int epicId = epic.getId();
        for (Integer key : subTasksHashMap.keySet()) {
            if (subTasksHashMap.get(key).getEpicId() == epicId) {
                epicsSubtasks.add(subTasksHashMap.get(key));
            }
        }
        return epicsSubtasks;
    }

}