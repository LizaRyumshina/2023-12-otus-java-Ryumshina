import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
    id("fr.brouillard.oss.gradle.jgitver") //a java library using git that computes a git repository version based on a given configuration
    id("io.spring.dependency-management") //плагин управления зависимостями от Spring
    id("org.springframework.boot") apply false // фреймворк Spring Boot
    id("name.remal.sonarlint") apply false //линтер
    id("com.diffplug.spotless") apply false // для автоматического форматирования и поддержки стилевых правил кода.
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(21)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


allprojects { //общие настройки для всех проектов
    group = "ru.otus"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val testcontainersBom: String by project
    val protobufBom: String by project
    val guava: String by project
    val jmh: String by project
    val asm: String by project
    val jetty: String by project
    val freemarker: String by project


    apply(plugin = "io.spring.dependency-management")
    dependencyManagement { //определение базовых версий зависимостей
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
                mavenBom("com.google.protobuf:protobuf-bom:$protobufBom")
            }
            dependency("com.google.guava:guava:$guava")
            dependency("org.openjdk.jmh:jmh-core:$jmh")
            dependency("org.openjdk.jmh:jmh-generator-annprocess:$jmh")
            dependency("org.ow2.asm:asm-commons:$asm")
            
            dependency("org.eclipse.jetty.ee10:jetty-ee10-servlet:$jetty")
            dependency("org.eclipse.jetty:jetty-server:$jetty")
            dependency("org.eclipse.jetty.ee10:jetty-ee10-webapp:$jetty")
            dependency("org.eclipse.jetty:jetty-security:$jetty")
            dependency("org.eclipse.jetty:jetty-http:$jetty")
            dependency("org.eclipse.jetty:jetty-io:$jetty")
            dependency("org.eclipse.jetty:jetty-util:$jetty")
            dependency("org.freemarker:freemarker:$freemarker")

        }
    }

    configurations.all { //для принудительного задания конкретных версий в случае конфликтов
        resolutionStrategy {
            failOnVersionConflict()
            
            // force -  принудительно задать требуемую версию для предотвращения возможных конфликтов в проекте
            force("javax.servlet:servlet-api:2.4") //устанавливает принудительно версию 2.4 для зависимости javax.servlet:servlet-api
            force("commons-logging:commons-logging:1.1.1")
            force("commons-lang:commons-lang:2.5")
            force("org.codehaus.jackson:jackson-core-asl:1.8.8")
            force("org.codehaus.jackson:jackson-mapper-asl:1.8.3")
            force("org.codehaus.jettison:jettison:1.1")
            force("net.java.dev.jna:jna:5.8.0")
            force("com.google.errorprone:error_prone_annotations:2.7.1")
            force("org.sonarsource.analyzer-commons:sonar-analyzer-commons:2.3.0.1263")
            force("com.google.code.findbugs:jsr305:3.0.2")
            force("org.sonarsource.sslr:sslr-core:1.24.0.633")
            force("org.eclipse.platform:org.eclipse.osgi:3.18.400")
            force("org.eclipse.platform:org.eclipse.equinox.common:3.18.0")
        }
    }
}

subprojects { // для всех проектов
    plugins.apply(JavaPlugin::class.java) // применить плагин JavaPlugin
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
    }

    apply<name.remal.gradle_plugins.sonarlint.SonarLintPlugin>()
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            palantirJavaFormat("2.38.0")
        }
    }

    plugins.apply(fr.brouillard.oss.gradle.plugins.JGitverPlugin::class.java)
    extensions.configure<fr.brouillard.oss.gradle.plugins.JGitverPluginExtension> {
        strategy("PATTERN")
        nonQualifierBranches("main,master")
        tagVersionPattern("\${v}\${<meta.DIRTY_TEXT}")
        versionPattern(
                "\${v}\${<meta.COMMIT_DISTANCE}\${<meta.GIT_SHA1_8}" +
                        "\${<meta.QUALIFIED_BRANCH_NAME}\${<meta.DIRTY_TEXT}-SNAPSHOT"
        )
    }

    tasks.withType<Test> { // Настройка задачи Test для выполнения тестов JUnit
        useJUnitPlatform()
        testLogging.showExceptions = true
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }
}
tasks {
    val managedVersions by registering {
        doLast {
            project.extensions.getByType<DependencyManagementExtension>()
                .managedVersions
                .toSortedMap()
                .map { "${it.key}:${it.value}" }
                .forEach(::println)
        }
    }
}