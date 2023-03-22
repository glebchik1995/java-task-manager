package manager;

import enumTask.Status;
import exception.ManagerValidateException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();

    protected Set<Task> prioritizedTasks = new TreeSet<>(new ComparatorManager());

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }


    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    protected HistoryManager historyManager;
    protected int counterId = 0;

    /*
      Tasks ↓ ↓ ↓
     */

    /**
     * 1. Получение списка всех задач ↓
     */
    @Override
    public List<Task> getTasks() {
        if (tasks.size() != 0) {
            return new ArrayList<>(tasks.values());
        } else {
            System.out.println("Список задач пуст");
            return Collections.emptyList();
        }
    }


    /**
     * // 2. Удаление всех задач ↓
     */
    @Override
    public void deleteTasks() {
        if (!tasks.isEmpty()) {
            for (Integer task : tasks.keySet()) {
                historyManager.remove(task);
            }
            tasks.clear();
            prioritizedTasks.clear();
        } else
            System.out.println("Удалять нечего список Задач и так пуст");

    }

    /**
     * // 3. Получение задачи по идентификатору ↓
     */
    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);

    }

    /**
     * // 4. Создание Задачи ↓
     */
    @Override
    public void creationTask(Task task) {
        if (task != null) {
            int id = ++counterId;
            task.setId(id);
            tasks.put(id, task);
            addNewPrioritizedTask(task);
        } else
            System.out.println("Задача не создана");
    }

    /**
     * // 5. Обновление Задачи ↓
     */
    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            addNewPrioritizedTask(task);
            tasks.put(task.getId(), task);
        } else
            System.out.println("Не удалось обновить задачу");
    }

    /**
     * // 6. Удаление задачи по идентификатору ↓
     */
    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.removeIf((Task task) ->
                    task.getId() == id
            );
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Задачи с таким id нет в списке");
        }
    }

    /*
      Epics ↓ ↓ ↓
     */

    /**
     * 7. Получение списка всех Эпиков ↓
     */
    @Override
    public List<Epic> getEpics() {
        if (!epics.isEmpty()) {
            return new ArrayList<>(epics.values());

        } else {
            System.out.println("Список задач пуст");
            return Collections.emptyList();
        }
    }

    /**
     * 8. Удаление всех Эпиков ↓
     */
    @Override
    public void deleteEpics() {
        if (!epics.isEmpty()) {
            for (Integer epic : epics.keySet()) {
                historyManager.remove(epic);
            }
            for (Integer subtask : subtasks.keySet()) {
                historyManager.remove(subtask);
            }
            epics.clear();
            subtasks.clear();
        } else
            System.out.println("Удалять нечего список Эпиков все равно пуст");
    }

    /**
     * 9. Получение Эпиков по идентификатору ↓
     */
    @Override
    public Epic getEpicById(int id) {
        final Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    /**
     * 10. Создание Эпика ↓
     */
    @Override
    public void creationEpic(Epic epic) {
        if (epic != null) {
            int id = ++counterId;
            epic.setId(id);
            epics.put(id, epic);
        } else
            System.out.println("Эпик не создан");
    }

    /**
     * 11. Обновление Эпика ↓
     */
    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateStatusEpic(epic);
            updateTimeEpic(epic);
        } else
            System.out.println("Не удалось обновить Эпик");
    }

    /**
     * 12. Удаление Эпика по идентификатору ↓
     */
    @Override
    public void deleteEpicById(int id) {
        final Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.remove(id);
            epics.remove(epic.getId());
            for (Integer subtaskId : epic.getSubtasksId()) {
                prioritizedTasks.removeIf(task -> task.getId() == subtaskId);
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
        } else {
            System.out.println("Эпика с таким id нет в списке");
        }
    }

    /**
     * 13. Обновление статуса у Эпика ↓
     */
    @Override
    public void updateStatusEpic(Epic epic) {
        List<Integer> subTasksId = epic.getSubtasksId();
        if (subTasksId.isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (int subtaskId : subTasksId) {
            if (subtasks.get(subtaskId).getStatus() == Status.NEW) {
                epic.setStatus(Status.NEW);
            }
        }
        for (int subtaskId : subTasksId) {
            if (subtasks.get(subtaskId).getStatus() == Status.DONE) {
                epic.setStatus(Status.DONE);
            }
        }
        epic.setStatus(Status.IN_PROGRESS);
    }

    /**
     * 14. Обновление времени у Эпика ↓
     */
    @Override
    public void updateTimeEpic(Epic epic) {
        List<Subtask> subtasks = getSubtaskByEpicId(epic.getId());
        LocalDateTime startTime = subtasks.get(0).getStartTime();
        LocalDateTime endTime = subtasks.get(0).getEndTime();

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        Duration duration = Duration.ofDays(endTime.getSecond() - startTime.getSecond());
        epic.setDuration(duration);
    }

    /*
      Subtasks ↓ ↓ ↓
     */

    /**
     * 15. Получение списка Подзадачи по идентификатору Эпика ↓
     */
    @Override
    public List<Subtask> getSubtaskByEpicId(int id) {
        if (epics.containsKey(id)) {
            List<Subtask> subtask = new ArrayList<>();
            for (Integer currentSubtask : epics.get(id).getSubtasksId()) {
                subtask.add(subtasks.get(currentSubtask));
            }
            return subtask;
        } else
            return Collections.emptyList();
    }

    /**
     * 16. Получение списка всех Подзадач ↓
     */
    @Override
    public List<Subtask> getSubtasks() {
        if (!subtasks.isEmpty()) {
            return new ArrayList<>(subtasks.values());
        } else {
            System.out.println("Список подзадач пуст");
            return Collections.emptyList();
        }
    }


    /**
     * 17. Удаление всех подзадач ↓
     */
    @Override
    public void deleteSubtasks() {
        if (!subtasks.isEmpty()) {
            for (Integer subtask : subtasks.keySet()) {
                historyManager.remove(subtask);
            }
            prioritizedTasks.clear();
            subtasks.clear();
            for (Epic epic : epics.values()) {
                epic.getSubtasksId().clear();
                updateStatusEpic(epic);
            }
        } else
            System.out.println("Удалять нечего список подзадач все равно пуст");
    }

    /**
     * 18. Получение Подзадач по идентификатору ↓
     */
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    /**
     * 19. Создание Подзадачи ↓
     */
    @Override
    public Subtask creationSubtask(Subtask subtask) {
        if (subtask != null) {
            int Id = ++counterId;
            subtask.setId(Id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                addNewPrioritizedTask(subtask);
                subtasks.put(Id, subtask);
                epic.setSubtasksId(Id);
                updateStatusEpic(epic);
                updateTimeEpic(epic);
            } else {
                System.out.println("Эпик не создан");
            }
        }
        return subtask;
    }

    /**
     * 20. Обновление Подзадачи ↓
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            addNewPrioritizedTask(subtask);
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateStatusEpic(epic);
            updateTimeEpic(epic);
        } else {
            System.out.println("Не удалось обновить Подзадачу");
        }
    }

    /**
     * 21. Удаление Подзадачи по идентификатору ↓
     */
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksId().remove((Integer) subtask.getId());
            updateStatusEpic(epic);
            updateTimeEpic(epic);
            prioritizedTasks.remove(subtask);
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Подзадачи с таким id нет в списке");
        }

    }

    /**
     * 22. Получение списка истории задач ↓ ↓ ↓
     */
    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    @Override
    public void timeReconciliation(Task task) throws ManagerValidateException {
        LocalDateTime startTime = task.getStartTime();

        for (Task prioritizedTask : prioritizedTasks) {
            if (startTime == null && prioritizedTask.getStartTime() == null) {
                return;
            }
            boolean conditionStartTime = task.getStartTime().isEqual(prioritizedTask.getStartTime());
            boolean conditionEndTime = task.getEndTime().isEqual(prioritizedTask.getStartTime());
            if (conditionStartTime && conditionEndTime) {
                throw new RuntimeException("Ошибка пересечения задач по времени");
            }
        }
    }

    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        timeReconciliation(task);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}