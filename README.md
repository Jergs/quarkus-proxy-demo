# quarkus-rest-proxy

This project uses Quarkus, the Supersonic Subatomic Java Framework.

This project operates as a "proxy" that will return the modified text of https://quarkus.io or any other domain.
- To each word, which consists of six letters, it will add the "™" symbol.
- The functionality of the original site is not altered (JS, including 3rd party JS; CSS; Images and any additional 
content is present and in working order).
- All internal navigation links of the site are replaced by the address of the proxy-server.

That is, site navigation is handled by a Proxy without taking the client back to the original site.

Example. A request to, say, `{proxy address}/quarkus3/` will show the content of the page
https://quarkus.io/quarkus3/ with changed words that were 6 characters long.
And all the site navigation to sections of the site go through Proxy.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

After building and running the project, go to http://localhost:8080.

## Running the application in dev mode

You can run the application in dev mode using:

```shell script
./gradlew quarkusDev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating Docker image and running application in Docker container

The application can be packed into a Docker image using:

```shell script
docker build -f src/main/docker/Dockerfile -t quarkus-gradle-app .
```

The application is now runnable using `docker run -i --rm -p 8080:8080 quarkus-gradle-app`.
You can specify the remote website you want to test (for example `quarkus.io`)
by passing the env parameter to docker container like this:

```shell script
docker run -e REMOTE_BASE_URI="quarkus.io" -i --rm -p 8080:8080 quarkus-gradle-app
```
Here `REMOTE_BASE_URI="quarkus.io"` is the desired website to proxy.

If you want to use docker-compose, execute the following command:

```shell script
docker-compose -f src/main/docker/docker-compose.yml up -d 
```

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/quarkus-rest-proxy-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.
