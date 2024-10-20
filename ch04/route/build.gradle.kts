plugins {
	java
	war
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "seungkyu"
version = "0.0.1-SNAPSHOT"

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

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.security:spring-security-core")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.cloud:" +
			"spring-cloud-starter-circuitbreaker-reactor-resilience4j:3.1.2")

	implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.1.5")

	implementation("org.springframework.boot:spring-boot-starter-web")

	// spring cloud stream
	implementation("org.springframework.cloud:spring-cloud-stream:4.1.3")
	implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka:4.1.3")


	/* test */
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// lombok
	testAnnotationProcessor("org.projectlombok:lombok")
	testCompileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")

	// mockito
	testImplementation("org.mockito:mockito-core")

	// reactor test
	testImplementation("io.projectreactor:reactor-test")
	implementation("io.projectreactor:reactor-tools")

	// mockWebServer
	testImplementation("com.squareup.okhttp3:mockwebserver")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
