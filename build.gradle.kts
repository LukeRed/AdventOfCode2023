plugins {
    id("java")
}

group = "eu.lukered.aoc2023"
version = "1.0-SNAPSHOT"
description = "Advent of Code 2023"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}