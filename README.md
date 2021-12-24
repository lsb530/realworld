# RealWorld API Spec
## Features
## General functionality:

* Authenticate users via JWT (login/signup pages + logout button on settings page)
* CRU* users (sign up & settings page - no deleting required)
* CRUD Articles
* CR*D Comments on articles (no updating required)
* GET and display paginated lists of articles
* Favorite articles
* Follow other users
* Global Error Response

## Endpoints
[https://gothinkster.github.io/realworld/docs/specs/backend-specs/endpoints](!https://gothinkster.github.io/realworld/docs/specs/backend-specs/endpoints)

[https://realworld-docs.netlify.app/docs/specs/backend-specs/endpoints](!https://realworld-docs.netlify.app/docs/specs/backend-specs/endpoints)

## Running API tests locally

To locally run the provided Postman collection against your backend, execute:

```
APIURL=http://localhost:${port}/api 
$ ./run-api-tests.sh
```

For more details, see [`run-api-tests.sh`](../main/java/com/boki/realworld/run-api-tests.sh).

## Test
```
$ ./gradlew test
```

## Database
![diagram](src/docs/diagram.png?raw=true)

## Postman
![postman](src/docs/postman.png?raw=true)

## Swagger 3.0 With Authorize
please input bearer ${TOKEN}

![swagger](src/docs/swagger.png?raw=true)

## License
All of the codebases are MIT licensed unless otherwise specified.
[![Brought to you by Thinkster](https://gothinkster.github.io/realworld/assets/images/end-eb4086a6a065b1b9290505f1c85a0e1c.png)](https://thinkster.io)
