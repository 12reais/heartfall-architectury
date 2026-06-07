import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    alias(libs.plugins.architectury)
    alias(libs.plugins.loom) apply false
    alias(libs.plugins.shadow) apply false
    java
}

architectury {
    minecraft = libs.versions.minecraft.get()
}

subprojects {
    apply(plugin = "dev.architectury.loom-no-remap")
    apply(plugin = "architectury-plugin")
    
    val loom = project.extensions.getByName("loom") as LoomGradleExtensionAPI

    dependencies {
         "minecraft"(rootProject.libs.minecraft)
    }

    configure<JavaPluginExtension> {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(25)
    }

    tasks.jar {
        archiveClassifier.set("raw")
    }
}
