plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.0"
}

group = "co.edu.unal"
version = "1.5.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.googlecode.soundlibs:basicplayer:3.0.0.0")
    implementation("org.pushing-pixels:radiance-substance:4.5.0")
    implementation("com.formdev:flatlaf:3.7.2")
    implementation("com.mpatric:mp3agic:0.9.1")
    implementation("com.melloware:jintellitype:1.5.6")
    implementation("se.michaelthelin.spotify:spotify-web-api-java:9.4.0")
    implementation("io.javalin:javalin:7.2.2")
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation("com.miglayout:miglayout-swing:11.4.3")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly("org.projectlombok:lombok:1.18.46")

    testAnnotationProcessor("org.projectlombok:lombok:1.18.46")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest {
        attributes(
            "Main-Class" to "co.edu.unal.Main"
        )
    }
}