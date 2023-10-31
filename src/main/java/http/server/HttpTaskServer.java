package http.server;

import com.sun.net.httpserver.HttpServer;
import http.handler.EpicHandler;
import http.handler.SubtaskHandler;
import http.handler.TaskHandler;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer server;
    private static final int PORT = 8080;


    public HttpTaskServer(TaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task", new TaskHandler(manager));
        server.createContext("/tasks/subtask", new SubtaskHandler(manager));
        server.createContext("/tasks/epic", new EpicHandler(manager));
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту!");
    }
}
