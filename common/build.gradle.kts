val enabledPlatforms: String by rootProject

architectury {
    common(enabledPlatforms.split(','))
}

dependencies {
    implementation(libs.fabric.loader)
    implementation(libs.architectury)
    compileOnly(libs.yacl.fabric)
    compileOnly(libs.yacl.neoforge)
}
