package cc.sofastframework.gradle;

import cc.sofastframework.gradle.optional.OptionalDependenciesPlugin;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.external.javadoc.CoreJavadocOptions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * @author apple
 */
public class JavaConventionsPlugin implements Plugin<Project> {

    private static final String SOURCE_AND_TARGET_COMPATIBILITY = "17";

    @Override
    public void apply(Project project) {
        System.out.println("JavaConventionsPlugin");
        project.getPlugins().apply(OptionalDependenciesPlugin.class);
        project.getPlugins().withType(JavaBasePlugin.class, (java) -> {
            configureJavaConventions(project);
            configureJavadocConventions(project);
            configureDependencyManagement(project);
        });
    }

    private void configureJavadocConventions(Project project) {
        project.getTasks().withType(Javadoc.class, (javadoc) -> {
            CoreJavadocOptions options = (CoreJavadocOptions) javadoc.getOptions();
            options.source("17");
            options.encoding("UTF-8");
            options.addStringOption("Xdoclint:none", "-quiet");
        });
    }

    private void configureJavaConventions(Project project) {
        if (!project.hasProperty("toolchainVersion")) {
            JavaPluginExtension javaPluginExtension = project.getExtensions().getByType(JavaPluginExtension.class);
            javaPluginExtension.setSourceCompatibility(JavaVersion.toVersion(SOURCE_AND_TARGET_COMPATIBILITY));
        }
        project.getTasks().withType(JavaCompile.class, (compile) -> {
            compile.getOptions().setEncoding("UTF-8");
            List<String> args = compile.getOptions().getCompilerArgs();
            if (!args.contains("-parameters")) {
                args.add("-parameters");
            }
            if (project.hasProperty("toolchainVersion")) {
                compile.setSourceCompatibility(SOURCE_AND_TARGET_COMPATIBILITY);
                compile.setTargetCompatibility(SOURCE_AND_TARGET_COMPATIBILITY);
            } else if (buildingWithJava17(project)) {
                args.addAll(Arrays.asList("-Werror", "-Xlint:unchecked", "-Xlint:deprecation", "-Xlint:rawtypes",
                        "-Xlint:varargs"));
            }
        });
    }

    private boolean buildingWithJava17(Project project) {
        return !project.hasProperty("toolchainVersion") && JavaVersion.current() == JavaVersion.VERSION_17;
    }

    private void configureDependencyManagement(Project project) {
        ConfigurationContainer configurations = project.getConfigurations();
        Configuration dependencyManagement = configurations.create("dependencyManagement", (configuration) -> {
            configuration.setVisible(false);
            configuration.setCanBeConsumed(false);
            configuration.setCanBeResolved(false);
        });
        configurations
                .matching((c) -> c.getName().endsWith("Classpath") || c.getName().toLowerCase().endsWith("annotationprocessor"))
                .all((c) -> c.extendsFrom(dependencyManagement));
        Dependency pulsarDependencies = project.getDependencies().enforcedPlatform(project.getDependencies()
                .project(Collections.singletonMap("path", ":dependencies")));
        dependencyManagement.getDependencies().add(pulsarDependencies);
        project.getPlugins().withType(OptionalDependenciesPlugin.class, (optionalDependencies) -> configurations
                .getByName(OptionalDependenciesPlugin.OPTIONAL_CONFIGURATION_NAME).extendsFrom(dependencyManagement));
    }


}
