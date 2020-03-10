# MW Fluance E-Health Cockpit Application 

Packaged as MW-APP.war(or .jar), MW Fluance Cockpit application is the main MW application of the Fluance EH Cockpit Solution.

It's composed by  4 modules. fluance-cockpit-app, fluance-cockpit-core, fluance-cockpit-solr, and fluance-cockpit-synlab.

* fluance-cockpit-app : contains the Controller, and the service layers.
* fluance-cockpit-core : contains the Data Layer (JPA/JDBC repositories, and Models)   
* fluance-cockpit-solr : a Solr Client Service
* fluance-cockpit-synlab : Integration Service with Synlab (Using SOAP)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

Get the Project from a Git repository and build it: 
```
git pull https://bitbucket.org/fluancedev/mw-app.git
cd mw-app/
mvn clean install -DskipTests
```
Now you can Deploy the war in a tomcat8 or just run it as a spring boot application using STS (Spring tool Suite)

### Prerequisites

* JAVA 8
* Maven
* Access to Fluance Artifactory to load all the necessary packages.
* Spring tools Suite is recommended as IDE. 

### Installing

A step by step series of examples that tell you how to get a development env running

Build the project

```
mvn clean install -DskipTests
```

And repeat

```
until finished
```

MW-APP is using a spring config server to load the right properties with the desired profile. You must set the parameters of the config server in bootstrap.properties or as VM Argument
End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc


