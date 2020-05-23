# Commands

## New commands
Build
`mvn package`

Run
`java -jar target/patientmonitoring-1.0-SNAPSHOT.jar`
or `mvn spring-boot:run -Dserver.port=9000`

Endpoints
`GET http://localhost:8080/api/v1/stats/daily/device:24:04:2019:14:00:00/2020-04-14/2020-04-19`

Test
`mvn test`

Clean
`mvn clean`

## Old commands
Compile to jar with all dependencies.

`mvn clean compile assembly:single`

Run

`java -jar target/patientmonitoring-1.0-SNAPSHOT.jar`

OR

`java -jar target/patientmonitoring-1.0-SNAPSHOT.jar ~/Downloads/myproj/config.template.properties`



# Pubnub js sdk
https://www.pubnub.com/docs/nodejs-javascript/pubnub-javascript-sdk

