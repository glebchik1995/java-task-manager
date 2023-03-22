package manager;

import http.KVServer;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(){
        return new HttpTaskManager(KVServer.PORT);
    }
}
