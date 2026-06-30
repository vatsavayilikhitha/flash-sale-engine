**Live demo:** https://flash-sale-engine-8sbe.onrender.com
# Flash Sale Engine

A concurrency-safe inventory system that simulates the Big Billion Days problem: thousands of users competing for a limited number of items, with zero overselling guaranteed.

## The problem

During flash sales, multiple users can attempt to buy the last item at the exact same millisecond. Without proper concurrency control, this leads to overselling — a system that confirms more orders than it has stock for. This project solves that with thread-safe inventory management, a fair FIFO queue, and a persistent order ledger.

## What it does

- Accepts purchase attempts from any number of concurrent users
- Guarantees stock never goes negative, even under simultaneous load
- Processes users in fair, first-registered-first-served order
- Persists every order permanently to MongoDB Atlas
- Exposes a live dashboard showing real-time stock, success/sold-out counts, and full order history

## Tech stack

- **Backend** — Java 17, Spring Boot 3.5
- **Concurrency** — `synchronized` methods + `AtomicInteger` for thread-safe stock decrements
- **Database** — MongoDB Atlas (cloud, persistent)
- **Frontend** — Vanilla HTML/CSS/JS dashboard
- **Queue** — In-memory FIFO queue (`LinkedList`) for fair order processing

## Architecture

```
Client (dashboard)
      |
      v
SaleController  --- /register/{userId}  --> QueueService (FIFO queue)
                --- /buy                --> InventoryService (synchronized)
                --- /status, /stats      --> read-only aggregates
                --- /history             --> OrderRepository --> MongoDB Atlas
```

Every `buyItem()` call is wrapped in a `synchronized` block, so even if 10,000 threads call it simultaneously, only one thread can decrement the stock counter at a time. This makes overselling mathematically impossible — not just unlikely.

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/sale/register/{userId}` | Adds a user to the purchase queue |
| POST | `/sale/buy` | Processes the next user in queue against live inventory |
| GET | `/sale/status` | Returns current stock and queue length |
| GET | `/sale/stats` | Returns success/sold-out counts and stock remaining |
| GET | `/sale/history` | Returns the full persisted order ledger |

## Running locally

```bash
git clone https://github.com/vatsavayilikhitha/flash-sale-engine.git
cd flash-sale-engine
./mvnw spring-boot:run
```

Then open `http://localhost:8080` in your browser.

You'll need a MongoDB Atlas connection string set as the `MONGODB_URI` environment variable, or in `application-local.properties` for local development.

## Simulating scale

The core logic was stress-tested with a 500-user, 100-item simulation (see `Main.java`):

```
SUCCESS orders : 100
SOLD OUT       : 400
Stock remaining: 0
```

Exactly 100 succeed, exactly 400 fail gracefully — no overselling, no errors, no race conditions.

## What this demonstrates

- Thread safety and concurrency control in Java
- Designing systems for fairness under load (FIFO queueing)
- REST API design with Spring Boot
- Database persistence with MongoDB
- Thinking at the scale real e-commerce platforms operate at

## Author

Likhitha Vatsavayi — built for Flipkart GRiD 8.0