import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    alias(libs.plugins.architectury)
    alias(libs.plugins.loom) apply false
    alias(libs.plugins.shadow) apply false
}

architectury {
    minecraft = libs.versions.minecraft.get()
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    val loom = project.extensions.getByName("loom") as LoomGradleExtensionAPI

    configure<JavaPluginExtension> {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        "minecraft"(rootProject.libs.minecraft)
        "mappings"(loom.officialMojangMappings())
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(21)
    }
}
