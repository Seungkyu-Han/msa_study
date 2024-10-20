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

extra["springCloudVersion"] = "2023.0.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.cloud:" +
			"spring-cloud-starter-circuitbreaker-reactor-resilience4j")

	implementation("org.springframework.cloud:spring-cloud-starter-gateway")

	// spring cloud stream
	implementation("org.springframework.cloud:spring-cloud-stream")
	implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")


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

	// mockWebServer
	testImplementation("com.squareup.okhttp3:mockwebserver")

	// spring-cloud-stream
	testImplementation("org.springframework.cloud:spring-cloud-stream") {
		artifact {
			name = "spring-cloud-stream"
			extension = "jar"
			type ="test-jar"
			classifier = "test-binder"
		}
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
