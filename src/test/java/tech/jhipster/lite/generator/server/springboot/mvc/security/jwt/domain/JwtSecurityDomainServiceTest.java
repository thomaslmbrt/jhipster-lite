package tech.jhipster.lite.generator.server.springboot.mvc.security.jwt.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static tech.jhipster.lite.TestUtils.tmpProjectWithPomXml;

import java.util.Optional;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.error.domain.GeneratorException;
import tech.jhipster.lite.generator.buildtool.generic.domain.BuildToolService;
import tech.jhipster.lite.generator.buildtool.generic.domain.Dependency;
import tech.jhipster.lite.generator.project.domain.Project;
import tech.jhipster.lite.generator.project.domain.ProjectRepository;
import tech.jhipster.lite.generator.project.infrastructure.secondary.GitUtils;
import tech.jhipster.lite.generator.server.springboot.common.domain.Level;
import tech.jhipster.lite.generator.server.springboot.common.domain.SpringBootCommonService;

@UnitTest
@ExtendWith(MockitoExtension.class)
class JwtSecurityDomainServiceTest {

  @Mock
  SpringBootCommonService springBootCommonService;

  @Mock
  ProjectRepository projectRepository;

  @Mock
  BuildToolService buildToolService;

  @InjectMocks
  JwtSecurityDomainService jwtSecurityDomainService;

  @Test
  void shouldInit() throws GitAPIException {
    Project project = tmpProjectWithPomXml();
    GitUtils.init(project.getFolder());
    when(buildToolService.getVersion(project, "jjwt")).thenReturn(Optional.of("0.0.0"));

    jwtSecurityDomainService.init(project);

    verify(buildToolService).addProperty(any(Project.class), anyString(), anyString());
    verify(buildToolService, times(5)).addDependency(any(Project.class), any(Dependency.class));

    // 9 classes + 3 tests
    verify(projectRepository, times(12)).template(any(Project.class), anyString(), anyString(), anyString());

    verify(springBootCommonService, times(3)).addProperties(any(Project.class), anyString(), any());
    verify(springBootCommonService, times(3)).addPropertiesTest(any(Project.class), anyString(), any());

    verify(springBootCommonService).addLoggerTest(any(Project.class), anyString(), any(Level.class));

    // 4 replaces
    verify(projectRepository, times(4)).replaceText(any(Project.class), anyString(), anyString(), anyString(), anyString());
  }

  @Test
  void shouldNotaddPropertyAndDependency() throws GitAPIException {
    Project project = tmpProjectWithPomXml();
    GitUtils.init(project.getFolder());

    assertThatThrownBy(() -> jwtSecurityDomainService.init(project)).isExactlyInstanceOf(GeneratorException.class);
  }

  @Test
  void shouldAddBasicAuth() {
    Project project = tmpProjectWithPomXml();

    jwtSecurityDomainService.addBasicAuth(project);

    verify(projectRepository, times(6)).template(any(Project.class), anyString(), anyString(), anyString());
    verify(springBootCommonService, times(3)).addProperties(any(Project.class), anyString(), any());
    verify(springBootCommonService, times(3)).addPropertiesTest(any(Project.class), anyString(), any());
  }
}
