import enumTask.Status;
import http.KVServer;
import manager.HttpTaskManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        TaskManager manager = Managers.getDefault();

        manager.creationTask(
                new Task("������� � �������", "��������� � 2 ���.���.", Status.NEW,
                        LocalDateTime.of(2019, Month.APRIL, 10, 15, 0), Duration.ofMinutes(10)));
        manager.creationTask(
                new Task("����������� � ������", "�������� ������ ����", Status.IN_PROGRESS,
                        LocalDateTime.of(2021, Month.MARCH, 10, 15, 0), Duration.ofMinutes(20)));
        manager.creationEpic(
                new Epic("������� 7-� ������ � �.����������", "����� ����� �����!", Status.NEW,
                        LocalDateTime.of(2023, Month.APRIL, 10, 15, 0), Duration.ofMinutes(30)));
        manager.creationEpic(
                new Epic("��������", "��������� �� �����", Status.NEW,
                        LocalDateTime.of(2022, Month.APRIL, 10, 15, 0), Duration.ofMinutes(40)));

        manager.creationSubtask(new Subtask("����������� � ������������ ��������",
                "��������� ������", Status.NEW, 3, LocalDateTime.now(), Duration.ofMinutes(50)));
        manager.creationSubtask(
                new Subtask("�������� ��������� �� �����", "�������� commit", Status.NEW,
                        3, LocalDateTime.of(2022, Month.MARCH, 13, 22, 0), Duration.ofMinutes(60)));
        manager.creationSubtask(
                new Subtask("������������ ����� �������", "������������� �����", Status.NEW,
                        4, LocalDateTime.of(2022, Month.MAY, 2, 15, 0), Duration.ofMinutes(70)));

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getSubtaskById(5);


//        FileBackedTasksManager.loadFromFile(new File("practicum.csv"));
        System.out.println();
        System.out.println("������ ���� �����: ");
        List<Task> tasks = manager.getTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }

        System.out.println("������ ���� ������: ");
        List<Epic> epics = manager.getEpics();
        for (Epic epic : epics) {
            System.out.println(epic);
        }

        System.out.println("������ ���� ��������: ");
        List<Subtask> subtasks = manager.getSubtasks();
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }

        System.out.println("������� ����� : " + manager.history());


        HttpTaskManager httpTaskManager = new HttpTaskManager(KVServer.PORT);
        httpTaskManager.load();
    }
}
