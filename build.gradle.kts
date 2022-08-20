plugins {
    id("application")
    id("java-library")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.booky"
version = "1.0.0"
val bootClass = "$group.nbtfmt.${name}Main"

repositories {
    mavenCentral()
}

dependencies {
    api("net.kyori:adventure-nbt:4.11.0")
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.toLowerCase()
        from(components["java"])
    }
}

application {
    mainClass.set(bootClass)
}

tasks {
    withType<JavaExec> {
        workingDir = projectDir.resolve("run")
        workingDir.mkdirs()
        standardInput = System.`in`
    }

    jar {
        manifest.attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "booky10",
            "Main-Class" to bootClass
        )
    }

    shadowJar {
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }
}
