# Open311 Java wrapper (Jreport) [![Build Status](https://travis-ci.org/codeforeindhoven/open311_java.png)](https://travis-ci.org/codeforeindhoven/open311_java)

This is a Java language binding (wrapper) to the Open311 GeoReport v2 REST API (current version: `0.0.4`). You can check the [library site](http://codeforamerica.github.io/open311_java/) if you want to check the javadocs, test coverage, etc.

The main features are:

+ Caching
+ Logging
+ Admits endpoints whose CA is not recognized by Java
+ Compatible with Android (transparently, it selects the correct components from your execution environment)
+ Compatible with some non-standard GeoReport servers.

If you find any problem or miss any feature, just open an [issue](https://github.com/codeforamerica/open311_java/issues?state=open).

If you are interested about new features or the development process please check this [blog](http://santimunin.blogspot.com.es/search?q=open311).

## How to get the library

### Add it as a dependency
The easiest way to start using the library is to add the dependency to your project dependency manager system. The library is currently avaliable in the [Maven central repository](http://search.maven.org/#artifactdetails%7Corg.codeforamerica.open311%7Cjreport%7C0.0.4%7Cjar), which makes it compatible with tools such as:

+ [Maven](http://maven.apache.org/)
+ [Sbt](http://www.scala-sbt.org/)
+ [Gradle](http://www.gradle.org/)
+ ...

Below, an example using Maven.

```xml
<dependency>
    <groupId>org.codeforamerica.open311</groupId>
    <artifactId>jreport</artifactId>
    <version>0.0.4</version>
</dependency>
```

### Download it
There are many links in the [Maven central repository page](http://search.maven.org/#artifactdetails%7Corg.codeforamerica.open311%7Cjreport%7C0.0.4%7Cjar), and you can find the `.jar` file (with its sources and javadocs). Please, note that this `.jar` does not contains its [dependencies](http://codeforamerica.github.io/open311_java/dependencies.html), so it will not work unless you attach them too.

### Compile it

In order to compile and test this project you should have [Maven](http://maven.apache.org/) installed in your system.

```bash
# Clone this repository
git clone https://github.com/codeforamerica/open311_java.git
cd open311_java

# The following commands are independent. Execute which you need (probably the last one).
# Compile the project (and download dependencies)
mvn compile

# Execute tests
mvn test

# Execute tests with cobertura analysis
mvn cobertura:cobertura

# Generate the .jar without dependencies
mvn package

# Generate the .jar with dependencies
mvn assembly:assembly
```

You may have problems if you are using an old version of `git` since one of the executed commands is `git clone --recursive` and it doesn't work with its oldest versions. Please, update your `git` in that case.

#### Locations

 + `mvn cobertura:cobertura` will write its output in `target/site/cobertura/`, open the `index.html` file to check it.
 + `mvn package` will output the `target/jreport-{version}.jar` file.
 + `mvn assembly:assembly` will output the `target/jreport-{version}-jar-with-dependencies.jar` file.


## Usage

### Build a wrapper

You will need the [APIWrapperFactory](http://codeforamerica.github.io/open311_java/apidocs/org/codeforamerica/open311/facade/APIWrapperFactory.html) in order to build the [APIWrapper](http://codeforamerica.github.io/open311_java/apidocs/org/codeforamerica/open311/facade/APIWrapper.html) (which will allow you to query the endpoint). There are two different ways of creating the factory:
 + Specifying the desired **city** and **type** of the endpoint (production, test...). The wrapper will get the most suitable endpoint and data interchange format for you.
 + Specifying the **url of endpoint**, the **jurisdiction_id** parameter and the **data interchange format**. It is your responsibility to check if the format you desire is supported.

```java
// From the city (EndpointType.PRODUCTION is selected by default)
APIWrapper wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).build();

// From the city, test endpoint.
APIWrapper wrapper = new APIWrapperFactory(City.SAN_FRANCISCO, EndpointType.TEST).build();

// From the url of the endpoint
APIWrapper wrapper = new APIWrapperFactory("https://www.myendpointurl.com/").build();

// From the url of the endpoint, the jurisdiction_id, and the data interchange format
APIWrapper wrapper = new APIWrapperFactory("https://www.myendpointurl.com/",
  "myjurisdictionId", Format.JSON).build();

// With the api key
wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).setApiKey("your api key").build();

// Without caching data and the api key
wrapper = new APIWrapperFactory(City.SAN_FRANCISCO).setCache(new NoCache()).
  setApiKey("your api key").build();
```

Check all the possible parameters and constructors of the `APIWrapperFactory` in its [documentation page](http://codeforamerica.github.io/open311_java/apidocs/org/codeforamerica/open311/facade/APIWrapperFactory.html).


### Invoke operations
```java
// GET service list
List<Service> listOfServices = wrapper.getServiceList();

// GET service definition
ServiceDefinition definition = wrapper.getServiceDefinition("serviceCode");

// POST service request
POSTServiceRequestResponse response = wrapper.postServiceRequest(
  new POSTServiceRequestData("serviceCode", addressId, listOfattributes));

// GET service_request_id from a token
ServiceRequestIdResponse serviceRequestIdresponse =
  wrapper.getServiceRequestIdFromToken("token");

// GET service requests
List<ServiceRequest> serviceRequests = wrapper.getServiceRequests(
  new GETServiceRequestsFilter().setStatus(Status.OPEN));

// GET service request 
ServiceRequest serviceRequest = wrapper.getServiceRequest("serviceRequestId");
```

It is worth it to check the [documentation](http://codeforamerica.github.io/open311_java/apidocs/index.html) and find all the possible parameters of the `GETServiceRequestFilter` and `POSTServiceRequestData` classes.
 
## Useful information

 + [Javadocs](http://codeforamerica.github.io/open311_java/apidocs/index.html)
 + [Cobertura report (code coverage)](http://codeforamerica.github.io/open311_java/cobertura/index.html)
 + [Team](http://codeforamerica.github.io/open311_java/team-list.html)
 + [Dependencies](http://codeforamerica.github.io/open311_java/dependencies.html)
 + [Continuous Integration System](https://travis-ci.org/codeforamerica/open311_java)

## Caching
This library tries to save some responses for a certain time in order to avoid expensive network operations.
 + In a regular Java application, it is activated by default.
 + If you do not want to cache anything: `factory = new APIWrapperFactory().setCache(new NoCache());`
 + Using an Android app: `factory = new APIWrapperFactory().setCache(AndroidCache.getInstance(getApplicationContext()));`
 + Using a special platform which doesn't allow to create or write to files: Extend the [AbstractCache](http://codeforamerica.github.io/open311_java/apidocs/org/codeforamerica/open311/internals/caching/AbstractCache.html) class and `factory = new APIWrapperFactory().setCache(new YourCacheImplementation());`

In case you want to delete the cache:
 + Use your wrapper's cache: `wrapper.getCache().delete()`
 + Get your platform's cache:
    * `RegularJavaCache.getInstance().delete()`
    * `AndroidCache.getInstance(getApplicationContext()).delete()`

Please, note that when you delete the cache you will delete **ALL** the cached data.
## SSL certificates
Some of the endpoints could have SSL certificates which signature won't be recognize by Java. Up to now, the HTTP client used by this library ignores those certificate signature problems so it is your responsibility to make sure that you are providing a secure url.
