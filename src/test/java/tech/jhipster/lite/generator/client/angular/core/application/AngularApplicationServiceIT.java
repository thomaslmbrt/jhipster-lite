package tech.jhipster.lite.generator.client.angular.core.application;

import static tech.jhipster.lite.TestUtils.*;
import static tech.jhipster.lite.generator.client.angular.core.application.AngularAssert.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.jhipster.lite.IntegrationTest;
import tech.jhipster.lite.generator.project.domain.Project;

@IntegrationTest
class AngularApplicationServiceIT {

  @Autowired
  AngularApplicationService angularApplicationService;

  @Test
  void shouldAddAngular() {
    Project project = tmpProjectWithPackageJsonComplete();

    angularApplicationService.addAngular(project);

    assertDevDependencies(project);
    assertDependencies(project);
    assertScripts(project);
    assertConfigFiles(project);
    assertAngularFiles(project);
    assertAppWithoutCss(project);
  }

  @Test
  void shouldAddStyledAngular() {
    Project project = tmpProjectWithPackageJsonComplete();

    angularApplicationService.addStyledAngular(project);

    assertDevDependencies(project);
    assertDependencies(project);
    assertScripts(project);
    assertConfigFiles(project);
    assertAngularFiles(project);
    assertAppWithCss(project);
    assertLogos(project);
  }
}
