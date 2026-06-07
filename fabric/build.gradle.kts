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
    api(libs.fabric.loader)
    implementation(libs.architectury.fabric)
    common(project(":common")) { isTransitive = false }
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
    dependsOn(tasks.named("jar"))
    from(zipTree(tasks.named<Jar>("jar").get().archiveFile))

    configurations = listOf(shadowBundle)
    archiveClassifier.set("dev-shadow")
    from(rootProject.file("LICENSE"))
}
