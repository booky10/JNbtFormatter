import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

plugins {
    id("java-library")
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.booky"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api("net.minecrell:terminalconsoleappender:1.3.0")
    runtimeOnly("org.jline:jline-terminal-jansi:3.21.0")
    runtimeOnly("com.lmax:disruptor:3.4.4")

    val log4jVersion = "2.19.0"
    api("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    api("org.apache.logging.log4j:log4j-iostreams:$log4jVersion")
    api("org.apache.logging.log4j:log4j-core:$log4jVersion")
    api("org.apache.logging.log4j:log4j-jul:$log4jVersion")
    api("org.apache.logging.log4j:log4j-api:$log4jVersion")

    api("io.javalin:javalin:5.4.2")
    api("org.spongepowered:configurate-yaml:4.1.2")
    api("net.kyori:adventure-nbt:4.11.0")
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

val bootClass = "$group.nbtfmt.main.NbtFormatterMain"
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

            "Multi-Release" to "true",
            "Main-Class" to bootClass
        )
    }

    shadowJar {
        transform(Log4j2PluginsCacheFileTransformer::class.java)
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaExec> {
        workingDir = file("run")
        workingDir.mkdirs()
        standardInput = System.`in`
    }
}
