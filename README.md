# order-management
Order management system with separate services for partners and orders.

## API Documentation

### Partner Service
[![Partner Service API](https://img.shields.io/badge/OpenAPI-Partner-blue)](http://localhost:8081/swagger-ui/index.html)  
[Partner Service Swagger UI](http://localhost:8081/swagger-ui/index.html)

### Order Service
[![Order Service API](https://img.shields.io/badge/OpenAPI-Order-green)](http://localhost:8082/swagger-ui/index.html)  
[Order Service Swagger UI](http://localhost:8082/swagger-ui/index.html)


## How to Run

```bash
docker-compose up --build
```

- Order Worker Listener
- RabbitMQ: http://localhost:15672
```
username: guest
password: guest
```
- Postgres:
```
username: admin
password: admin
```



## Features

- Create and retrieve partners
- Check available credit
- Create orders with credit validation
- Update order status
- Process Listener Orders