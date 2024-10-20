plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":article:core"))
    implementation(project(":article:persistence"))

    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-mustache:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.3.4")
    implementation("org.springframework.cloud:" +
            "spring-cloud-starter-circuitbreaker-reactor-resilience4j")

    implementation("org.springframework.cloud:spring-cloud-stream:3.1.3")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka:3.1.3")

    // reactor tool
    implementation("io.projectreactor:reactor-tools")

    // rxjava
    implementation("io.reactivex.rxjava3:rxjava")

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.0.Alpha5")

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.4")

    // lombok
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
    testCompileOnly("org.projectlombok:lombok:1.18.34")

    annotationProcessor("org.projectlombok:lombok:1.18.34")
    compileOnly("org.projectlombok:lombok:1.18.34")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
    }
}

tasks.test{
    useJUnitPlatform()
}