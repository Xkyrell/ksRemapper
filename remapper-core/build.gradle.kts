plugins {
    id("remapper.base-conventions")
}

repositories {
    mavenCentral()
}

// Deprecated
dependencies {
    implementation("com.google.guava:guava:32.1.2-jre")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
}
