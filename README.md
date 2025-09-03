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
7. Access H2 Console at `http://localhost:8084/h2-console` (JDBC URL: `jdbc:h2:mem:stocksyncdb`, User: `sa`, Password: leave blank).
8. Test the `GET /products` endpoint using Swagger UI or any API client.
9. Check logs for out-of-stock events.
10. To stop the application, press `Ctrl+C` in the terminal.
11. (Optional) To run with Docker:
    - Build the Docker image:
      ```docker build -t stock-sync .```
    - Run the Docker container:
      ```docker run -p 8084:8084 -v /tmp/vendor-b:/tmp/vendor-b stock-sync```
