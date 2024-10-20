plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "seungkyu"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

extra["springCloudVersion"] = "2023.0.3"

dependencies {

    implementation(project(":article:core"))

    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.3.4")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")

//    implementation("org.springframework.cloud:spring-cloud-stream:3.1.3")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka:3.2.1")


    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    // r2dbc
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.3.4")

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.4")

    // lombok
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    compileOnly("org.projectlombok:lombok:1.18.34")

    // reactor test
    testImplementation("io.projectreactor:reactor-test:3.6.10")

    // testcontainer
    testImplementation("org.testcontainers:testcontainers:1.20.2")
    testImplementation("org.testcontainers:junit-jupiter:1.20.2")
    testImplementation("org.testcontainers:mongodb:1.20.2")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
