package performance.throughput;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import performance.throughput.httprequesthandler.WordCountHandler;

public class HighThroughputHttpSearchServer {

  private static final String INPUT_FILE = "resources/war_and_peace.txt";

  private static final int NUM_OF_THREADS = 1;

  public static void main(String[] args) {
    try {
      String fileContent = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
      startServer(fileContent);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void startServer(String fileContent) throws IOException {

    // backlog = Size of the queue of the HTTP server requests = 0, hence no requests will be queued
    // by the HTTP server. The requests will rather end up in the Thread Pool's queue instead.
    HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);

    // createContext is used to assign a handler to a particular HTTP route. The handler handles
    // the HTTP request and returns a response.
    httpServer.createContext("/search", new WordCountHandler(fileContent));

    // Executor that schedules each incoming HTTP request to a pool of worker threads.
    Executor executor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    httpServer.setExecutor(executor);
    httpServer.start();
  }

}
