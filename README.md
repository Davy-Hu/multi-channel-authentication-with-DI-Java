# Multi-Channel Authentication using Java and MySQL

## Introduction

This tutorial serves as a demo for using multi-channel authentication in the java environment. This documentation contains both a detailed design of the process and implementation details.

## Programming Languages and Tools

Language: Java. While the task of authentication can be performed by any language, here, we will use Java as the main programming language. In particular, we will be creating a Maven Project with Spring Boot to manage our program.

Database: MySQL. To store data in a database, I will be using MySQL. MySQL can be installed below. MySQL will be used to store the data of all users. After installing, follow the installation instructions to set up MySQL for your machine.

[https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)

IDE: Eclipse. Eclipse will be used as an IDE to code in. It is very convenient for coding in java and allows for easier development. Eclipse can be downloaded below.

[https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/)

Web Container: Apache Tomcat.

[https://tomcat.apache.org/tomcat-8.5-doc/setup.html](https://tomcat.apache.org/tomcat-8.5-doc/setup.html)

## Design

### Authentication

The authentication process starts when the user wishes to confirm their identity. Most of the time, this is done in three ways listed below:

1. Biological
2. Token
3. Username/Password

For the purposes of this demo, I will only focus on the username/password method of authentication. The design behind this idea is that the user will send their information to the server, which will then check with the database whether this information is valid or not, and will return the appropriate response.

### Database

As previously mentioned, the database that will be used is MySQL. Below is the schema that the database will follow. We will only need one table for this demo as the sole focus is on the authentication process.

- Email: String
- Password: String
- First Name: String
- Last Name: String

Of the above, only email and password are mandatory. The others are optional and will only be there to serve as more concrete material.

It is important that before storing passwords into the database that they are encrypted. We will be using BCrypt&#39;s one-way hash as our encryptor. Because this method is one-way, if a user forgets their password, they will have to reset it rather than retrieve it.

Salt is optional but mandatory for OAuth which is used to store the returned token from the third party

### Dependency Injection

We will be using dependency injection to dynamically inject a channel to use when the user wants to authenticate themselves. In Java, this requires four main parts:

#### Interface

We will need an interface in order to create a general baseline for what the classes will look like. Because these classes will inherit from this interface, the interface will need to have all methods that the classes will need. For the purposes of this design, the only method needed is the authenticate method, which will be used to authenticate the user. Below is the structure of the interface.
```
public interface IAuthentiate {

    String authenticate(UserCredential user);

    boolean verifyUser(String code) throws IOException;

}
```
#### Classes

The authentication classes that will be used to authenticate users will all inherit from the Authentication interface. They will all follow a similar structure dictated by the interface, but will have their own minute differences due to the difference in implementation. These classes will be:

- MySQLAuthenticate
- GoogleAuthenticate
- FacebookAuthenticate

We will also need to create a class called UserCredential. This user credential class will be a basic class that will hold an email and password if required. The reason this class is created is so that it follows the interface above and can be passed into any authentication method. In the case of google and facebook, UserCredential will just be false.

#### Injector

For the injector, we will first of all need a factory to generate our authentication methods. To do that, we will use a Map to contain each of our four methods. Rather than creating a map in the class, we will store the classes in a properties file, which will then be loaded in and cached.

#### Client

When the user would like to authenticate themselves, they will be asked to choose an authentication channel: local database, third party OAuth such as Google or Facebook, or WindowsAuto which will be done automatically. Because all of the classes inherit from the IAuthenticate Interface and all implement the authenticate() method, the authentication logic will be different depending on which channel is selected by the user.

## Implementation

Firstly, note that all specific implementation details can be found in the github repository.

### Database

We will start our implantation with the database. After installing MySQL and following the installation instructions, there should be a new application called MySQL Workbench on your machine. Open this application. On the homepage, there should be a section called MySQL Connections. Select the local instance of MySQL and enter the password that you set up during configuration. You can follow the official documentation found below for instructions on how to create a database and table.

[https://dev.mysql.com/doc/refman/8.0/en/tutorial.html](https://dev.mysql.com/doc/refman/8.0/en/tutorial.html)

For our purposes, we will name the database multichannelauth and create a table called users.

### Create Maven Project

We will be creating a Maven project with spring boot. To do this, follow the instructions below:

[https://www.toolsqa.com/java/maven/create-new-maven-project-eclipse/](https://www.toolsqa.com/java/maven/create-new-maven-project-eclipse/)

Upon creating the project, there will be a default, blank project. Open the file called pom.xml. This file contains all the details about the project including the dependencies that the project relies on. You will be able to find a complete version of the pom.xml file in the repository.

### Create Project Packages

Let&#39;s take some time to generate the structure of our project. We don&#39;t want all of our classes in the same location, as that would make things messy and disorganized. To do this, we will create packages to organize our directory. In src/main/java there should be a default package created for you with the file App.java. In this package, create three more packages called authentication, user, and util. The authentication package will contain all of our authentication classes. The user package will contain all classes related to users, and the util package will be for all utility.

### App.java

Now that we have created out project, there should be a file called App.java. This file is responsible for running the entire project. Check to ensure that your default App.java file is configured properly and is the same as the one provided in the repository.

### Properties File

We will use a properties file to store all the properties that we will want to import into out project. Create a new file called application.properties in src/main/resources if it is not already created for you. A complete version of the properties file can be found in the repository. Some of the values in the properties file have demo values placed in them. You will need to replace those with your own. The values you will need to change will be highlighted below.

- authenticationclasses: replace with the names of your classes
- dbURL: replace with the destination of your database
- dbUser: replace with your db username
- dbPassword: replace with your db password
- googleId: replace with your google id
- googleSecret: replace with your google secret
- facebookId: replace with your facebook id
- facebookSecret: replace with your facebook password

### Resource Loader File

Now that we&#39;ve created a properties file, we want to be able to read the file and extract information. Create a file called ResrouceLoader.java in /src/main/java/com/bill/security/util. The basic intuition for this would be to create a bunch of getter and setter methods to get and set each of the properties, however, this is very inefficient for a project that can have many properties. Therefore, another intuition is to create a map that stores all the keys and values, but this is still inefficient if there are many properties in the file. Thus, we want to create a solution that will read the properties file, and load the properties from the file the same way no matter how many elements there are. We will still use a map to store the keys and values, but when we load the properties, we will loop though the file to get all key value pairs.

### Create User Classes and Models

#### User Class

First, we&#39;ll need to create a user class that wills serve as a model. Create a file called User.java in /src/main/java/com/bill/security/user The class will have a field for every field in the database as well as an extra one for the id of the user to store in the database. It is important to note that we will be using JPA as a relational database to communicate with the MySQL database.

#### UserRepository Class

The UserRepository Class will be the class that actually extends JpaRepository and communicate with the database. In the same package as the User class we just defined, make an interface called UserRepository.java. We can add some queries that we need to communicate with the database if needed in this class.

#### UserCredential Class

We will use the UserCredential Class as described in the design. Create one more file in the user package called UserCredential.java.

### Create Authentication Classes

#### Interface

We will start by creating an interface for all of the authentication classes to follow as mentioned in the design at the beginning. We will put this file along with all the authentication classes in the authentication package. The complete interface can be found at /src/main/java/com/bill/security/authentication/IAuthenticate.java

#### MySQL

The first authentication method that we will be implementing. Create a file called MySQLAuthenticate in /src/main/java/com/bill/security/authentication and make sure that it implements the IAuthenticate interface and overrides its methods. This class will not be using the verify user method, but it still needs to be overridden in order for the class to compile. The authentication logic we will use is that we will connect to the database, and query for the email provided. If this email does not exist, we will fail to authenticate the user. If the email does exist, then we encrypt the provided password and check if it is a valid version of the one in the database.

#### Google

For google authenticate, we will be using Google&#39;s OAuth2 Playground to authenticate our users. If you haven&#39;t already done so, configure google credentials by following these instructions:

[https://developers.google.com/api-client-library/java/google-api-java-client/oauth2](https://developers.google.com/api-client-library/java/google-api-java-client/oauth2)

Once you get your google id and google secret, edit the contents of application.properties.

Then create a file called GoogleAuthenticate in /src/main/java/com/bill/security/authentication.

In order to authenticate a user with google, we must first send a request to googles servers passing in the following fields:

- scope
- response\_type
- state
- redirect\_uri
- client\_id

Google will then authenticate the user and redirect them to the url defined by the user. Within this redirect will be a request parameter called code. This code is required to retrieve an id\_token from google&#39;s servers. We can then exchange this token with the one stored at google and retrieve a json body with the users data. In this case, we will be using this data to ensure that the user is already in our database before allowing them to access our services.

#### Facebook

Facebook authentication follows a similar process to google. We will be using Facebook&#39;s developer tools to get some credentials. Look at facebook&#39;s official documentation on how to create credentials if you haven&#39;t already.

[https://developers.facebook.com/docs/development/](https://developers.facebook.com/docs/development/)

Like with google, edit the contents of application.properties after creating your credentials.

Then create a file called FacebookAuthenticate in /src/main/java/com/bill/security/authentication.

Just like with Google, we will need to send a request to facebooks servers to get an access\_token. Facebook is a little easier in that it only requires 4 parameters:

- client\_id
- redirect\_uri
- scope

Like with google, we will get a code which we can use to get an access\_token. This access token can then be used to retrieve the users data.

### App Controller

With most of the backend login done, it&#39;s time to connect the logic to physical endpoints that the user can use. To do this, we will create a class classed AppController in /src/main/java/com/bill/security/util. We will be using this file to configure all the routes that will be used in the application. We will be using path variable indicated by {variable\_name} to indicate when the url path can be any value. This is very useful for our authentication network as it will allow us to use dependency injection, which is described in the design at the beginning.

### Authenticate Factory &amp; Util Class

In order to use Dependency injection, we will need to make a Factory that will generate the different authentication methods for us. Create a class called AuthenticationFactory in /src/main/java/com/bill/security/util.

The first intuition to solve this problem is to check what the passed in parameter is and to return a new instance of the authentication method class. However, what we will do is that we will load the authentication method class path from the properties file. Since the properties file only has the class paths, we will need another utility class to help retrieve the key for our values.

While in this case we will only use this utility class for authentication, other projects may have other uses for this. Thus, we will make this a general utility class for loading data. Create a class called DataUtil.java in /src/main/java/com/bill/security/util. This DataUtil class will contain a map that will have the authentication class path as a key and name as value. We can then use the class path to retrieve the name and put that as our key in the factory.

### Spring Boot Configurations

The last two classes that we will implement will be some simple configurations to out Spring Boot application that will allow it to run. Create a class called MvcConfig.java in /src/main/java/com/bill/security/util. This class will implement the WebMvcConfigurer interface which is responsible to setting up Spring Boot projects. In this class, we will override the addViewControllers method and specify our own login page.

Next, create a class called WebSecurityConfig.java in /src/main/java/com/bill/security/util. Just like the class we just created, this class will be used to help configure our Spring Boot application, this time focusing on the security aspect.

### HTML pages

The final step to complete our application is to set up pages that the user can actually view. Our controller will read directly from the /src/main/resources/templates folder for html files to render.
