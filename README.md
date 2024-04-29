Real-time Transaction Service
===============================
# Overview
## Schema
The [included service.yml](service.yml) is the OpenAPI 3.0 schema to the service.

## Details
The service processes two distinct transaction types:

- Loads: Credits funds to a user's account.
- Authorizations: Conditionally debits funds from a user's account.

Each transaction, whether a load or an authorization, return the user's updated balance upon completion. Additionally, even if an authorization is declined and does not alter the balance, it will still be recorded.

## Instructions

To run this server locally, do the following:

Clone this repository:

Ensure you have access to the source code. If it's hosted on a version control system like Git, use:

```bash
$ git clone [repository_url]
$ cd [project_directory]
````

### Running the Application with Docker

1. Build the docker image:

```bash
$ docker build -t real-time-transaction-service .
```

2. Run the docker container:

```bash
$ docker run -p 8080:8080 --name real-time-transaction-service real-time-transaction-service
```

### Running the Application with Maven

1. Environment setup:

```bash
$ sudo apt update
$ sudo apt install maven
```

2. Build the project:

```bash
$ mvn clean install
```

3. Run the project:

```bash
$ mvn spring-boot:run
```

### Verify the Server

Open a browser or use a tool like Postman to hit the /transaction/ping endpoint to verify that the server is running correctly.

### Endpoint Test

1. Ping Endpoint Test

```bash
$ curl -X GET "http://localhost:8080/transaction/ping" \
     -H "Accept: application/json"
     
❯ {"serverTime":"2024-04-27T20:52:47.336995638Z"}%
```

2. Load Funds Endpoint Test

- Loading funds successfully:

```bash
$ curl -X PUT "http://localhost:8080/transaction/load/736fe931-f8eb-4c1d-b489-d365274d5038" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
           "userId": "2226e2f9-ih09-46a8-958f-d659880asdfD",
           "transactionAmount": {
             "amount": "100.23",
             "currency": "USD",
             "debitOrCredit": "CREDIT"
           }
         }'
         
❯ {"userId":"2226e2f9-ih09-46a8-958f-d659880asdfD",
   "messageId":"736fe931-f8eb-4c1d-b489-d365274d5038",
   "balance":{"amount":"100.23",
              "currency":"USD",
              "debitOrCredit":"CREDIT"}}%
```

- Invalid UUID for message ID:

```bash
$ curl -X PUT "http://localhost:8080/transaction/load/55210c62-e480-asdf-bc1b-e991ac67FSAC" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
           "userId": "2226e2f9-ih09-46a8-958f-d659880asdfD",
           "transactionAmount": {
             "amount": "100.23",
             "currency": "USD",
             "debitOrCredit": "CREDIT"
           }
         }'
         
❯ {"message":"Invalid message ID format: 55210c62-e480-asdf-bc1b-e991ac67FSAC - The UUID to be validated should be 128 bit long.",
   "code":"400"}%
```

3. Authorization Endpoint Test

- Removing funds successfully:

```bash
$ curl -X PUT "http://localhost:8080/transaction/authorization/3f29fc51-2a3f-49da-8361-f4e64f62ab02" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
           "userId": "2226e2f9-ih09-46a8-958f-d659880asdfD",
           "transactionAmount": {
             "amount": "100.23",
             "currency": "USD",
             "debitOrCredit": "DEBIT"
           }
         }'
         
❯ {"userId":"2226e2f9-ih09-46a8-958f-d659880asdfD",
   "messageId":"3f29fc51-2a3f-49da-8361-f4e64f62ab02",
   "responseCode":"APPROVED",
   "balance":{"amount":"0.00",
              "currency":"USD",
              "debitOrCredit":"DEBIT"}}%
```

- Declined Authorization

```bash
$ curl -X PUT "http://localhost:8080/transaction/authorization/a73ec0b0-1523-4cb7-9978-872523812cdb" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
           "userId": "2226e2f9-ih09-46a8-958f-d659880asdfD",
           "transactionAmount": {
             "amount": "5000",
             "currency": "USD",
             "debitOrCredit": "DEBIT"
           }
         }'
         
❯ {"userId":"2226e2f9-ih09-46a8-958f-d659880asdfD",
   "messageId":"a73ec0b0-1523-4cb7-9978-872523812cdb",
   "responseCode":"DECLINED",
   "balance":{"amount":"0.00",
              "currency":"USD",
              "debitOrCredit":"DEBIT"}}%
```

# Design considerations

1. Event-Sourcing and CQRS Principles:

- Implementation: I implemented the transaction service using the Event-Sourcing and Command Query Responsibility Segregation (CQRS) patterns. This architecture separates the read and write models, enhancing the system's performance, scalability, and reliability. Event-sourcing ensures that all changes to the application state are stored as a sequence of events, which can then be used to reconstruct past states if necessary.
- Benefits: This approach provides a robust audit trail and facilitates complex business processes by maintaining a complete history of all changes. It also allows for high flexibility in querying data without impacting write performance.

2. Concurrency Handling:

- ConcurrentLinkedQueue for Event Storage: I use a ConcurrentLinkedQueue to store events. This non-blocking data structure efficiently handles concurrent data operations, ensuring that event processing remains fast and reliable, even under high load.
- ConcurrentHashMap for User Balances: User balances are managed using a ConcurrentHashMap. This choice is crucial for maintaining thread safety while avoiding the latency and resource overhead associated with traditional locking mechanisms. ConcurrentHashMap supports high concurrency by allowing multiple threads to read and write simultaneously without blocking, making it ideal for a financial system where balance updates are frequent and performance is critical.

3. Extensibility:

- Framework Choice: By choosing Spring Boot, I leverage its extensive ecosystem and dependency injection to facilitate the seamless integration of new features. This extensibility allows us to adapt quickly to new business requirements or changes in financial regulations.
- Service Evolution: The system is designed to easily incorporate additional services or modifications, such as new payment methods or integration with other financial platforms, without significant overhaul.

4. Error Handling:

- Robustness: The service includes comprehensive error handling that anticipates potential failure points, thereby preventing system crashes and unhandled exceptions. This ensures a reliable user experience and trustworthiness of the platform.

5. Currency Unification via Exchange Service:

- Rationale: Implementing an exchange service to standardize transactions to the US dollar addresses the complexities of dealing with multiple currencies. This unification simplifies the financial reporting and auditing processes, crucial for meeting the compliance and regulatory requirements in the United States.
- Business Impact: By converting all transactions to a single currency, the company can more accurately track income flows and manage financial risks associated with currency fluctuations. This strategic approach not only enhances operational efficiency but also aligns with Current's goal of maximizing revenue through effective financial management.
- Technical Benefits: The exchange service also allows us to offer more consistent and competitive rates to our customers, improving user satisfaction and potentially increasing transaction volumes.

# Logic of entire process:

- Load Funds Process:
1. When a user calls the `/load/{messageId}` endpoint, the request is routed to the `loadFunds` method of the `TransactionController`.
2. In the `loadFunds` method, it parses `userId` and `transactionAmount` from the `request` body.
3. It uses `userId` and `transactionAmount` to create a `LoadFundsCommand` object.
4. This command is passed to the `LoadFundsCommandHandler`, which creates a `LoadFundsEvent` object based on the command and stores it in the `InMemoryEventStore`.
5. The `InMemoryEventStore` handles the new event and passes the `LoadFundsEvent` to the `LoadFundsEventHandler`.
6. The `LoadFundsEventHandler` updates the user's balance in the `UserBalanceReadModel`.
7. Finally, the controller retrieves the updated balance from the `UserBalanceReadModel` and returns it to the client in the format of a `LoadResponse`.

- Authorization Process:
1. When a user calls the `/authorization/{messageId}` endpoint, the `request` is routed to the `authorizeFunds` method of the `TransactionController`.
2. That method parses `userId` and `transactionAmount` from the `request` body.
3. It uses these data to create an `AuthorizeFundsCommand` object.
4. This command is passed to the `AuthorizeFundsCommandHandler`, which creates an `AuthorizationEvent` object based on the command and stores it in the `InMemoryEventStore`.
5. The `InMemoryEventStore` handles the new event, and passes the `AuthorizationEvent` to the `AuthorizationEventHandler`.
6. The `AuthorizationEventHandler` updates the user's balance in the `UserBalanceReadModel`.
7. Finally, the controller retrieves the updated balance from the `UserBalanceReadModel`, and based on whether the balance is zero, decides the `responseCode` and `adviceMessage`. It returns this to the client in the format of an `AuthorizationResponse`.

# Deployment considerations

- Cloud Services:
  - Deploy on a managed Kubernetes service such as AWS EKS, Azure AKS, or Google GKE. These services offer auto-scaling, load balancing, and self-healing systems which are critical for high-availability and scalability.
- Database Integration:
  - Integrate with a robust relational database like PostgreSQL or MySQL for persistent storage of transactions and balances, with replication and backup configurations to ensure data integrity and availability.
- Monitoring and Logging:
  - Implement logging via ELK stack (Elasticsearch, Logstash, Kibana) and monitoring using Prometheus and Grafana to keep track of the application's health and performance in real-time.
- Security:
  - Use HTTPS for secure communication, manage secrets with tools like AWS Secrets Manager or HashiCorp Vault, and ensure compliance with financial regulations for data protection and privacy.
- Continuous Integration/Continuous Deployment (CI/CD):
  - Set up pipelines using Jenkins, GitHub Actions, or GitLab for automated testing and deployment, ensuring quick and reliable delivery of updates.