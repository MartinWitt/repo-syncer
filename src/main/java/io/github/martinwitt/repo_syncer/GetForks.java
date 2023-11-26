package io.github.martinwitt.repo_syncer;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.List;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This class represents a utility for getting all forks of the authenticated user's repositories on
 * GitHub.
 */
@ApplicationScoped
public class GetForks {

  private GitHub gitHub;

  public GetForks() throws IOException {
    gitHub = GitHub.connectUsingOAuth(System.getenv("GITHUB_TOKEN"));
  }

  public List<GHRepository> getAllForks() {
    try {
      var list =
          gitHub.getMyself().listRepositories().withPageSize(100).toList().stream()
              .filter(repo -> repo.isFork())
              .filter(
                  v -> {
                    try {
                      return v.getOwner().getLogin().equals(gitHub.getMyself().getLogin());
                    } catch (IOException e) {
                      Log.error(
                          "Error while getting owner of repository %s".formatted(v.getName()));
                      return false;
                    }
                  })
              .toList();
      return list;
    } catch (IOException e) {
      e.printStackTrace();
      return List.of();
    }
  }
}
