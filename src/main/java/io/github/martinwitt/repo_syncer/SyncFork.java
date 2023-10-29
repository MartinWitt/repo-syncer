package io.github.martinwitt.repo_syncer;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import org.kohsuke.github.GHRepository;

@ApplicationScoped
public class SyncFork {

  private String token = System.getenv("GITHUB_TOKEN");

  public boolean syncFork(GHRepository repo) {
    if (!repo.isFork()) {
      return false;
    }
    try {
      JsonObject json = Json.createObjectBuilder().add("branch", repo.getDefaultBranch()).build();
      String jsonString = json.toString();

      String url =
          "https://api.github.com/repos/"
              + repo.getOwnerName()
              + "/"
              + repo.getName()
              + "/merge-upstream";
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .POST(BodyPublishers.ofString(jsonString))
              .header("Accept", "application/vnd.github+json")
              .header("Authorization", "Bearer %s".formatted(token))
              .build();
      HttpClient client = HttpClient.newHttpClient();
      var result = client.send(request, HttpResponse.BodyHandlers.ofString());
      return result.statusCode() == 200;
    } catch (IOException | InterruptedException e) {
      Log.error("Error while syncing fork", e);
      return false;
    }
  }
}
