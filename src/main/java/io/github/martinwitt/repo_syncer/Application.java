package io.github.martinwitt.repo_syncer;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import io.vertx.core.Vertx;
import java.time.Duration;
import org.kohsuke.github.GHRepository;

public class Application {

  private GetForks getForks;
  private SyncFork syncFork;
  private Vertx vertx;
  private long duration = Duration.ofHours(6).toMillis();

  public Application(GetForks getForks, SyncFork syncFork, Vertx vertx) {
    this.getForks = getForks;
    this.syncFork = syncFork;
    this.vertx = vertx;
  }

  @Scheduled(every = "6h")
  public void updateForks() {
    var result = getForks.getAllForks();
    Log.info("Syncing %d forks".formatted(result.size()));
    long start = System.currentTimeMillis();
    int count = 0;
    for (GHRepository repository : result) {
      if (syncFork.syncFork(repository)) {
        count++;
      }
    }
    long end = System.currentTimeMillis();
    Log.info("Synced %d forks in %d ms".formatted(count, end - start));
  }
}
