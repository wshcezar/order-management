# order-management
Order management system with separate services for partners and orders.

## How to Run

```bash
docker-compose up --build
```

- Partner Service API: http://localhost:8081/swagger-ui.html
- Order Service API: http://localhost:8082/swagger-ui.html

## Features

- Create and retrieve partners
- Check available credit
- Create orders with credit validation
- Update order status