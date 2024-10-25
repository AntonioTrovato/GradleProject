import org.gradle.testing.jacoco.plugins.JacocoTaskExtension.Output

plugins {
    java
    jacoco
}

jacoco {
    toolVersion = "0.8.8"
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.10.0")
    implementation("com.github.javaparser:javaparser-core:3.24.4")
    testImplementation("org.jacoco:org.jacoco.core:0.8.8")
    testImplementation("org.jacoco:org.jacoco.agent:0.8.8")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
}

tasks.named<Test>("test") {
    useJUnit()

    // run the JaCoCo Agent
    extensions.configure(JacocoTaskExtension::class.java) {
        output = Output.TCP_SERVER
        address = "localhost"
        isJmx = true

        // Exclude classes that are not in the 'app/java/main' package
        excludes = listOf(
            "**/test/**",       // Exclude any test classes
            "**/generated/**",  // Exclude generated classes, if any
            "**/jmh/java/**",
            "net/**",
            "org/**",
            "com/**",
            "worker/**"
        )
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)  // Assicurati che i benchmark siano eseguiti prima di generare il report
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