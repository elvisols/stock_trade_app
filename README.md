# Getting Started

This is a fully containerized stock trading application with the following features:

- let users pick a username and register for the app.
- let registered users log in.
- allow a user to look up a stock’s current price.
- enable a user to buy stocks. (With the current price of the stock)
- list all the current stocks that the user has purchased.
- allow a user to sell shares of a stock that he or she owns.
- summarise all of a user’s transactions ever or over a time period.

##### Application Requirements:
- Java 8
- Maven 3+
- Docker (optional)
- Internet Connection
- Port 8880

##### Application Run:
```
~$ mvn clean
```
```
~$ mvn package
```
```
~$ mvn spring-boot:run
```

> To Build docker image (optional):
```
~$ mvn clean package docker:build
```

Once the service is up, the application documentation can be accessed, from the source folder, via

`./target/generated-docs/index.html`

It contains details about each endpoints.

### Reference Documentation
For further reference, how the webservice is consumed please visit the documentation home page

* [Official Stock Trade Service Documentation](./target/generated-docs/index.html)


###   --

