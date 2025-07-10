# Elixir Finder CLI (Quarkus Native)

A command-line application built with Quarkus and Picocli that fetches elixir data from the Wizard World API and displays which potions can be brewed from user-provided ingredients.

---

## Features

* Interactive prompt (type `exit` to quit)
* One-shot mode with `--ingredients` argument
* Native executable via GraalVM
* Docker image for containerized native deployment
* Formatted ASCII output with rows: Elixir's Name, Effect and Ingredients

---

## Prerequisites

Make sure you have the following installed:

| Tool                          | Minimum Version | Installation/Download                                                                         |
|-------------------------------|-----------------|-----------------------------------------------------------------------------------------------|
| Java SE Development Kit (JDK) | 21              | [Oracle JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) |
| Apache Maven                  | 3.8.0+          | [Apache Maven](https://maven.apache.org/download.cgi)                                         |
| Docker (for container builds) | 20.10+          | [Docker](https://docs.docker.com/get-docker/)                                                 |

> **Notes:** 
> 1. [GraalVM](https://www.graalvm.org/) could also be installed, and you may include the Native Image component: run `gu install native-image` after GraalVM installation.
> 2. You can also use the maven wrapper executable present in this project instead of using mvn. 

---

## Project Structure

```
nitro
├── wizard-world/
      ├── pom.xml
      ├── src/
      │   ├── main/
      │   │   ├── java/org/nitro/
      │   │   │   ├── Main.java
      │   │   │   ├── cli/ElixirFinderCommand.java
      │   │   │   ├── cache/{WizardWorldCache.java, WizardWorldCacheDefault.java}
      │   │   │   ├── service/{ElixirService.java, ElixirServiceDefault.java}
      │   │   │   ├── client/WizardWorldClient.java
      │   │   │   └── model/{Elixir.java, Ingredient.java}
      │   │   └── resources/application.properties
├── pom.xml
├── .mvn # folder with the needed tools to the ./mvnw executable
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## Quick Start

### 1. Clone repository

```bash
git clone https://your-repo-url/elixir-finder.git
cd elixir-finder
```

### 2. Build JVM JAR

```bash
mvn clean package
```

#### Run interactive prompt (JAR)

```bash
java -jar wizard-world/target/quarkus-app/quarkus-run.jar
```

#### Run one-shot mode

```bash
java -jar wizard-world/target/quarkus-app/quarkus-run.jar --ingredients="MandrakeRoot,Gillyweed"
```

---

## Native Executable (GraalVM)

Quarkus supports native builds using Docker, so you do not need to install GraalVM manually.

#### Package native binary and docker image:
```
mvn clean package -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
```

> **Windows Users:**
> - To build natively on Windows, you may need to install the [Visual Studio Build Tools](https://visualstudio.microsoft.com/downloads/#build-tools-for-visual-studio-2022) (C++ workload).
> - Or use container mode (Docker) to avoid installing a native toolchain:
>   ```bash
>   .\mvnw clean package -Pnative \
>     -Dquarkus.native.container-build=true \
>     -Dquarkus.container-image.build=true
>   ```

#### Run native executable
* Guarantee that the below file is indeed an executable, if not make it (i.e `chmod +x <file-name>`)
   ```
   wizard-world/target/wizard-world-1.0.0-SNAPSHOT-runner
   ```

#### Run docker container from created native image
```
docker run -it nitro/wizard-world:1.0.0-SNAPSHOT
```

---
