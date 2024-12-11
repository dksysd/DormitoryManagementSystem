plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.github.cdimascio:dotenv-java:3.0.2")
    implementation("com.mysql:mysql-connector-j:9.1.0")
    implementation("com.zaxxer:HikariCP:6.0.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("org.slf4j:slf4j-api:1.7.36") // SLF4J API
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0") // Log4j 구현
    testImplementation("org.slf4j:slf4j-simple:2.1.0-alpha1")
}

tasks.test {
    useJUnitPlatform()
}