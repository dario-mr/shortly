package io.dagger.modules.ci;

import static io.dagger.client.Dagger.dag;

import io.dagger.client.Directory;
import io.dagger.client.Directory.DockerBuildArguments;
import io.dagger.client.Platform;
import io.dagger.client.Secret;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;
import java.util.List;

@Object
public class Ci {

  private static final String DOCKER_REGISTRY = "docker.io";

  /**
   * Run Maven tests inside a container.
   */
  @Function
  public String test(Directory source) throws Exception {
    var mavenCache = dag().cacheVolume("maven-cache");

    return dag().container()
        .from("maven:3.9.9-eclipse-temurin-21-jammy")
        .withMountedCache("/root/.m2", mavenCache)
        .withMountedDirectory(
            "/app",
            source
                .withoutDirectory(".git")
                .withoutDirectory(".dagger"))
        .withWorkdir("/app")
        .withExec(List.of("mvn", "--batch-mode", "test"))
        .stdout();
  }

  /**
   * Run tests, then build Docker image from Dockerfile, and push to Docker Hub.
   *
   * @param source         repo root
   * @param dockerUsername Docker Hub username
   * @param dockerPassword Docker Hub token/password (Secret)
   * @param imageTag       image tag (e.g., "username/repo:tag")
   * @return fully-qualified pushed image ref
   */
  @Function
  public String buildAndPush(
      Directory source,
      String dockerUsername, Secret dockerPassword,
      String imageTag
  ) throws Exception {
    // run tests
    this.test(source);

    // build image from Dockerfile
    var dockerArgs = new DockerBuildArguments()
        .withDockerfile("Dockerfile")
        .withPlatform(Platform.from("linux/arm64"));
    var image = source.dockerBuild(dockerArgs);

    // authenticate and push to Docker Hub
    var authed = image.withRegistryAuth(DOCKER_REGISTRY, dockerUsername, dockerPassword);
    return authed.publish(DOCKER_REGISTRY + "/" + imageTag);
  }
}
