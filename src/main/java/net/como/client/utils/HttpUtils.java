package net.como.client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class HttpUtils {
    private static final HttpUtils INSTANCE = new HttpUtils();
    private final HttpClient CLIENT = HttpClient.newHttpClient();

    private final Gson GSON = new GsonBuilder().create();

    public static HttpUtils getInstance() {
        return INSTANCE;
    }

    private enum Method {
        GET,
        POST
    }

    public class Request {
        private HttpRequest.Builder builder;
        private Method method;

        public Request(Method method, String url) {
            try {
                this.builder = HttpRequest.newBuilder().uri(new URI(url)).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
                this.method = method;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        public Request bearer(String token) {
            builder.header("Authorization", "Bearer " + token);

            return this;
        }

        public Request bodyString(String string) {
            builder.header("Content-Type", "text/plain");
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(string));
            method = null;

            return this;
        }

        public Request bodyForm(String string) {
            builder.header("Content-Type", "application/x-www-form-urlencoded");
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(string));
            method = null;

            return this;
        }

        public Request bodyJson(String string) {
            builder.header("Content-Type", "application/json");
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(string));
            method = null;

            return this;
        }

        public Request bodyJson(Object object) {
            builder.header("Content-Type", "application/json");
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(GSON.toJson(object)));
            method = null;

            return this;
        }

        private <T> T send(String accept, HttpResponse.BodyHandler<T> responseBodyHandler) {
            builder.header("Accept", accept);
            if (method != null) builder.method(method.name(), HttpRequest.BodyPublishers.noBody());

            try {
                var res = CLIENT.send(builder.build(), responseBodyHandler);
                return res.statusCode() == 200 ? res.body() : null;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void send() {
            send("*/*", HttpResponse.BodyHandlers.discarding());
        }

        public InputStream sendInputStream() {
            return send("*/*", HttpResponse.BodyHandlers.ofInputStream());
        }

        public String sendString() {
            return send("*/*", HttpResponse.BodyHandlers.ofString());
        }

        public Stream<String> sendLines() {
            return send("*/*", HttpResponse.BodyHandlers.ofLines());
        }

        public <T> T sendJson(Type type) {
            InputStream in = send("application/json", HttpResponse.BodyHandlers.ofInputStream());
            return in == null ? null : GSON.fromJson(new InputStreamReader(in), type);
        }
    }

    public Request get(String url) {
        return new Request(Method.GET, url);
    }

    public Request post(String url) {
        return new Request(Method.POST, url);
    }
}
