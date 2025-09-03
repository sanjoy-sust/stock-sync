# Stock Sync Service

A Spring Boot microservice for synchronizing product stock levels from two vendors, persisting data in a database, and detecting out-of-stock events.

## Features
- Periodically syncs product stock from:
    - **Vendor A**: Mock REST API (`/vendor-a/products`).
    - **Vendor B**: CSV file at `/tmp/vendor-b/stock.csv`.
- Persists product data in an H2 in-memory database.
- Logs out-of-stock events when a product's stock transitions to zero.
- Exposes a `GET /products` endpoint to retrieve current stock levels.
- Includes Swagger/OpenAPI documentation at `/swagger-ui.html`.

## Setup Instructions
### Prerequisites
- Java 17
- Maven 3.8+
- Docker (optional, for containerized execution)
- Ensure `/tmp/vendor-b/stock.csv` exists with the following content:
  sku,name,stockQuantity
  ABC123,Product A,10
  XYZ456,Product B,0

### Build and Run Locally
1. Clone the repository:
   ```git clone git@github.com:sanjoy-sust/stock-sync.git```
2. ```cd stock-sync```
3. Build the project:
   ```mvn clean package```
4. Run the application:
   ```mvn spring-boot:run```
5. Access the application at `http://localhost:8084`.
6. Access Swagger UI at `http://localhost:8084/swagger-ui.html`.
7. Access H2 Console at `http://localhost:8084/h2-console` (JDBC URL: `jdbc:h2:mem:stocksync`, User: `sa`, Password: leave blank).
8. Test the `GET /products` endpoint using Swagger UI or any API client.
9. Check logs for out-of-stock events.
10. To stop the application, press `Ctrl+C` in the terminal.
11. (Optional) To run with Docker:
    - Build the Docker image:
      ```docker build -t stock-sync .```
    - Run the Docker container:
      ```docker run -p 8084:8084 -v /tmp/vendor-b:/tmp/vendor-b stock-sync```
12. Access the application at `http://localhost:8084` as described above.

### Vendor A Simulation
Vendor A is simulated using a separate Spring Boot controller (not included in this project). Example response:
```json
[
  {"sku": "ABC123", "name": "Product A", "stockQuantity": 8},
  {"sku": "LMN789", "name": "Product C", "stockQuantity": 0}
]
```
Run a mock server at `http://localhost:8081/vendor-a/products` to provide this data.

### Vendor B Simulation
Vendor B provides a CSV file at `/tmp/vendor-b/stock.csv` with the structure:
```csv
sku,name,stockQuantity
XYZ123,Product A,10
XYZ456,Product B,0
```

## Assumptions
- SKUs are unique per vendor, so a composite key (sku + vendor) is used.
- Full sync is performed every 1 minutes, replacing existing data.
- Stock-out events are logged using SLF4J.
- Vendor A API is available at `http://localhost:8081/vendor-a/products`.
- CSV file is always accessible at `/tmp/vendor-b/stock.csv`.

## Trade-offs
- Used H2 in-memory database for simplicity; production would use a persistent database like PostgreSQL.
- Basic error handling for API and CSV; retry logic could be added for resilience.
- No incremental sync; full sync is simpler but less efficient for large datasets.
- Not implemented exception handling for CSV parsing errors.

## Improvements
- Add retry logic for Vendor B csv file read using Spring Retry.
- Add more comprehensive integration tests and unit tests.
- Implement proper exception handling and logging errors.
- Add authentication and authorization for the API endpoints.
- Use a persistent database for production use cases.
- proper usage of mappers for entity to dto conversion.
- Add pagination and filtering to the `GET /products` endpoint.
- implement all CRUD operations for products.
- Add monitoring and alerting for sync failures.
- Write more unit test cases.



### SAMPLE CURL COMMANDS:
- To get all products:
  ```bash
  curl -X 'GET' \
  'http://localhost:8084/products' \
  -H 'accept: */*'"
  ```
- JSON Response:
  ```json
   {
  "data": [
    {
      "id": 1,
      "sku": "ABC123",
      "name": "Product A",
      "stockQuantity": 8,
      "vendor": "VendorA"
    },
    {
      "id": 2,
      "sku": "LMN789",
      "name": "Product C",
      "stockQuantity": 0,
      "vendor": "VendorA"
    },
    {
      "id": 3,
      "sku": "XYZ123",
      "name": "Product A",
      "stockQuantity": 10,
      "vendor": "VendorB"
    },
    {
      "id": 4,
      "sku": "XYZ456",
      "name": "Product B",
      "stockQuantity": 0,
      "vendor": "VendorB"
    }
  ]
  }
  ```
