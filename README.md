
<img width="814" height="211" alt="image" src="https://github.com/user-attachments/assets/8529cd9f-e372-4b33-8fef-c973ac7ae3ff" />

# Titanium

Titanium is a command-line tool designed to easily patch and customize PaperMC server JAR files.

## Features

- **Change Server Name**: Modify the server name displayed in the server list by editing `spigot.yml` within the JAR.
- **Add Custom APIs**: Inject custom `.java` source files into the JAR to be compiled and used by other plugins or systems.

*Note: This project is currently under development. More features, such as performance optimization and patch application, are planned.*

## Usage

The primary command is `patch`, which takes a path to a PaperMC JAR file and applies various modifications based on the options provided.

### Basic Syntax
```bash
./gradlew :cli:run --args="patch <path-to-jar-file> [options...]"
```

### Examples

#### Change the Server Name
This command will change the server name in the JAR's `spigot.yml` to "My Awesome Server".

```bash
./gradlew :cli:run --args="patch /path/to/paper-1.20.1.jar --new-name 'My Awesome Server'"
```

#### Add Custom API Files
This command will find all `.java` files in the `/path/to/my-api-sources` directory and add them to the `/API/` directory inside the JAR.

```bash
./gradlew :cli:run --args="patch /path/to/paper-1.20.1.jar --custom-api-dir /path/to/my-api-sources"
```

You can also combine options:
```bash
./gradlew :cli:run --args="patch /path/to/paper-1.20.1.jar --new-name 'My Custom Server' --custom-api-dir /path/to/api"
```

## For Developers

### Building the Project
This project uses a Gradle wrapper. To build all modules, simply run:
```bash
./gradlew build
```

The executable distribution (ZIP and TAR) will be created in `cli/build/distributions/`.

### Running Tests
To run the unit tests for all modules, execute:
```bash
./gradlew test
```
