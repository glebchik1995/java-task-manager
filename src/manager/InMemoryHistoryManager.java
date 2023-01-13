package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> savedHistory = new ArrayList<>();

    private static final int MAX_SIZE_BY_HISTORY = 10;

    @Override
    public List<Task> getHistory() {
        return savedHistory;
    }

    @Override
    public void add(Task task) {
        if (savedHistory.size() < MAX_SIZE_BY_HISTORY) {
            savedHistory.add(task);
        } else {
            savedHistory.remove(0);
            savedHistory.add(task);
        }
    }
}
