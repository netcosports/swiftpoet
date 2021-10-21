import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `java-library`
  `maven-publish`
  signing

  kotlin("jvm") version "1.5.31"
  id("org.jetbrains.dokka") version "1.5.31"

  id("net.minecrell.licenser") version "0.4.1"
  id("org.jmailen.kotlinter") version "3.3.0"
}


group = "com.origins-digital"
version = "1.1.1"
description = "A Kotlin/Java API for generating .swift source files."

val isSnapshot = "$version".endsWith("SNAPSHOT")


//
// DEPENDENCIES
//

// Versions




repositories {
  mavenCentral()
  jcenter()
}

dependencies {

  //
  // LANGUAGES
  //

  // kotlin
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  //
  // MISCELLANEOUS
  //

  implementation("com.google.guava:guava:31.0.1-jre")

  //
  // TESTING
  //

  // junit
  val junitJupiterVersion = "5.6.2"
  testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")

  testImplementation("org.hamcrest:hamcrest-all:1.3")

}


//
// COMPILE
//

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8

  withSourcesJar()
  withJavadocJar()
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
    }
  }
}


//
// TEST
//

tasks {
  test {
    useJUnitPlatform()
  }
}


//
// DOCS
//

tasks {
  dokkaHtml {
    outputDirectory.set(file("$buildDir/javadoc/${project.version}"))
  }

  javadoc {
    dependsOn(dokkaHtml)
  }
}


//
// CHECKS
//

kotlinter {
  indentSize = 2
}

license {
  header = file("HEADER.txt")
  include("**/*.kt")
}


//
// PUBLISHING
//

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      artifactId = "swiftpoet"
      from(components["java"])
    }
  }

  repositories {
    maven {
      url = uri("https://artifactory-blr.netcodev.com/artifactory/libs-release-local")

      credentials {
        username = project.findProperty("repoUsername")?.toString()
        password = project.findProperty("repoPassword")?.toString()
      }
    }
  }
}



