# REST API Court reservation

## 1. Get source code

via HTTPS

````sh
git clone https://github.com/kuci-JK/inqool-java-spring-api-reservation.git
````

or SSH

````sh
git clone git@github.com:kuci-JK/inqool-java-spring-api-reservation.git
````

## 2. Build

Outputs to ./target in project root

```sh
./mvnw package
```

## 3. Run

Without data initialization:

```sh
java -jar target/inQool-tennis-reservation-api-0.0.1-SNAPSHOT.jar
```

With data initialization (creates 2 court surfaces and 4 courts on startup):

```sh
java -jar target/inQool-tennis-reservation-api-0.0.1-SNAPSHOT.jar data-init
```

## 4. Connect

Api is available at `http://localhost:8080/api`

- surfaces: `http://localhost:8080/api/surfaces`
- courts: `http://localhost:8080/api/courts`
- reservations: `http://localhost:8080/api/reservations`
