plugins {
    id("java")
    id("application")
}

group = "org.SquidSquad"
version = "0.1-PRE-ALPHA"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "org.SquidSquad.Main"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.SquidSquad.Main"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}