package main

import (
	"fmt"
	"log"
	"sync"
	"time"
)

// Constants
const (
	numWorkers = 5
	numTasks   = 20
)

// Task type
type Task string

// Worker function
func worker(id int, tasks <-chan Task, results *[]string, wg *sync.WaitGroup, mu *sync.Mutex) {
	defer wg.Done()
	log.Printf("[Worker %d] started.\n", id)

	for task := range tasks {
		log.Printf("[Worker %d] processing: %s\n", id, task)

		time.Sleep(1 * time.Second) // Simulate processing delay

		mu.Lock()
		*results = append(*results, fmt.Sprintf("Worker %d completed: %s", id, task))
		mu.Unlock()
	}

	log.Printf("[Worker %d] finished.\n", id)
}

func main() {
	log.Println("[INFO] Starting Ride Sharing System...")

	// Shared task queue (channel) and results
	tasks := make(chan Task, numTasks)
	var results []string
	var mu sync.Mutex
	var wg sync.WaitGroup

	// Launch worker goroutines
	for i := 1; i <= numWorkers; i++ {
		wg.Add(1)
		go worker(i, tasks, &results, &wg, &mu)
	}

	// Send tasks into the channel
	for i := 1; i <= numTasks; i++ {
		task := Task(fmt.Sprintf("Ride Request #%d", i))
		tasks <- task
	}
	close(tasks) // Close the channel so workers stop when done

	// Wait for all workers to finish
	wg.Wait()

	log.Println("[INFO] All workers completed.")
	log.Println("[INFO] Final Results:")
	for _, result := range results {
		fmt.Println(" -", result)
	}
}
