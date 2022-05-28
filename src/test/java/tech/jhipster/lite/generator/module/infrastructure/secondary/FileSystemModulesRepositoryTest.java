package tech.jhipster.lite.generator.module.infrastructure.secondary;

import static tech.jhipster.lite.generator.module.domain.JHipsterModulesFixture.*;
import static tech.jhipster.lite.generator.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.generator.module.domain.JHipsterModule;
import tech.jhipster.lite.generator.module.domain.JHipsterProjectFolder;

@UnitTest
class FileSystemModulesRepositoryTest {

  @Test
  void shouldApplyModule() {
    JHipsterModule module = module();
    addPomToproject(module.projectFolder());

    assertThatModule(module)
      .createFiles(
        "src/main/java/com/company/myapp/MyApp.java",
        "src/main/java/com/company/myapp/errors/Assert.java",
        "src/main/java/com/company/myapp/errors/AssertionException.java",
        ".gitignore"
      )
      .createFile("src/main/java/com/company/myapp/MyApp.java")
      .containing("com.test.myapp")
      .and()
      .createFile("pom.xml")
      .containing(
        """
                <dependency>
                  <groupId>org.junit.jupiter</groupId>
                  <artifactId>junit-jupiter-engine</artifactId>
                  <version>${spring-boot.version}</version>
                  <scope>test</scope>
                  <optional>true</optional>
                </dependency>
            """
      )
      .containing("<spring-boot.version>")
      .containing("</spring-boot.version>")
      .containing("<artifactId>spring-boot-starter</artifactId>")
      .containing("<artifactId>problem-spring-web</artifactId>")
      .notContaining(
        "    <dependency>\n" +
        "      <groupId>org.assertj</groupId>\n" +
        "      <artifactId>assertj-core</artifactId>\n" +
        "      <version>${assertj.version}</version>\n" +
        "      <scope>test</scope>\n" +
        "    </dependency>"
      );
  }

  private static void addPomToproject(JHipsterProjectFolder project) {
    Path folder = Paths.get(project.folder());
    try {
      Files.createDirectories(folder);
    } catch (IOException e) {
      throw new AssertionError(e);
    }

    Path pomPath = folder.resolve("pom.xml");
    try {
      Files.copy(Paths.get("src/test/resources/projects/maven/pom.xml"), pomPath);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }
}