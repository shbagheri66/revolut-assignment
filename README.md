# Revolut test task : Money transfer Rest API

A Java RESTful API for transfer money between accounts

### Run project
enter in command line : 
mvn exec:java
or
Run Application.java or fat jar

Application will start a jetty server on localhost port 8080 

### Rest Services

| HTTP METHOD | URL | DESCRIPTION |
| -----------| ------ | ------ |
| GET | /account/getall | get all available accounts | 
| GET | /account/{accountId} | get an account by id | 
| PUT | /account/add | create a new account | 
| DELETE | /account/{accountId} | remove account from accounts list | 
| POST | /account/transfer/{sourceAccountId}/{destinationAccountId}/{amount} | transfer money from source to destination | 

### Http Status
- 200 OK: The request was succeeded
- 400 Bad Request: The request input parameters are not valid
- 406 Not Acceptable: The requested is not acceptable
- 500 Internal Server Error: The server encountered an unexpected error 

### Sample JSON
##### input account to save : 
```sh
{
  "id":1000,
  "ownerName":"Shahab",
  "balance":5000
}
```
##### sample response : 

```sh
{
    "result": {
        "id": 1000,
        "ownerName": "Shahab",
        "balance": 5000,
        "creationDate": 1547580090691
    },
    "error": null,
    "success": true
}
```
