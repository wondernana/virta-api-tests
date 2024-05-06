# Station API Tests

## Stack

- Java
- JUnit 5
- Rest-Assured
- Allure Report
- Owner

## Instructions

### To run all the tests use: ### 
``` mvn clean test ``` 

### To generate new report: ### 
- Install Allure Report, following [instructions](https://allurereport.org/docs/gettingstarted-installation/).
- Run ``` allure serve ```: this will launch allure server on your local machine and will open the report in the browser. 
- Alternatively, you can run ``` allure generate --single-file allure-results ```: this will generate the report in a single html file located in `allure-report` folder.