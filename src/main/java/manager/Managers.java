package manager;

import java.io.File;

import static Util.Constant.URL;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getHttpDefault(){
        return new HttpTaskManager(URL);
    }

    public static TaskManager getDefault(File file) {
        return FileBackedTasksManager.loadFromFile(file);
    }

    public static TaskManager getNewDefault(File file) {
        return new FileBackedTasksManager(file);
    }

}
