val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val swagger_version: String by project
val junit_version: String by project
val h2_version: String by project

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.noarg") version "1.6.10"
    id("io.ktor.plugin") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
}

group = "mimsoft.io"
version = "0.0.1"
application {
    mainClass.set("mimsoft.io.ApplicationKt")


    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")

}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("org.slf4j:slf4j-log4j12:2.0.5")
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.4")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson-jvm:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.10")


    //tgbot
    implementation("org.telegram:telegrambots:6.5.0")
    implementation("io.ktor:ktor-network-jvm:2.3.4")

    //security
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")


    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junit_version}")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junit_version}")

}

//ktor{
//    fatJar {
//        archiveFileName.set("lamenu-all.jar")
//    }
//    fatJar{
//        archiveFileName.set("lamenu-back.jar")
//    }
//
//}
ktor {
    fatJar {
        archiveFileName.set("lamenu-all.jar")
    }
}
//
//ktor{
//    fatJar{
//        archiveFileName.set("lamenu-beta.jar")
//    }
//    fatJar{
//        archiveFileName.set("lamenu-dev.jar")
//    }
//}
