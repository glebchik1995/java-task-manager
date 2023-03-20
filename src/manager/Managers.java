package manager;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(){
        return new HttpTaskManager("http://localhost:8078/");
    }
}
