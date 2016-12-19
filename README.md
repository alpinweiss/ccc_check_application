# README #

This README would normally document whatever steps are necessary to get your application up and running.

### Setup ###

* Install Java 8
* Install Maven 3+
* Retrieve all necessary dependencies using Maven

### Run ###
* Run app from IDE as common Java application, main class: eu.alpinweiss.ccccheck.CccChechkApplication
* Or possible to run app from command line using Spring Boot Maven plugin: mvn spring-boot:run
* Open url: http://localhost:8080

### Generate reports ###
* Run from command line: mvn site -DgenerateReports=false surefire-report:report
* Open target/site/surefire-report.html to view test report
