import model.Status;
import model.Task;
import model.Subtask;
import model.Epic;
import controlManager.Manager;
public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();
        System.out.println("Менеджер задач: ");
        System.out.println();

        Task simpleTask1 = new Task("Встать на работу", "в 6 утра", Status.NEW);
        manager.creationTask(simpleTask1);

        Task simpleTask2 = new Task("Выходной день", "Спать до 12", Status.NEW);
        manager.creationTask(simpleTask2);

        System.out.println("Список задач до обновления: " + manager.getTasks().toString());

        simpleTask1.setStatus(Status.IN_PROGRESS);

        System.out.println();

        Task simpleTask3 = new Task("Новый год", "Хорошо отметить и лечь спать",
                Status.IN_PROGRESS);

        manager.updateTask(simpleTask3);

        System.out.println("Список задач после обновления: " + manager.getTasks().toString());

        System.out.println();

        Epic epic1 = new Epic("Отпраздновать Новый год", "В кругу семьи");
        manager.creationEpic(epic1);

        Subtask subtask1 = new Subtask("Нарядить елку", "Красными и синими шарами",
                Status.NEW, epic1.getId());
        manager.creationSubtask(subtask1);

        Subtask subtask2 = new Subtask("Накрыть на стол", "На 8 человек",
                Status.NEW, epic1.getId());
        manager.creationSubtask(subtask2);

        Epic epic2 = new Epic("Пойти в спорт зал", "Кардио тренировка");
        manager.creationEpic(epic2);


        Subtask subtask3 = new Subtask("Сделать присяд", "3 подхода по 7 раз",
                Status.DONE, epic2.getId());
        manager.creationSubtask(subtask3);

        manager.updateEpic(epic1);

        System.out.println("Статус epic1 " + epic1.getStatus());
        System.out.println("Статус epic2 " + epic2.getStatus());

        System.out.println("Список всех эпиков: " + manager.getEpics().toString());
        System.out.println("Список всех подзадач: " + manager.getSubtasks().toString());
        System.out.println("Подзадачи epic1: " + manager.getSubtaskEpic(epic1).toString());
        System.out.println("Подзадачи epic2: " + manager.getSubtaskEpic(epic2).toString());
        System.out.println();

        System.out.println("Удаляем задачу");
        manager.removeTask(simpleTask1.getId());
        System.out.println("Список задач после удаления задачи: " + manager.getTasks().toString());

        System.out.println();
        System.out.println("Удаляем Эпик");
        manager.removeEpic(epic2.getId());
        System.out.println("Список эпиков после удаления эпика: " + manager.getEpics().toString());
        System.out.println("Список подзадач после удаления эпика: " + manager.getSubtasks().toString());
        System.out.println();

        System.out.println("Удаляем подзадачу");
        manager.removeSubtask(subtask1.getId());
        System.out.println("Список подзадач после удаления: " + manager.getSubtasks().toString());

    }
}

