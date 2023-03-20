package http;

import exception.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private final String apiToken;

    private final String url;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public KVTaskClient(String url) {
        this.url = url;
        apiToken = register(url);
    }

    private String register(String url) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "register/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-type", "application/json")
                .build();
        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            System.out.println("Код ответа: " + response.statusCode());
            System.out.println("Тело ответа: " + response.body());
            return response.body();
        } catch (Exception exception) {
            throw new ManagerSaveException("Сообщение об ошибке регистрации API_TOKEN" + exception.getMessage());

        }
    }

    public void save(String key, String value) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublishers.ofString(value, DEFAULT_CHARSET);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(value))
                .header("Content-type", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException exception) {
            throw new ManagerSaveException("Сообщение об ошибке сохранения" + exception.getMessage());
        }
    }

    public void load(String key) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-type", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            System.out.println("Код ответа: " + response.statusCode());
            System.out.println("Тело ответа: " + response.body());
            response.body();
        } catch (Exception exception) {
            throw new ManagerSaveException("Сообщение об ошибке загрузки" + exception.getMessage());

        }
    }
}

