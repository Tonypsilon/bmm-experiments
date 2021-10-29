# bmm

Webapp for the organization of Berlin chess team championships

### Development Environment Setup

Create database.properties from database.properties.sample and fill in connection details.


### basedata and resultdata

Basedata contains all entities that are set before a season starts.
Resultdata contains all entities that are created throughout a season.

# news

Uses github actions for automated tests.

# Container image creation

Spring Boots Maven plugin comes with a target to create container images.
The image will have the name of the project - bmm - and the tag will be the version number.

```bash
mvn spring-boot:build-image
```
