package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        this.file = file;
        file = new File("sprint6.csv");
        if (!file.exists()) {
            try {
                Files.createFile(Paths.get(String.valueOf(file)));
            } catch (IOException exception) {
                throw new ManagerSaveException("Файл не удалось создать.");
            }
        }
    }

    private void save() throws ManagerSaveException {

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write("id,type,name,status,description,epic\n");

            Map<Integer, String> allTasks = new HashMap<>();

            Map<Integer, Task> tasks = getTasksMap();

            Map<Integer, Epic> epics = getEpicsMap();

            Map<Integer, Subtask> subtasks = getSubtasksMap();

            /**
             * // Сохраняем tasks в HashMap allTasks
             */

            for (Integer id : tasks.keySet()) {
                allTasks.put(id, tasks.get(id).toString());
            }
            /**
             * // Сохраняем epics в HashMap allTasks
             */

            for (Integer id : epics.keySet()) {
                allTasks.put(id, epics.get(id).toString());
            }
            /**
             * // Сохраняем subtasks в HashMap allTasks
             */

            for (Integer id : subtasks.keySet()) {
                allTasks.put(id, subtasks.get(id).toString());
            }
            /**
             * // Сохраняем значения allTasks в fileWriter
             */

            for (String value : allTasks.values()) {
                fileWriter.append(value).append("\n");
            }
            fileWriter.append("\n");
            fileWriter.append(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось внести запись в файл.");
        }
    }

    /**
     * // Создание задачи из строки
     */

    private static Task fromString(String values) {
        String[] value = values.split(",", 6);
        int id = Integer.parseInt(value[0]);
        Types type = Types.valueOf(value[1]);
        String title = value[2];
        Status status = Status.valueOf(value[3]);
        String description = value[4];

        if (type == Types.TASK) {
            return new Task(id, title, status, description);
        }
        if (type == Types.EPIC) {
            return new Epic(id, title, status, description);
        }
        if (type == Types.SUBTASK) {
            int epicId = Integer.parseInt(value[5]);
            return new Subtask(id, title, status, description, epicId);
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
        List<Integer> historyIds = historyFromString(values[values.length - 1]);
        fileBackedTasksManager.historyList.addAll(historyIds);

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
    public void deleteTask(int id) {
        super.deleteTask(id);
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
    public int creationEpic(Epic epic) {

        save();
        return super.creationEpic(epic);
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
    public void creationSubtask(Subtask subtask) {
        super.creationSubtask(subtask);
        save();
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
