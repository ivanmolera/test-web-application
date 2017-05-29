# test-web-application

java -cp test-web-application.jar SimpleHttpServer

# Web Server

I used com.sun.net.httpserver.HttpServer, Jackson, SQLite, Mustache and JUnit to build and test this simple HTTP server that runs in your IP address over port 8000.


# Users

There are four already created users with different roles:

Username | Password	| Role | Access to
------------------------------------------------
user01 | 1111  | PAGE_1 | /schibsted/page/one.html

user02 | 2222  | PAGE_2 | /schibsted/page/two.html

user03 | 3333  | PAGE_3 | /schibsted/page/three.html

admin  | admin | ADMIN  | All


Every user has access to a single web page, except admin user who can access to all pages.

When you try to access to some page you will be asked to login.
Inside every page there is a link to log out.


# REST API

You can also access the REST API using Postman with Basic Authorization (same users/passwords).

## How to show user details:

GET http://192.168.1.37:8000/schibsted/user/user01
Response Body:
{
  "username": "user01",
  "roles": [
    "PAGE_1"
  ]
}

## How to add new user:

POST http://192.168.1.37:8000/schibsted/user/
Request Body:
{
  "username": "user04",
  "password": "4444",
  "roles": [
    "PAGE_1"
  ]
}

## How to update user:

PUT http://192.168.1.37:8000/schibsted/user/
Request Body:
{
  "username": "user04",
  "password": "5555",
  "roles": [
    "PAGE_1", "PAGE_2"
  ]
}

## How to delete users:

DELETE http://192.168.1.37:8000/schibsted/user/user04



