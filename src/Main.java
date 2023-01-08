import manager.Manager;
import model.StatusEnum;
import model.Subtask;
import model.Task;
import model.Epic;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        System.out.println("Менеджер задач: ");

        System.out.println();

        Task simpleTask1 = new Task("Встать на работу", "в 6 утра",
                StatusEnum.NEW);

        manager.creationTask(simpleTask1);

        Task simpleTask2 = new Task("Выходной день", "Спать до 12",
                StatusEnum.NEW);

        manager.creationTask(simpleTask2);

        System.out.println("Список задач до обновления: " +
                manager.getTasks().toString());

        simpleTask1.setStatus(StatusEnum.IN_PROGRESS);

        System.out.println();

        Task simpleTask3 = new Task(simpleTask1.getId(), "Новый год", "Хорошо отметить и лечь спать",
                StatusEnum.IN_PROGRESS);

        manager.updateTask(simpleTask3);

        System.out.println("Список задач после обновления: " +
                manager.getTasks().toString());

        System.out.println();

        Epic epic1 = new Epic("Отпраздновать Новый год", "В кругу семьи");

        manager.creationEpic(epic1);


        Subtask subtask1 = new Subtask("Нарядить елку", "Красными и синими шарами",
                StatusEnum.NEW, epic1.getId());

        manager.creationSubtask(subtask1);

        Subtask subtask2 = new Subtask("Накрыть на стол", "На 8 человек",
                StatusEnum.NEW, epic1.getId());

        manager.creationSubtask(subtask2);

        Epic epic2 = new Epic("Пойти в спорт зал", "Кардио тренировка");

        manager.creationEpic(epic2);

        Subtask subtask3 = new Subtask("Сделать присяд", "3 подхода по 7 раз",
                StatusEnum.DONE, epic2.getId());

        manager.creationSubtask(subtask3);

        manager.updateEpic(epic1);

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

        System.out.println("Список подзадач после удаления: " +
                manager.getSubtasks().toString());

        System.out.println();

        manager.deleteSubtasks();

        System.out.println("Список подзадач после удаления: " +
                manager.getSubtasks().toString());
    }
}
