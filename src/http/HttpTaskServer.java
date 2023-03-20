package http;

import com.sun.net.httpserver.HttpServer;
import handler.EpicHandler;
import handler.HistoryHandler;
import handler.SubtaskHandler;
import handler.TaskHandler;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8088;
    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        TaskManager manager = Managers.getDefault();
        server.createContext("/tasks/task", new TaskHandler(manager));
        server.createContext("/tasks/epic", new EpicHandler(manager));
        server.createContext("/tasks/subtask", new SubtaskHandler(manager));
        server.createContext("/tasks/history", new HistoryHandler(manager));
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
    }
}
