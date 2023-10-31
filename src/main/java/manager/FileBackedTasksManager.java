package manager;

import enums.Status;
import enums.Types;
import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;
    private static final String TITLE = "ID, TYPE, TITLE, STATUS, DESCRIPTION, START TIME, DURATION, EPIC_ID\n";

    public FileBackedTasksManager(File file) {
        this.file = file;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
                throw new ManagerSaveException("Файл не удалось создать.");
            }
        }

    }

    public FileBackedTasksManager() {
    }

    private Map<Integer, Task> getTasksOfAllTypes() {

        Map<Integer, Task> all = new HashMap<>();
        all.putAll(tasks);
        all.putAll(epics);
        all.putAll(subtasks);

        return all;
    }


    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            Map<Integer, Task> map = getTasksOfAllTypes();
            fileWriter.write(TITLE);
            for (Integer key : map.keySet()) {
                fileWriter.append(String.valueOf(map.get(key))).append("\n");
            }
            fileWriter.append("\n");
            fileWriter.append(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в файл!");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTaskManager = new FileBackedTasksManager(file);
        String stringsFromFile;
        try {
            stringsFromFile = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла!");
        }

        String[] values = stringsFromFile.split("\n");

        for (int i = 1; i < values.length - 2; i++) {

            Task task = fromString(values[i]);
            if (task instanceof Subtask) {
                fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
            } else if (task instanceof Epic) {
                fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
            } else if (task != null) {
                fileBackedTaskManager.tasks.put(task.getId(), task);
            }
        }
        if (values[values.length - 1].isEmpty()) {
            List<Integer> historyById = historyFromString(values[values.length - 1]);
            if (!historyById.isEmpty()) {
                for (Integer id : historyById) {
                    fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getTasksOfAllTypes().get(id));
                }
            }
        }
        return fileBackedTaskManager;
    }

    public static Task fromString(String value) {

        String[] values = value.split(",");
        int id = Integer.parseInt(values[0].trim());
        Types type = Types.valueOf(values[1]);
        String title = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        LocalDateTime startTime = LocalDateTime.parse(values[5], DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        Duration duration = Duration.ofMinutes(Long.parseLong(values[6]));

        return switch (type) {
            case TASK -> new Task(id, title, status, description, startTime, duration);
            case EPIC -> new Epic(id, title, status, description, startTime, duration);
            case SUBTASK -> {
                int epicId = Integer.parseInt(values[7]);
                yield new Subtask(id, title, status, description, startTime, duration, epicId);
            }
        };
    }

    private static String historyToString(HistoryManager manager) {
        List<String> historyList = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            historyList.add(String.valueOf(task.getId()));
        }
        return String.join(",", historyList);
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();

        if (!value.isEmpty()) {
            for (String id : value.split(",")) {
                history.add(Integer.parseInt(id));
            }
        }
        return history;
    }

    @Override
    public List<Task> findHistory() {
        return super.findHistory();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        save();
        return super.getTaskById(id);

    }

    @Override
    public void creationTask(Task task) {
        super.creationTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        save();
        return super.getEpicById(id);
    }

    @Override
    public void creationEpic(Epic epic) {
        save();
        super.creationEpic(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        save();
        return super.getSubtaskById(id);
    }

    @Override
    public Subtask creationSubtask(Subtask subtask) {
        super.creationSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    public static void main(String[] args) {

        TaskManager manager = Managers.getNewDefault(new File("src/main/java/CSV", "practicum.csv"));

        Task task1 = new Task(
                1,
                Types.TASK,
                "Task 1",
                Status.NEW,
                "Описание Task 1",
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        Task task2 = new Task(
                2,
                Types.TASK,
                "Task 2",
                Status.NEW,
                "Описание Task 2",
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        Task task3 = new Task(
                3,
                Types.TASK,
                "Task 3",
                Status.NEW,
                "Описание Task 3",
                LocalDateTime.now(),
                Duration.ofMinutes(45));

        manager.creationTask(task1);
        manager.creationTask(task2);
        manager.creationTask(task3);

        Epic epic1 = new Epic(
                1,
                Types.EPIC,
                "Epic 1",
                Status.NEW,
                "Описание Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Epic epic2 = new Epic(
                2,
                Types.EPIC,
                "Epic 2",
                Status.NEW,
                "Описание Epic 2",
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Epic epic3 = new Epic(
                3,
                Types.EPIC,
                "Epic 3",
                Status.NEW,
                "Описание Epic 3",
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        manager.creationEpic(epic1);
        manager.creationEpic(epic2);
        manager.creationEpic(epic3);

        Subtask subtaskOneEpicOne = new Subtask(
                1,
                Types.SUBTASK,
                "Subtask 1 Epic 1",
                Status.NEW,
                "Описание Subtask 1 Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(60),
                epic1.getId());
        Subtask subtaskTwoEpicOne = new Subtask(
                2,
                Types.SUBTASK,
                "Subtask 2 Epic 1",
                Status.NEW,
                "Описание Subtask 2 Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(120),
                epic1.getId());
        Subtask subtaskTreeEpicOne = new Subtask(
                3,
                Types.SUBTASK,
                "Subtask 3 Epic 1",
                Status.NEW,
                "Описание Subtask 3 Epic 1",
                LocalDateTime.now(),
                Duration.ofMinutes(60),
                epic1.getId());
        Subtask subtaskOneEpicTwo = new Subtask(
                1,
                Types.SUBTASK,
                "Subtask 1 Epic 2",
                Status.NEW,
                "Описание Subtask 1 Epic 2",
                LocalDateTime.now(),
                Duration.ofMinutes(300),
                epic2.getId());
        Subtask subtaskTwoEpicTwo = new Subtask(
                2,
                Types.SUBTASK,
                "Subtask 2 Epic 2",
                Status.NEW,
                "Описание Subtask 2 Epic 2",
                LocalDateTime.now(),
                Duration.ofMinutes(60),
                epic2.getId());

        manager.creationSubtask(subtaskOneEpicOne);
        manager.creationSubtask(subtaskTwoEpicOne);
        manager.creationSubtask(subtaskTreeEpicOne);
        manager.creationSubtask(subtaskOneEpicTwo);
        manager.creationSubtask(subtaskTwoEpicTwo);

        manager.getTaskById(0);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(9);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getEpicById(4);
        manager.getEpicById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(8);
        manager.getSubtaskById(9);
        manager.getSubtaskById(10);
        manager.getTaskById(0);
        manager.getSubtaskById(6);
        manager.getSubtaskById(8);
        manager.deleteTaskById(0);
        manager.deleteSubtaskById(10);

        printSortedTasks(manager.getPrioritizedTasks());
        printHistory(manager.findHistory());

        TaskManager newManager = Managers.getDefault(new File("src/main/java/CSV", "practicum.csv"));
        printHistory(newManager.findHistory());
    }

    public static void printHistory(List<Task> history) {
        System.out.print("История просмотров: \n");
        for (Task task : history) {
            System.out.print(task + "  " + "\n");
        }
        System.out.println();
    }

    public static void printSortedTasks(List<Task> tasksSet) {
        System.out.print("Отсортированные по дате и времени таски/подзадачи: \n");
        for (Task task : tasksSet) {
            System.out.print(
                    "[ID: " +
                            task.getId() +
                            ", Название: " +
                            task.getTitle() +
                            " " + task.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yy HH.mm")) + "]\n");
        }
        System.out.println();
    }
}





