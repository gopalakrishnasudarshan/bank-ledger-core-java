A Kafka-inspired, event-driven banking ledger being built using pure Core Java.

The project explores low-level TCP networking, append-only log design, and event-driven system fundamentals without using frameworks.

 Current work done: Implement concurrent client handling and add offset-based ledger read support

- Introduced fixed thread pool in LedgerServer for multi-client handling
- Made LedgerWriter.append() thread-safe using synchronized
- Added graceful shutdown hook for ExecutorService
- Implemented READ,<offset>,<maxLines> protocol for ledger consumption
- Added basic offset-based log reading with bounded response and END marker

Work in progress: additional features such as concurrency, consumer services, and fault-tolerance are being added incrementally.
