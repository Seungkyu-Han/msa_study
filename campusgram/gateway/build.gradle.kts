plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "seungkyu"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.springframework.boot:spring-boot-starter-webflux:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.1.5")

    implementation("org.springframework.boot:spring-boot-starter-web")

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-aarch_64")

    // lombok
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")


}

tasks.test {
    useJUnitPlatform()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}