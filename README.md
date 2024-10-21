# Voucher Pool System

This project implements a Voucher Pool system using Spring Boot, allowing for the generation, validation, and management of voucher codes.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [Initial Data](#initial-data)
- [Configuration](#configuation)
- [API Endpoints](#api-endpoints)

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MongoDB 4.4 or higher
- An IDE of your choice (e.g., IntelliJ IDEA, Eclipse)

## Setup

1. Clone the repository:
   ```
   git clone https://github.com/kuoklimG/voucher-pool.git
   cd voucher-pool
   ```

2. Ensure MongoDB is running on your system. By default, the application will try to connect to MongoDB at `mongodb://localhost:27017/voucher_pool`. 
   If your MongoDB setup is different, you'll need to update the connection string in `application.properties`.

3. Build the project:
   ```
   mvn clean install
   ```


## Running the Application

1. Make sure MongoDB is running.

2. Start the application:
   ```
   mvn spring-boot:run
   ```

3. The application will start on `http://localhost:8080`

## Configuration

The application uses the following default MongoDB configuration:

- Host: localhost
- Port: 27017
- Database: voucherpool

## Initial Data

When the application starts for the first time, it automatically loads some sample data into the database. This includes a set of recipients and special offers. Here's a list of the initial data:

### Recipients

| Email             | Name          |
|-------------------|---------------|
| john@example.com  | John Doe      |
| jane@example.com  | Jane Smith    |
| bob@example.com   | Bob Johnson   |

### Special Offers

| Name              | Discount (%) |
|-------------------|--------------|
| Summer Sale       | 20.0         |
| Winter Discount   | 15.0         |
| Spring Promotion  | 10.0         |

This initial data is loaded to provide a starting point for testing and development. You can modify or add to this data as needed for your use case.

### Note on Data Loading

When you run the application for the first time, it should automatically load this initial sample data into the MongoDB database. However, in some cases, you might not see this data immediately in your MongoDB viewer. If this occurs, please try the following steps:

1. Ensure the application has fully started and there are no errors in the console logs.
2. Check the application logs for messages indicating that sample data has been loaded.
3. If you're using a MongoDB viewer or GUI tool, try refreshing the database connection.
4. If the data still doesn't appear, restart your MongoDB viewer or reconnect to the database.
5. As a last resort, you can try stopping the application, clearing the database, and then restarting the application.

If you continue to experience issues with data loading, please check the application logs for any error messages and ensure your MongoDB connection settings are correct.

## API Endpoints

### 1. Generate a voucher

- Method: POST
- URL: `http://localhost:8080/api/vouchers/generate`
- Params (Query Params in Postman):
  - `email`: The recipient's email address
  - `specialOffer`: The name of the special offer
  - `expirationDate`: The expiration date of the voucher (format: YYYY-MM-DD)

Example in Postman:
1. Set the method to POST
2. Enter the URL: `http://localhost:8080/api/vouchers/generate`
3. In the "Params" tab, add:
   - Key: `email`, Value: `john@example.com`
   - Key: `specialOffer`, Value: `Summer Sale`
   - Key: `expirationDate`, Value: `2023-12-31`
4. Click "Send"

Expected response:
{
"code": "ABCD1234"
}

### 2. Validate a voucher

- Method: POST
- URL: `http://localhost:8080/api/vouchers/validate`
- Params (Query Params in Postman):
  - `code`: The voucher code to validate
  - `email`: The email of the recipient trying to use the voucher

Example in Postman:
1. Set the method to POST
2. Enter the URL: `http://localhost:8080/api/vouchers/validate`
3. In the "Params" tab, add:
   - Key: `code`, Value: `ABCD1234`
   - Key: `email`, Value: `john@example.com`
4. Click "Send"

Expected response:
{
"discount": 20.0,
"offerName": "Summer Sale",
"expirationDate": "2023-12-31",
"usageDate": "2023-06-15T14:30:00"
}

### 3. Get valid vouchers for a user

- Method: GET
- URL: `http://localhost:8080/api/vouchers/valid`
- Params (Query Params in Postman):
  - `email`: The email of the recipient

Example in Postman:
1. Set the method to GET
2. Enter the URL: `http://localhost:8080/api/vouchers/valid`
3. In the "Params" tab, add:
   - Key: `email`, Value: `john@example.com`
4. Click "Send"

Expected response:
{
"vouchers": [
"EFGH5678 - Winter Discount",
"IJKL9012 - Spring Promotion"
]
}

Note: These examples assume the application is running on localhost:8080. Adjust the URL if your setup is different.
