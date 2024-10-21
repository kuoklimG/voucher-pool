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
   git clone https://github.com/your-username/voucher-pool-system.git
   cd voucher-pool-system
   ```

2. Ensure MongoDB is running on your system. By default, the application will try to connect to MongoDB at `mongodb://localhost:27017/voucherpool`. 
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

The application comes with a `DataLoader` that populates initial data for recipients and special offers. This data is loaded automatically when the application starts if the database is empty. You can find a list of the initial data in the `initial_data.txt` file.

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