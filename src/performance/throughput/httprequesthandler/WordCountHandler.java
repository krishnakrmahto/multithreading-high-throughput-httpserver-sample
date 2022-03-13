package performance.throughput.httprequesthandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class WordCountHandler implements HttpHandler {

  private final String textToSearchIn;
  public WordCountHandler(String textToSearchIn) {
    this.textToSearchIn = textToSearchIn;
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String query = httpExchange.getRequestURI().getQuery();
    String[] keyValue = query.split("=");

    String key = keyValue[0];
    String value = keyValue[1];

    if (!"word".equals(key)) {
      httpExchange.sendResponseHeaders(400, 0);
    } else {
      int count = countWord(value);

      byte[] response = Long.toString(count).getBytes();
      httpExchange.sendResponseHeaders(200, response.length);
      OutputStream responseBodyOutputStream = httpExchange.getResponseBody();
      responseBodyOutputStream.write(response);

      // Close the responseBodyOutputStream which will result in sending the response to the client
      responseBodyOutputStream.close();
    }

  }

  private int countWord(String value) {

    int count = 0;
    int index = 0;

    while (index >= 0) {
      index = textToSearchIn.indexOf(value, index);
      if (index >= 0) {
        count++;
        index++;
      }
    }

    return count;
  }
}
