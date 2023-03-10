package Main;

import manager.FileBackedTasksManager;
import EnumTasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.Epic;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager manager = new FileBackedTasksManager(new File("sprint7.csv"));

        manager.creationTask(
                new Task("Сходить в магазин", "Уложиться в 2 тыс.руб.", Status.NEW,
                        LocalDateTime.of(2019, Month.APRIL, 10, 15, 0), Duration.ofMinutes(10)));
        manager.creationTask(
                new Task("Разобраться в чулане", "Выкинуть старые вещи", Status.IN_PROGRESS,
                        LocalDateTime.of(2021, Month.MARCH, 10, 15, 0), Duration.ofMinutes(20)));
        manager.creationEpic(
                new Epic("Сделать 7-й проект в Я.Практикуме", "Сдать любой ценой!", Status.NEW,
                        LocalDateTime.of(2023, Month.APRIL, 10, 15, 0), Duration.ofMinutes(30)));
        manager.creationEpic(
                new Epic("Каникулы", "Отдохнуть от учебы", Status.NEW,
                        LocalDateTime.of(2022, Month.APRIL, 10, 15, 0), Duration.ofMinutes(40)));

        manager.creationSubtask(new Subtask("Разобраться в комментариях ревьюера",
                "Исправить ошибки", Status.NEW, 3, LocalDateTime.now(), Duration.ofMinutes(50)));
        manager.creationSubtask(
                new Subtask("Повторно отправить на ревью", "Добавить commit", Status.NEW,
                        3, LocalDateTime.of(2022, Month.MARCH, 13, 22, 0), Duration.ofMinutes(60)));
        manager.creationSubtask(
                new Subtask("Порадоваться сдаче проекта", "Отпраздновать сдачу", Status.NEW,
                        4, LocalDateTime.of(2022, Month.MAY, 2, 15, 0), Duration.ofMinutes(70)));

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getSubtaskById(5);


        FileBackedTasksManager.loadFromFile(new File("sprint7.csv"));
        System.out.println();
        System.out.println("Список всех задач: ");
        List<Task> tasks = manager.getTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }

        System.out.println("Список всех эпиков: ");
        List<Epic> epics = manager.getEpics();
        for (Epic epic : epics) {
            System.out.println(epic);
        }

        System.out.println("Список всех подзадач: ");
        List<Subtask> subtasks = manager.getSubtasks();
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }

        System.out.println("История задач : " + manager.history()+ "\n");
    }
}
