package manager;

import task.Epic;
import enumTask.Status;
import task.Subtask;
import task.Task;
import enumTask.Types;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    Map<Integer, Task> taskMap = new HashMap<>();
    Map<Integer, Task> epicMap = new HashMap<>();
    Map<Integer, Task> subtasksMap = new HashMap<>();
    List<Integer> historyList = new ArrayList<>();

    public FileBackedTasksManager(File file) throws ManagerSaveException {
        super(Managers.getDefaultHistory());
        this.file = file;
        file = new File("sprint7.csv");
        if (!file.exists()) {
            try {
                Files.createFile(Paths.get(String.valueOf(file)));
            } catch (IOException exception) {
                throw new ManagerSaveException("Файл не удалось создать.");
            }
        }
    }

    public void save() throws ManagerSaveException {

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write("id,type,name,status,description,epic\n");

            Map<Integer, String> allTasks = new HashMap<>();

            Map<Integer, Task> tasks = getTasksMap();

            Map<Integer, Epic> epics = getEpicsMap();

            Map<Integer, Subtask> subtasks = getSubtasksMap();

            for (Integer id : tasks.keySet()) {
                allTasks.put(id, tasks.get(id).toString());
            }

            for (Integer id : epics.keySet()) {
                allTasks.put(id, epics.get(id).toString());
            }

            for (Integer id : subtasks.keySet()) {
                allTasks.put(id, subtasks.get(id).toString());
            }

            for (String value : allTasks.values()) {
                fileWriter.append(value).append("\n");
            }
            fileWriter.append("\n");
            fileWriter.append(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось внести запись в файл.");
        }
    }

    /**
     * // Создание задачи из строки
     */

    private static Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        Types type = Types.valueOf(values[1]);
        String title = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        LocalDateTime startTime = LocalDateTime.parse(values[5].trim(), DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        Duration duration = Duration.ofMinutes(Long.parseLong(values[7]));

        if (type == Types.TASK) {
            return new Task(id, title, status, description, startTime, duration);
        }
        if (type == Types.EPIC) {
            return new Epic(id, title, status, description, startTime, duration);
        }
        if (type == Types.SUBTASK) {
            int epicId = Integer.parseInt(values[8]);
            return new Subtask(id, title, status, description, startTime, duration, epicId);
        } else
            return null;
    }

    private static String historyToString(HistoryManager manager) {
        List<String> list = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            list.add(String.valueOf(task.getId()));
        }
        return String.join(",", list);
    }

    private static List<Integer> historyFromString(String value) {
        final String[] arrayIds = value.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String str : arrayIds) {
            historyList.add(Integer.valueOf(str));
        }
        return historyList;
    }

    /**
     * // Восстановление данных менеджера из файла
     */
    public static void loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String stringsFromFile;

        try {
            stringsFromFile = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочесть файл.");
        }
        String[] values = stringsFromFile.split("\n");
        for (int i = 1; i < values.length - 2; i++) {
            Task task = fromString(values[i]);
            if (task != null) {
                if (task.getType() == Types.TASK) {
                    fileBackedTasksManager.taskMap.put(i, task);
                } else if (task.getType() == Types.EPIC) {
                    fileBackedTasksManager.epicMap.put(i, task);
                } else if (task.getType() == Types.SUBTASK) {
                    fileBackedTasksManager.subtasksMap.put(i, task);
                }
            }
        }

        if (values[values.length - 1].isEmpty()) {
            List<Integer> historyById = historyFromString(values[values.length - 1]);
            if (!historyById.isEmpty()) {
                fileBackedTasksManager.historyList.addAll(historyById);
            }
        }

    }

    @Override
    public List<Task> history() {
        return super.history();
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

}
