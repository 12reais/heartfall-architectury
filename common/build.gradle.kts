val enabledPlatforms: String by rootProject

architectury {
    common(enabledPlatforms.split(','))
}

dependencies {
    modImplementation(libs.fabric.loader)
    modImplementation(libs.architectury)
}
