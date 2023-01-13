import manager.Managers;
import manager.TaskManager;
import model.StatusEnum;
import model.Subtask;
import model.Task;
import model.Epic;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        System.out.println("Менеджер задач: ");

        System.out.println();

        Task simpleTask1 = new Task("Сделать 1-й проект в Я.Практикуме",
                "Сдать любой ценой!", StatusEnum.NEW);
        Task simpleTask2 = new Task("Сделать 2-й проект в Я.Практикуме",
                "Сдать любой ценой!", StatusEnum.NEW);
        Task simpleTask3 = new Task("Сделать 3-й проект в Я.Практикуме",
                "Сдать любой ценой!", StatusEnum.NEW);

        manager.creationTask(simpleTask1);
        manager.creationTask(simpleTask2);
        manager.creationTask(simpleTask3);

        System.out.println("Список задач до обновления: " +
                manager.getTasks().toString());
        System.out.println();

        Task simpleTask4 = new Task(simpleTask1.getId(),"Сделать 4-й проект в Я.Практикуме",
                "Сдать любой ценой!", StatusEnum.DONE);

        manager.updateTask(simpleTask4);
        System.out.println("Список задач после обновления: " +
                manager.getTasks().toString());
        System.out.println();

        Epic epic1 = new Epic("Сделать 5-й проект в Я.Практикуме", "Сдать любой ценой!");
        manager.creationEpic(epic1);
        Subtask subtask1 = new Subtask("Разобраться в комментариях ревьюера",
                "Исправить ошибки", StatusEnum.NEW, epic1.getId());
        manager.creationSubtask(subtask1);
        Subtask subtask2 = new Subtask("Повторно отправить на ревью", "Добавить commit",
                StatusEnum.NEW, epic1.getId());
        manager.creationSubtask(subtask2);

        Epic epic2 = new Epic("Каникулы", "отдохнуть от учебы");
        manager.creationEpic(epic2);
        Subtask subtask3 = new Subtask("Посмотреть любимый сериал",
                "Дом дракона", StatusEnum.NEW, epic2.getId());
        manager.creationSubtask(subtask3);

        System.out.println("Статус epic1 " +
                epic1.getStatus());

        System.out.println("Статус epic2 " +
                epic2.getStatus());

        System.out.println();

        System.out.println("Список всех эпиков: " +
                manager.getEpics().toString());

        System.out.println("Список всех подзадач: " +
                manager.getSubtasks().toString());

        System.out.println();

        System.out.println("Подзадачи epic1: " +
                manager.getSubtaskByEpicId(epic1.getId()).toString());

        System.out.println("Подзадачи epic2: " +
                manager.getSubtaskByEpicId(epic2.getId()).toString());

        System.out.println();

        System.out.println("Просматриваем задачи....хм...");
        manager.getTaskById(simpleTask1.getId());
        manager.getTaskById(simpleTask2.getId());
        manager.getTaskById(simpleTask3.getId());
        manager.getTaskById(simpleTask4.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubtaskById(subtask3.getId());
        System.out.println();

        System.out.println("Показать историю : " + manager.history());
        System.out.println();

        System.out.println("Удаляем задачу");

        manager.deleteTask(simpleTask3.getId());

        System.out.println("Список задач после удаления задачи: " +
                manager.getTasks().toString());

        System.out.println();

        System.out.println("Удаляем Эпик");

        manager.deleteEpicById(epic2.getId());

        System.out.println("Список эпиков после удаления эпика: " +
                manager.getEpics().toString());

        System.out.println("Список подзадач после удаления эпика: " +
                manager.getSubtasks().toString());

        System.out.println();

        System.out.println("Удаляем подзадачу");

        manager.deleteSubtaskById(subtask1.getId());

        System.out.println("Список подзадач после удаления подзадачи: " +
                manager.getSubtasks().toString());

        System.out.println();

        System.out.println("Удаляем все имеющиеся подзадачи");
        manager.deleteSubtasks();

        System.out.println("Список подзадач после удаления всех имеющихся подзадач: " +
                manager.getSubtasks().toString());

    }
}
