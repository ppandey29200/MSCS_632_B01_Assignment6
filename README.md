# MSCS_632_B01_Assignment6 - Ride Sharing System — Multi-Threaded Data Processing (Java & Go)

This project simulates a ride sharing system where multiple worker threads (drivers) process ride requests concurrently using shared resources. The system is implemented in **both Java and Go**, showcasing different concurrency models and error-handling strategies.

---

## Features

- Shared task queue with safe concurrent access
- Multiple worker threads/goroutines simulating data processing
- Synchronized output storage (results list)
- Exception and error handling:
  - Java: Try-catch blocks
  - Go: Error values and `defer` for cleanup
- Thread-safe logging for debugging and output tracking

---

## Technologies Used

- Java 11+
- Go 1.17+
- Standard concurrency packages:
  - Java: `java.util.concurrent`, `ReentrantLock`
  - Go: `goroutines`, `channels`, `sync.Mutex`, `sync.WaitGroup`

---

