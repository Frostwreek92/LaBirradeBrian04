plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.mysql:mysql-connector-j:9.0.0") //MySQL
    implementation("org.mongodb:mongodb-driver-sync:4.11.0")
}

tasks.test {
    useJUnitPlatform()
}