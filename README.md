# Randomized Maps

* [Build](#build)
  * [Requirements](requiriments)
* [Run](#run)
* [Configuration](#configuration)

## Build
```sh
$ mvn clean install
```

### Requirements
 * maven
 * JDK 1.8

## Run
Either
```sh
$ mvn exec:java
```

or
```sh
$ java -jar target/fat.jar
```

## Configuration
Pass configuration on command line or directly to config.json `-Dconfig.google.key=myKey` == 
```
{
    "google": {
        "key": "myKey"
    }
}
```
and will be merged (rewrite existing) `config.json`

