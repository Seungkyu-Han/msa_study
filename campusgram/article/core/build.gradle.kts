plugins {
    kotlin("jvm") version "1.9.25"
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // javax.inject
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    // nullable
    implementation("com.github.spotbugs:spotbugs-annotations:4.8.6")


    // reactor
    implementation("io.projectreactor:reactor-core:3.6.11")

    /* test */

    // mockito
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")

    // reactor
    testImplementation("io.projectreactor:reactor-test:3.6.11")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.4")

    // lombok
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    compileOnly("org.projectlombok:lombok:1.18.28")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.0")

}

tasks.test{
    useJUnitPlatform()
}