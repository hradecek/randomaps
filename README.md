![All tests](https://github.com/hradecek/randomaps/workflows/All%20tests/badge.svg)
# Randomized Maps

* [Build](#build)
  * [Requirements](requirements)
* [Run](#run)
* [Configuration](#configuration)

## Build
```sh
$ mvn clean install
```

### Requirements
 * maven
 * JDK 11

## Run
Either
```sh
$ mvn exec:java
```

or
```sh
$ java -jar target/*fat.jar
```

## Configuration

### Google Maps API key
In order to generate random maps, Google Maps API key must be provided.

Google Maps API key might be passed as:

 - environment variable `GOOGLE_API_KEY=myKey`
 - system variable `-Dgoogle.api.key=myKey`

or as part of `config.json` configuration:
```
{
    "google": {
        "key": "myKey"
    }
}
```

