<div align="center">

<br/>

```
██████╗ ███████╗██╗   ██╗███████╗██████╗ ██╗██╗  ██╗
██╔══██╗██╔════╝██║   ██║██╔════╝██╔══██╗██║╚██╗██╔╝
██████╔╝█████╗  ██║   ██║█████╗  ██████╔╝██║ ╚███╔╝
██╔══██╗██╔══╝  ╚██╗ ██╔╝██╔══╝  ██╔══██╗██║ ██╔██╗
██║  ██║███████╗ ╚████╔╝ ███████╗██║  ██║██║██╔╝ ██╗
╚═╝  ╚═╝╚══════╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝
```

### ✦ Cinema Curated For You — Powered by AI, Built for Humans ✦

<br/>

[![Live Demo](https://img.shields.io/badge/🎬%20LIVE%20DEMO%20→-reverix--2--0.onrender.com-e50914?style=for-the-badge&labelColor=000000)](https://reverix-2-0.onrender.com/)
&nbsp;
[![GitHub](https://img.shields.io/badge/SOURCE%20CODE-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/ashish-babu-03/Reverix-2.0)

<br/>

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)
![Llama 3.3](https://img.shields.io/badge/Llama%203.3-041028?style=flat-square&logo=meta&logoColor=white)
![Render](https://img.shields.io/badge/Render-46E3B7?style=flat-square&logo=render&logoColor=black)
![Liquibase](https://img.shields.io/badge/Liquibase-2962FF?style=flat-square)
![Resilience4j](https://img.shields.io/badge/Resilience4j-0055AA?style=flat-square)

<br/>

> *Built by a backend engineer who got laid off in March 2026.*
> *Shipped to production in May. Because building beats waiting.*

<br/>

</div>

---

## What is Reverix?

Reverix is a **full-stack, production-grade movie booking platform** — not a tutorial clone, not a CRUD demo. It is a real product with a real architecture, real engineering decisions, and a real opinion about how cinema should feel.

It handles atomic seat locking, idempotency-guaranteed bookings, AI-powered mood recommendations via Rev-Bot, and a gamified coin reward system inspired by mobile gaming economies — applied to cinema for the first time.

**Every flow works. End to end. In production. Right now.**

**[→ Open Reverix and try it live](https://reverix-2-0.onrender.com/)**

---

## Architecture

Reverix is built on **Hexagonal (Ports & Adapters) Architecture**. The domain layer has zero knowledge of Spring, MySQL, Redis, or anything external. Swap the entire infrastructure — the business logic doesn't care.

```
╔══════════════════════════════════════════════════════════════════════════╗
║                        R E V E R I X  2 . 0                              ║
╠══════════════════════════════════════════════════════════════════════════╣
║                                                                          ║
║  DRIVING SIDE (Input Adapters)         DRIVEN SIDE (Output Adapters)     ║
║  ──────────────────────────────        ─────────────────────────────     ║
║  ┌─────────────┐                                      ┌──────────────┐   ║
║  │  REST API   │                                      │  PostgreSql  │   ║
║  │ Controllers │────────────────────────────────────▶ │  (Liquibase) │   ║
║  └─────────────┘         ┌───────────────────┐        └──────────────┘   ║
║                          │                   │                           ║
║  ┌─────────────┐         │   DOMAIN LAYER    │         ┌──────────────┐  ║
║  │  WebSocket  │────────▶│                   │───────▶│    Redis     │  ║
║  │  (Rev-Bot)  │         │  BookSeatUseCase  │         │  Seat Locks  │  ║
║  └─────────────┘         │  CoinUseCase      │         └──────────────┘  ║
║                          │  ReviewUseCase    │                           ║
║  ┌─────────────┐         │  WatchlistUseCase │         ┌──────────────┐  ║
║  │ Vanilla JS  │────────▶│  RecommendUseCase │───────▶ │  Llama 3.3  │  ║
║  │  Frontend   │         │                   │         │   (Rev-Bot)  │  ║
║  └─────────────┘         │  Pure Kotlin/Java │         └──────────────┘  ║
║                          │  Zero frameworks  │                           ║
║                          └───────────────────┘         ┌──────────────┐  ║
║                                    │                   │     TMDB     │  ║
║                                    └─────────────────▶ │  Movie Data  │  ║
║                                                        └──────────────┘  ║
║  ┌───────────────────────────────────────────────────────────────────┐   ║
║  │  Resilience4j — Circuit Breaker (TMDB) · Rate Limiter (AI)        │   ║
║  │  Health Controller · Email Alerts on Downtime · Render Hosting    │   ║
║  └───────────────────────────────────────────────────────────────────┘   ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

## Feature Walkthrough

### 🔐 Auth — JWT, Stateless, Role-Based
Register or login. Get a JWT. Every protected endpoint validates it at the Spring Security filter level. Roles: `USER` and `ADMIN`. Session state lives in the token, not the server.

---

### 🎬 Discovery — Real Movies, Real Data

```
TMDB API ──▶ MovieRepositoryAdapter ──▶ Cache ──▶ Movie Grid (UI)
                    │
                    └──▶ Circuit Breaker (Resilience4j) ──▶ Fallback
```

Real movie posters. Genre filters. Language filters. Live search. Click any movie → detail drawer opens with rating, duration, genre tags, and two actions: **Watchlist** or **Book Tickets**. Watchlist persists per user in MySQL.

---

### 💺 The Booking Engine — Where It Gets Serious

Race conditions, double-bookings, and payment retries are all handled.

```
① User selects seats on the seat map
         │
         ▼
② POST /api/bookings/lock
         │
         ▼
③ Redis SETNX — atomic seat lock (TTL: 10 minutes)
   Seats invisible to all other users for 10 min
         │
         ▼
④ Idempotency key check
   Same request twice = same response, one booking
         │
         ▼
⑤ Payment (UPI QR flow)
         │
         ▼
⑥ POST /api/bookings/confirm
         │
         ▼
⑦ MySQL persistence + BookingConfirmedEvent published
         │
         ▼
⑧ CoinUseCase triggered → coins credited to user ✅
```

**Engineering decisions that matter:**
- `Redis SETNX` for atomic locking — no race condition possible at any scale
- `idempotencyKey` on every booking — rage-clicking "Pay" 10 times = 1 booking, not 10
- `SeatAlreadyLockedException` — clean domain exception, not a 500
- Soft delete pattern — bookings never hard deleted, full audit trail preserved

---

### 🤖 Rev-Bot — AI That Actually Understands You

Not a search bar. Not a filter. Rev-Bot understands **mood**.

```
User: "I'm feeling sad, want something hopeful"
         │
         ▼
WebSocket message via STOMP broker
         │
         ▼
RecommendationDomainService + Llama 3.3 AI
         │
         ▼
Formatted response with clickable Movie Cards
         │
         ▼
User clicks a card → straight into the booking flow
```

Real-time. Conversational. The recommendations come back as interactive cards you can book directly from chat.

---

### 🪙 Coin Reward System — The Product Idea

Inspired by mobile gaming economies. Applied to cinema.

| Action | Coins |
|:---|:---:|
| Book a ticket | +50 |
| Write a review | +20 |
| Watch an ad | +10 |
| Redeem on booking | -N |

**The loop:** Book a movie → earn coins → review it → earn more → watch an ad → earn more → redeem on the next movie. Users stay in the ecosystem. Theatres earn ad revenue. Everyone wins. This isn't a feature — it's a monetization model baked into the architecture from day one.

---

### 🏥 Production Health — Because This Isn't a Demo

```bash
GET /health
# → { "status": "UP", "database": "UP", "redis": "UP" }
```

- Health endpoint for uptime monitoring
- Email alerts fire when the endpoint goes down
- Resilience4j circuit breaker on TMDB — if TMDB is slow, Reverix isn't
- Rate limiter on AI recommendation requests — no runaway costs
- Deployed on Render with real logs and production-like environment

---

## Database Schema

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    users    │       │    movies   │       │   theatres  │
│─────────────│       │─────────────│       │─────────────│
│ id (PK)     │       │ id (PK)     │       │ id (PK)     │
│ email ◀idx  │       │ title       │   ┌──▶│ name        │
│ passwordHash│       │ genre       │   │   │ city        │
│ role        │       │ moodTags    │   │   │ vibe        │
│ createdAt   │       │ language    │   │   └─────────────┘
└──────┬──────┘       │ rating      │   │
       │              │ posterUrl   │   │   ┌─────────────┐
       │              └──────┬──────┘   │   │    shows    │
       │                     │          │   │─────────────│
       │                     └──────────┼──▶│ movie_id(FK)│
       │                                └───│theatre_id   │
       │                                    │ startTime   │
┌──────▼──────┐   ┌──────────────────┐      │ endTime     │
│  bookings   │   │  booking_seats   │      │ totalSeats  │
│─────────────│   │──────────────────│      └──────┬──────┘
│ id (PK)     │◀──│ booking_id (FK)  │             │
│ user_id(FK) │   │ seat_id (FK) ────┼──────┐      │
│ show_id(FK) │   └──────────────────┘      │      │
│ status      │                      ┌──────▼──────▼───┐
│idempotencyKey                      │     seats       │
└─────────────┘                      │─────────────────│
                                     │ id (PK)         │
                                     │ show_id(FK) ◀idx│
                                     │ seatNumber      │
                                     │ zone            │
                                     │ status          │
                                     └─────────────────┘
```

All migrations managed by **Liquibase** — every schema change is versioned, repeatable, and production-safe.

---

## Tech Stack

| Layer | Technology                   | Why |
|:---|:-----------------------------|:---|
| **Backend** | Spring Boot, Kotlin/Java     | Production-grade, battle-tested |
| **Architecture** | Hexagonal (Ports & Adapters) | Domain fully decoupled from infra |
| **Database** | Postgresql                   | Relational integrity for bookings |
| **Migrations** | Liquibase                    | Versioned, production-safe schema changes |
| **Cache / Locking** | Redis                        | Atomic seat holds via SETNX + TTL |
| **AI** | Llama 3.3 API               | Mood-based movie recommendations |
| **Real-time** | WebSockets (STOMP)           | Rev-Bot live chat |
| **Security** | Spring Security + JWT        | Stateless, role-based auth |
| **Resilience** | Resilience4j                 | Circuit breaker + rate limiter |
| **Movie Data** | TMDB API                     | Real posters, real metadata |
| **Frontend** | Vanilla HTML / CSS / JS      | No framework bloat needed |
| **Deployment** | Render                       | Production hosting + health monitoring |

---

## Run Locally

### Prerequisites
- Java 17+
- PostgreSql 11+
- Redis (or Docker)
- Maven

### Setup

```bash
# Clone
git clone https://github.com/ashish-babu-03/Reverix-2.0.git
cd Reverix-2.0

# Create the database using the psql utility
psql -U postgres -c "CREATE DATABASE reverix;"

# Configure
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
# Edit application.properties with your DB, Redis, TMDB, Llama 3.3 , JWT values

# Start Redis (if not running)
docker run -d -p 6379:6379 redis:alpine

# Run the backend
./mvnw spring-boot:run

# Open the frontend
open static/index.html
```

### Environment Variables

| Variable | Where to get it                                                  |
|:---|:-----------------------------------------------------------------|
| `DB_URL` / `DB_USERNAME` / `DB_PASSWORD` | Your local PostgreSql                                            |
| `REDIS_URL` | localhost:6379 or Docker                                         |
| `TMDB_API_KEY` | [themoviedb.org](https://www.themoviedb.org/settings/api) — free |
| `ANTHROPIC_API_KEY` | [console.anthropic.com](https://console.anthropic.com)           |
| `JWT_SECRET` | Any long random string                                           |

### Health check

```bash
curl http://localhost:8080/health
# { "status": "UP", "database": "UP", "redis": "UP" }
```

---

## API Reference

### Public

```
GET  /api/movies           All movies (TMDB-backed, cached)
GET  /api/movies/{id}      Movie detail
GET  /api/shows            Available shows
POST /api/auth/register    Register → returns JWT
POST /api/auth/login       Login → returns JWT
GET  /health               Service health
```

### Protected (Bearer token required)

```
POST /api/bookings/lock    Lock seats — Redis, 10 min TTL
POST /api/bookings/confirm Confirm booking after payment
GET  /api/bookings         My booking history
POST /api/watchlist        Add to watchlist
GET  /api/watchlist        My watchlist
GET  /api/coins/balance    Coin balance
WS   /ws/chat              Rev-Bot — real-time STOMP chat
```

---

## Engineering Lessons

**On Hexagonal Architecture** — When I had to swap how TMDB data was fetched, the domain layer didn't change at all. Zero. The architecture isn't complexity for its own sake. It's a contract that survives infrastructure changes.

**On Redis seat locking** — The seat selection problem is a classic distributed concurrency problem. Two users, same seat, same millisecond. `Redis SETNX` is atomic at the server level — only one writer wins. The TTL guarantees seats are never permanently orphaned if payment doesn't complete.

**On idempotency keys** — I learned this building a billing pipeline at work. Money can never be wrong. A user who retries a payment should get the same result, not a duplicate charge. Every booking carries a `idempotencyKey`. Same key = same response. Always.

**On Resilience4j** — TMDB can go down. Openrouter can be slow. Production systems don't crash when their dependencies do — they degrade gracefully. If TMDB is flaky, the movie list falls back. The user never sees a 500.

**On deployment** — It takes longer than building. Always.

---

## Roadmap

- [ ] Real payment gateway — Razorpay integration
- [ ] Email / SMS notifications — `BookingEventListener` skeleton already wired
- [ ] TMDB periodic sync — `@Scheduled` job for nightly catalogue refresh
- [ ] Social layer — movie buff profiles, review feeds, watch parties
- [ ] Theatre sorting by snack prices *(yes, this is actually planned)*
- [ ] Mobile app

---

## About the Builder

**Ashish Babu** — Backend Engineer

3 years at SuperOps.ai (Series C, $54M raised) building distributed systems at scale — Apache Pulsar, Kafka, SAML 2.0/SSO migrations for 1000+ enterprise clients, billing pipelines where money can never be wrong.

Stack: Kotlin · Java · Spring Boot · MySQL · PostgreSql · Redis · Kafka · Apache Pulsar · AWS S3 · SAML/SSO

Available immediately.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/ashish-babu-z)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/ashish-babu-03)
[![Email](https://img.shields.io/badge/Gmail-EA4335?style=for-the-badge&logo=gmail&logoColor=white)](mailto:ashish.babu.sde@gmail.com)

---

<div align="center">

**[→ Try Reverix Live](https://reverix-2-0.onrender.com/)**

<br/>

*Built with Spring Boot · Redis · Llama 3.3 AI · Hexagonal Architecture · Pure stubbornness*

<br/>

⭐ If this repo helped you think about architecture differently, drop a star

</div>