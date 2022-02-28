package tech.jhipster.lite.generator.client.vite.react.core.domain;

import static tech.jhipster.lite.common.domain.FileUtils.getPath;

import tech.jhipster.lite.error.domain.GeneratorException;
import tech.jhipster.lite.generator.packagemanager.npm.domain.NpmService;
import tech.jhipster.lite.generator.project.domain.Project;
import tech.jhipster.lite.generator.project.domain.ProjectRepository;

public class ViteReactDomainService implements ViteReactService {

  public static final String SOURCE = "client/vite/react";
  public static final String SOURCE_APP = "src/main/webapp/app/common/primary/app";

  private final ProjectRepository projectRepository;
  private final NpmService npmService;

  public ViteReactDomainService(ProjectRepository projectRepository, NpmService npmService) {
    this.projectRepository = projectRepository;
    this.npmService = npmService;
  }

  @Override
  public void addViteReact(Project project) {
    addCommonViteReact(project);
    addViteReactUnstyledFiles(project);
  }

  @Override
  public void addStyledViteReact(Project project) {
    addCommonViteReact(project);
    addViteReactStyledFiles(project);
  }

  public void addCommonViteReact(Project project) {
    addDevDependencies(project);
    addDependencies(project);
    addScripts(project);
    addFiles(project);
    addJestSonar(project);
    addViteReactCommonFiles(project);
  }

  public void addDevDependencies(Project project) {
    ViteReact
      .devDependencies()
      .forEach(dependency ->
        npmService
          .getVersionInViteReact(dependency)
          .ifPresentOrElse(
            version -> npmService.addDevDependency(project, dependency, version),
            () -> {
              throw new GeneratorException("DevDependency not found: " + dependency);
            }
          )
      );
  }

  public void addDependencies(Project project) {
    ViteReact
      .dependencies()
      .forEach(dependency ->
        npmService
          .getVersionInViteReact(dependency)
          .ifPresentOrElse(
            version -> npmService.addDependency(project, dependency, version),
            () -> {
              throw new GeneratorException("Dependency not found: " + dependency);
            }
          )
      );
  }

  public void addScripts(Project project) {
    ViteReact.scripts().forEach((name, cmd) -> npmService.addScript(project, name, cmd));
  }

  public void addFiles(Project project) {
    ViteReact.files().forEach(file -> projectRepository.add(project, SOURCE, file));
  }

  public void addViteReactCommonFiles(Project project) {
    ViteReact.reactCommonFiles().forEach((file, path) -> projectRepository.template(project, getPath(SOURCE, path), file, path));
  }

  public void addViteReactUnstyledFiles(Project project) {
    projectRepository.template(project, getPath(SOURCE, SOURCE_APP), "App.css", SOURCE_APP);
    projectRepository.template(project, getPath(SOURCE, SOURCE_APP), "App.tsx", SOURCE_APP);
  }

  public void addViteReactStyledFiles(Project project) {
    String imagesPath = "src/main/webapp/content/images";
    projectRepository.template(project, getPath(SOURCE, SOURCE_APP), "StyledApp.css", SOURCE_APP, "App.css");
    projectRepository.template(project, getPath(SOURCE, SOURCE_APP), "StyledApp.tsx", SOURCE_APP, "App.tsx");
    projectRepository.add(project, getPath(SOURCE, imagesPath), "ReactLogo.png", imagesPath);
    projectRepository.add(project, getPath(SOURCE, imagesPath), "JHipster-Lite-neon-blue.png", imagesPath);
  }

  public void addJestSonar(Project project) {
    String oldText = "\"cacheDirectories\": \\[";
    String newText =
      """
      "jestSonar": \\{
          "reportPath": "target/test-results/jest",
          "reportFile": "TESTS-results-sonar.xml"
        \\},
        "cacheDirectories": \\[""";
    projectRepository.replaceText(project, "", "package.json", oldText, newText);
  }
}
