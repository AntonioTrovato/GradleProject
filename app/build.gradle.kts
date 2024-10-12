plugins {
    java
    id("jacoco")
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.10.0")
    implementation("com.github.javaparser:javaparser-core:3.24.4")
}

tasks.named<Test>("test") {
    useJUnit()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

// Task per creare un JAR "fat" con tutte le dipendenze
tasks.register<Jar>("fatJar") {
    group = "build"
    archiveClassifier.set("all")

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

    manifest {
        attributes["Main-Class"] = "ASTGenerator" // Assicurati che il nome completo della classe sia corretto
    }
}