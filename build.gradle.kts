plugins {
    id("java")
}

group = "org.pom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val serenityCoreVersion = "4.1.20"
val cucumberVersion    = "7.15.0"

dependencies {
    testImplementation("net.serenity-bdd:serenity-core:$serenityCoreVersion")
    testImplementation("net.serenity-bdd:serenity-cucumber:$serenityCoreVersion")
    testImplementation("net.serenity-bdd:serenity-screenplay-webdriver:$serenityCoreVersion")

    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")

    testImplementation("junit:junit:4.13.2")

    testImplementation("org.seleniumhq.selenium:selenium-java:4.18.1")

    testImplementation("io.github.bonigarcia:webdrivermanager:5.8.0")

    testImplementation("org.assertj:assertj-core:3.25.3")

    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.12")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnit()

    systemProperties(System.getProperties().toMap() as Map<String, Any>)

    outputs.dir("target/site/serenity")
}

tasks.register<JavaExec>("aggregate") {
    group = "Serenity BDD"
    description = "Re-agrega los resultados JSON de Serenity en un reporte HTML"

    mainClass.set("net.thucydides.core.reports.html.HtmlAggregateStoryReporter")
    classpath = configurations.testRuntimeClasspath.get()
    args = listOf(
        "--sourceDirectory", layout.buildDirectory.dir("../target/site/serenity").get().asFile.absolutePath,
        "--outputDirectory", layout.buildDirectory.dir("../target/site/serenity").get().asFile.absolutePath,
        "--projectName", rootProject.name
    )
    isIgnoreExitValue = true
}