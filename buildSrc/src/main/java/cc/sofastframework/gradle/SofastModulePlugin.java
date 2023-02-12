package cc.sofastframework.gradle;


import io.freefair.gradle.plugins.lombok.LombokBasePlugin;
import io.freefair.gradle.plugins.lombok.LombokPlugin;
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin;
import org.graalvm.buildtools.gradle.NativeImagePlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Bundling;
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.attributes.Usage;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.springframework.boot.gradle.plugin.SpringBootAotPlugin;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

public class SofastModulePlugin implements Plugin<Project> {

    private static final String STAGE = "stage";

    private static final String PROD = "prod";

    private static final String PROFILE = "profile";

    @Override
    public void apply(Project project) {
        System.out.println("Sofast Dependency Manager plugin.");

        //java 插件
        project.getPlugins().apply(JavaPlugin.class);
        project.getPlugins().apply(JavaBasePlugin.class);

        //springboot
        project.getPlugins().apply(SpringBootAotPlugin.class);
        project.getPlugins().apply(SpringBootPlugin.class);
        project.getPlugins().apply(DependencyManagementPlugin.class);
        project.getPlugins().apply(NativeImagePlugin.class);

        //lombok
        project.getPlugins().apply(LombokPlugin.class);
        project.getPlugins().apply(LombokBasePlugin.class);

        project.getPlugins().apply(JavaConventionsPlugin.class);

        //依赖仓库
        project.getRepositories().mavenLocal();
        project.getRepositories().mavenCentral();
        project.getRepositories().maven(mavenArtifactRepository -> mavenArtifactRepository.setUrl("https://maven.aliyun.com/nexus/content/groups/public/"));
        project.getRepositories().maven(mavenArtifactRepository -> mavenArtifactRepository.setUrl("https://repo.spring.io/release"));
        project.getRepositories().maven(mavenArtifactRepository -> mavenArtifactRepository.setUrl("https://repo.spring.io/milestone"));

        //配置开发阶段的依赖
        configureDevelopmentStageConfiguration(project);
    }

    private void configureDevelopmentStageConfiguration(Project project) {
        Configuration developmentStage = project.getConfigurations().create("developmentStage");
        developmentStage.setDescription("Configuration for development-stage dependencies.");
        if (!prodAndStageBuild(project)) {
            Configuration runtimeClasspath = project.getConfigurations().getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME);
            Configuration productionRuntimeClasspath = project.getConfigurations().create("customProductionRuntimeClasspath");
            AttributeContainer attributes = productionRuntimeClasspath.getAttributes();
            ObjectFactory objectFactory = project.getObjects();
            attributes.attribute(Usage.USAGE_ATTRIBUTE, objectFactory.named(Usage.class, Usage.JAVA_RUNTIME));
            attributes.attribute(Bundling.BUNDLING_ATTRIBUTE, objectFactory.named(Bundling.class, Bundling.EXTERNAL));
            attributes.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
                    objectFactory.named(LibraryElements.class, LibraryElements.JAR));
            productionRuntimeClasspath.setVisible(false);
            productionRuntimeClasspath.setExtendsFrom(runtimeClasspath.getExtendsFrom());
            productionRuntimeClasspath.setCanBeResolved(runtimeClasspath.isCanBeResolved());
            productionRuntimeClasspath.setCanBeConsumed(runtimeClasspath.isCanBeConsumed());
        }
    }

    private boolean prodAndStageBuild(Project project) {
        Object profile = project.getProperties().get(PROFILE);
        if (profile == null) {
            return false;
        }
        String profileStr = String.valueOf(profile);
        System.out.println("build package profile is " + profileStr);
        return STAGE.equals(profileStr) || PROD.equals(profileStr);
    }
}

