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