import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import com.kolibree.gradle.publisher.PublisherPluginExtension

buildscript {
    repositories {
        jcenter()
        mavenCentral()
        google()
        maven {
            val kolibree_artifactory_url: String by project
            val kolibree_artifactory_username: String by project
            val kolibree_artifactory_password: String by project

            setUrl("$kolibree_artifactory_url/libs-release")
            credentials {
                username = kolibree_artifactory_username
                password = kolibree_artifactory_password
            }
        }
    }
    dependencies {
        classpath(BuildPlugins.kotlinPlugin)
        classpath(BuildPlugins.junitGradle)
        classpath(BuildPlugins.dokka)
        classpath(BuildPlugins.kolibreePublisher)
    }
}


plugins {
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC6-4"
}

configure<DetektExtension> {
    debug = true
    version = "1.0.0.RC6-4"
    profile = "main"

    profile("main", Action {
        input = rootProject.projectDir.absolutePath
        filters = ".*/resources/.*,.*/build/.*,.*/sample-app/.*"
        config = "${rootProject.projectDir}/default-detekt-config.yml"
        baseline = "${rootProject.projectDir}/reports/baseline.xml"
    })
}

allprojects {
    group = "com.malinskiy.marathon"

    repositories {
        jcenter()
        mavenCentral()
        google()
    }
}

subprojects {
    apply(plugin = "kolibree-publisher")

    configure<PublisherPluginExtension> {
        val kolibree_artifactory_url: String by project
        val kolibree_artifactory_username: String by project
        val kolibree_artifactory_password: String by project

        groupId = group.toString()
        artifactId = project.name
        version = Versions.marathon

        repositoryUrl = kolibree_artifactory_url
        repositoryUsername = kolibree_artifactory_username
        repositoryPassword = kolibree_artifactory_password
    }
}
