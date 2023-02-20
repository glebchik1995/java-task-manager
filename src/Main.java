import manager.FileBackedTasksManager;
import model.Status;
import model.Subtask;
import model.Task;
import model.Epic;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager manager = new FileBackedTasksManager(new File("sprint6.csv"));

        manager.creationTask(
                new Task("Сходить в магазин", "Уложиться в 2 тыс.руб.", Status.NEW));
        manager.creationTask(
                new Task("Разобраться в чулане", "Выкинуть старые вещи", Status.NEW));
        manager.creationEpic(
                new Epic("Сделать 6-й проект в Я.Практикуме", "Сдать любой ценой!"));
        manager.creationEpic(
                new Epic("Каникулы", "Отдохнуть от учебы"));
        manager.creationSubtask(new Subtask("Разобраться в комментариях ревьюера",
                "Исправить ошибки", Status.NEW, 3));
        manager.creationSubtask(
                new Subtask("Повторно отправить на ревью", "Добавить commit", Status.NEW, 3));
        manager.creationSubtask(
                new Subtask("Порадоваться сдаче проекта", "Отпраздновать сдачу", Status.NEW, 4));

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getSubtaskById(5);


        FileBackedTasksManager.loadFromFile(new File("sprint6.csv"));

        System.out.println("Список всех задач: ");
        List<Task> tasks = manager.getTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Список всех эпиков: ");
        List<Epic> epics = manager.getEpics();
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        System.out.println("Список всех подзадач: ");
        List<Subtask> subtasks = manager.getSubtasks();
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }

        System.out.println("История задач : " + manager.history());
    }
}
