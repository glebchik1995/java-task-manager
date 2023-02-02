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

        Epic epic1 = new Epic("Сделать 5-й проект в Я.Практикуме", "Сдать любой ценой!");
        manager.creationEpic(epic1);
        Subtask subtask1 = new Subtask("Разобраться в комментариях ревьюера",
                "Исправить ошибки", StatusEnum.NEW, epic1.getId());
        manager.creationSubtask(subtask1);
        Subtask subtask2 = new Subtask("Повторно отправить на ревью", "Добавить commit",
                StatusEnum.NEW, epic1.getId());
        manager.creationSubtask(subtask2);
        Subtask subtask3 = new Subtask("Порадоваться сдаче проекта", "Отпраздновать сдачу",
                StatusEnum.NEW, epic1.getId());
        manager.creationSubtask(subtask3);

        Epic epic2 = new Epic("Каникулы", "отдохнуть от учебы");
        manager.creationEpic(epic2);

        System.out.println();

        System.out.println("Просматриваем задачи....хм...");
        manager.getEpicById(epic1.getId());
        System.out.println("Показать историю : " + manager.history());
        manager.getSubtaskById(subtask1.getId());
        System.out.println("Показать историю : " + manager.history());
        manager.getSubtaskById(subtask3.getId());
        System.out.println("Показать историю : " + manager.history());
        manager.getSubtaskById(subtask2.getId());
        System.out.println("Показать историю : " + manager.history());
        manager.getSubtaskById(subtask2.getId());
        System.out.println("Показать историю : " + manager.history());
        manager.getEpicById(epic2.getId());
        System.out.println("Показать историю : " + manager.history());
        manager.getSubtaskById(subtask1.getId());
        System.out.println("Показать историю : " + manager.history());
        manager.getSubtaskById(subtask3.getId());
        System.out.println("Показать историю : " + manager.history());
        manager.getEpicById(epic2.getId());
        System.out.println("Показать историю : " + manager.history());

        System.out.println();

        System.out.println("Список эпиков до удаления эпика: " +
                manager.getEpics().toString());

        System.out.println("Список подзадач до удаления эпика: " +
                manager.getSubtasks().toString());

        System.out.println("Показать историю до удаления: " + manager.history());

        System.out.println("Удаляем Эпик с тремя подзадачами");

        manager.deleteEpicById(epic1.getId());

        System.out.println("Список эпиков после удаления: " +
                manager.getEpics().toString());

        System.out.println("Список подзадач после удаления эпика: " +
                manager.getSubtasks().toString());

        System.out.println("Показать историю : " + manager.history());

        System.out.println();

    }
}
