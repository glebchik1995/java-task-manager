package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> nodeById = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    /**
     * // Создание класса для узла списка
     */
    public static class Node<T> {
        private T data;
        private Node<T> next;
        private Node<T> prev;

        Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * // Добавление задачи в историю просмотра задач
     */
    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkTail(task);
        }
    }

    /**
     * // Удаление задачи из просмотра истории задач
     */
    @Override
    public void remove(int id) {
        removeNode(nodeById.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    /**
     * // Добавление задачи в конец списка
     */
    private void linkTail(Task data) {
        final Node<Task> lastTail = tail;
        final Node<Task> newNode = new Node<>(lastTail, data, null);
        nodeById.put(data.getId(), newNode);
        tail = newNode;
        if (lastTail != null)
            lastTail.next = newNode;
        else
            head = newNode;
    }

    /**
     * // Добавление задач из метода linkTail в отдельный список
     */
    private List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            list.add(node.data);
            node = node.next;
        }
        return list;
    }

    /**
     * // Передача узла связанного списка - Node в метод remove(int id)
     */
    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> prev = node.prev;
            final Node<Task> next = node.next;
            node.data = null;

            if (tail == node && head != node) {
                tail = prev;
                tail.next = null;
            } else if (tail == node && head == node) {
                tail = null;
                head = null;
            } else if (tail != node && head != node) {
                next.prev = prev;
                prev.next = next;
            } else {
                head.prev = null;
                head = next;

            }

        }
    }
}
