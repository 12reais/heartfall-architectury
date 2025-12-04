plugins {
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

val common: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

configurations {
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
    get("developmentNeoForge").extendsFrom(common)
}

repositories {
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    neoForge(libs.neoforge)
    modImplementation(libs.architectury.neoforge)
    common(project(":common", "namedElements")) { isTransitive = false }
    shadowBundle(project(":common", "transformProductionNeoForge"))
}

configure<BasePluginExtension> {
    archivesName.set("heartfall-neoforge")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(
            "version" to project.version.toString(),
            "minecraft" to rootProject.libs.versions.minecraft.get(),
            "modName" to rootProject.extra["modName"].toString(),
            "modDescription" to rootProject.extra["modDescription"].toString(),
            "modAuthor" to rootProject.extra["modAuthor"].toString(),
            "modLicense" to rootProject.extra["modLicense"].toString(),
            "enabledPlatforms" to rootProject.extra["enabledPlatforms"].toString()
        )
    }
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
}

tasks.remapJar {
    inputFile = tasks.shadowJar.get().archiveFile
    dependsOn(tasks.shadowJar)
}
