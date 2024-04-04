plugins {
    java
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.10.0")
}

tasks.named<Test>("test") {
    useJUnit()
}