### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)
- Run the test by `mvn test`
Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

### JWT token related Notes for authentication 
 - Add the config package 
 - Add the Json token generation as Jwt token
 - Add the helpers package (which contains response and request helper)
 - You need to json token by Register and login from /auth/register and /auth/login
 - To make request to Employee Controller you have get provide Header "Authorization: Bearer <access token>"
 - All the Test are in Test folder

### Database Related Notes
 - Using the different database for Main application and Test
 - Please see on Resources for both main and test package

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### Your experience in Java

Please let us know more about your Java experience in a few sentences. For example:

- I have 7 years experience in Java
- I am also knows the reactive programming with Java and Spring boot  
- I know Spring Boot very well and have been using it for many years
- Also Have experience in micro-service build with Spring boot and Java
- Also have experience in Writing test with Junit and Mockito and swagger documentation
- Has some experience with Android development 
- Also have experience in GRPC Java


