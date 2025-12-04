plugins {
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val common by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

val shadowBundle by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

configurations {
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
    get("developmentFabric").extendsFrom(common)
}

dependencies {
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.architectury.fabric)
    common(project(":common", "namedElements")) { isTransitive = false }
    shadowBundle(project(":common", "transformProductionFabric"))
}

configure<BasePluginExtension> {
    archivesName.set("heartfall-fabric")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
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
