# REVERIX 2.0
```text
██████╗ ███████╗██╗   ██╗███████╗██████╗ ██╗██╗  ██╗    ██████╗  ██████╗ 
██╔══██╗██╔════╝██║   ██║██╔════╝██╔══██╗██║╚██╗██╔╝    ╚════██╗██╔═████╗
██████╔╝█████╗  ██║   ██║█████╗  ██████╔╝██║ ╚███╔╝      █████╔╝██║██╔██║
██╔══██╗██╔════╝╚██╗ ██╔╝██╔════╝██╔══██╗██║ ██╔██╗     ██╔═══╝ ██║██║██║
██║  ██║███████╗ ╚████╔╝ ███████╗██║  ██║██║██╔╝ ██╗    ███████╗╚██████╔╝
╚═╝  ╚═╝╚══════╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝    ╚══════╝ ╚═════╝ 
```
> **Where mood meets movies. AI-powered. Production-grade.**

---

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socket.io&logoColor=white)](#)

[![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)](https://jwt.io/)
[![Liquibase](https://img.shields.io/badge/Liquibase-2962FF?style=for-the-badge&logo=liquibase&logoColor=white)](https://www.liquibase.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![OpenRouter AI](https://img.shields.io/badge/OpenRouter_AI-FF6B6B?style=for-the-badge)](#)
[![Resilience4j](https://img.shields.io/badge/Resilience4j-F7BD00?style=for-the-badge)](#)

---

## 📽️ What is Reverix 2.0?
Reverix 2.0 is a premium, high-concurrency movie booking platform that redefines discovery through an AI-powered cinematic concierge. Built for speed and reliability, it leverages distributed locking to manage real-time seat availability across diverse theaters. By bridging the gap between emotional mood and movie data, it offers a deeply personalized experience that goes beyond traditional booking engines.

---

## 🏗️ Architecture
Reverix 2.0 is built on **Hexagonal Architecture (Ports & Adapters)** to ensure that core business logic remains isolated, testable, and independent of external infrastructure.

*   **Pure Domain Core**: Models and services contain zero framework dependencies, protecting the integrity of the business rules.
*   **Decoupled Side-Effects**: External systems (DB, AI, Cache) are integrated via defined Output Ports, allowing for seamless infrastructure swaps.
*   **Event-Driven Orchestration**: Complex cross-domain flows are managed by Application Orchestrators and asynchronous listeners.
*   **Stateless Scaling**: Security and session management are moved to the infrastructure perimeter using JWT and externalized caching.

```text
[REST Controllers] ──► [Input Ports] ──► [Domain Services]
      │                                         │
[WebSocket]           [Domain Models]    [Output Ports]
      │                                         │
[JWT Security] ◄───────────────────── [Infrastructure Adapters]
                                 (MySQL | Redis | OpenRouter AI)
```

---

## 🌟 Key Features
- **🤖 AI Movie Concierge**: Real-time natural language discovery via WebSocket and LLM integration (OpenRouter), providing instant movie recommendations.
- **🔒 Distributed Seat Locking**: Atomic seat reservation system powered by Redis with a strict 600s TTL, ensuring zero double-bookings in high-traffic scenarios.
- **🛡️ Idempotent Booking Engine**: Robust transaction management using unique idempotency keys to prevent duplicate charges and records.
- **⚡ Circuit Breaker Resilience**: Integrated Resilience4j fallback mechanisms for external AI calls, maintaining uptime even during third-party outages.
- **🪙 Rev-Coins Reward System**: A tiered loyalty ledger that incentivizes user engagement through bookings and reviews.
- **🔐 JWT Stateless Auth**: Secure, scalable authentication flow with custom filters for role-based access control.
- **🌱 Auto-Seeding TMDb Sync**: Intelligent startup routine that fetches, formats, and seeds the latest cinematic data directly from the TMDb API.
- **📜 Versioned Migrations**: Comprehensive database evolution tracking using Liquibase for reliable multi-environment deployments.

---

## 🚀 API Endpoints
| Method | Endpoint | Auth | Description |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/auth/register` | Public | Create a new account |
| **POST** | `/api/auth/login` | Public | Authenticate and receive JWT |
| **GET** | `/api/movies` | Public | List all movies with ratings |
| **GET** | `/api/movies/{id}` | Public | Get detailed movie metadata |
| **POST** | `/api/bookings/lock` | JWT | Lock seats for 10 minutes |
| **POST** | `/api/bookings/confirm` | JWT | Finalize payment and booking |
| **GET** | `/api/bookings/my-bookings` | JWT | View personal booking history |
| **GET** | `/api/coins/balance` | JWT | Check Re-coin reward balance |
| **POST** | `/api/watchlist/{id}` | JWT | Add movie to personal watchlist |
| **GET** | `/api/reviews/average/{id}` | Public | Get community average rating |

---

## 🛠️ Local Setup
Follow these steps to get Reverix 2.0 running on your machine:

**Prerequisites**: Java 17, MySQL 8.0, Redis 6+, Docker Desktop.

### Step 1: Clone the Repo
```bash
git clone https://github.com/ashish-babu-03/Reverix-2.0.git
cd Reverix-2.0
```

### Step 2: Set Environment Variables
Create an `.env` file or export the following:
```bash
export OPENROUTER_API_KEY=your_openrouter_key
export TMDB_API_KEY=your_tmdb_key
export DB_PASSWORD=your_mysql_password
```

### Step 3: Start Services with Docker
```bash
docker-compose up -d
```

### Step 4: Run the Application
```bash
./gradlew bootRun
```

### Step 5: Verify Deployment
The application will automatically run Liquibase migrations and seed TMDb data.
Visit: [http://localhost:8080](http://localhost:8080)

---

## 🌍 Environment Variables
| Variable | Required | Default | Description |
| :--- | :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | Yes | `jdbc:mysql://localhost:3306/reverix` | DB Connection String |
| `SPRING_DATA_REDIS_HOST` | Yes | `localhost` | Redis instance host |
| `TMDB_API_KEY` | Yes | - | API key for Movie Seeding |
| `OPENROUTER_API_KEY` | Yes | - | API key for AI Chatbot |
| `JWT_SECRET` | No | `super-secret-key` | Secret for token signing |

---

## 💡 What I Learned
Building Reverix 2.0 was a deep dive into engineering resilience and architectural purity. My key takeaways:
- **Hexagonal Architecture is worth the boilerplate**: It felt like extra work at first, but the ability to test domain logic in isolation without starting a Spring context is a game-changer for developer speed.
- **Redis for Atomic State**: Implementing distributed locking taught me the importance of atomic operations. Managing TTLs for seat holds is far superior to database-level locks for user experience.
- **Idempotency is non-negotiable**: In a booking system, handling duplicate requests gracefully is critical. Designing the system to be idempotent from day one saved countless potential data headaches.
- **Designing for Failure**: Integrating circuit breakers for AI calls made the application "self-healing." It taught me to treat every third-party integration as a potential point of failure.
- **AI as a First-Class Citizen**: WebSocket-based AI integration is about more than just chat; it's about making the UI reactive to intelligent suggestions in real-time.

---

**Built with purpose by [Ashish Babu Z](https://github.com/ashish-babu-03)**
> 🎬 Curating cinema, one mood at a time.
