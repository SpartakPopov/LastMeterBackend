# LastMeterBackend User Manual

This README is a self-contained user manual for peer testers and stakeholders.

No technical knowledge is required beyond having Docker installed and running. The application has no graphical interface in this repository, so testing is done by starting the backend locally and using the exact copy-paste commands in this document.

## 1. What You Are Testing

LastMeterBackend is a backend service for a last-meter delivery workflow. It lets users and testers validate these workflows:

- find users
- create order requests
- edit order requests
- approve or reject order requests
- fulfill approved requests by creating packages
- view, update, deliver, and pick up packages
- receive package delivery notifications
- view lockers
- group order requests

The backend runs locally on your computer at:

```text
http://localhost:8080
```

## 2. What You Need

You only need:

- Docker Desktop installed
- Docker Desktop running
- this project folder available on your computer

You do not need to install Java, Gradle, MySQL, or any developer tools.

## 3. Quick Start

Run the commands below from the main project folder, the folder that contains this README file.

### Step 1: Open a terminal

Use one of these:

- Windows: PowerShell
- macOS: Terminal
- Linux: Terminal

### Step 2: Confirm Docker is available

```bash
docker --version
```

Expected result: Docker prints a version number.

If Docker is not recognized, open Docker Desktop and wait until it finishes starting.

### Step 3: Create the app network

```bash
docker network create lastmeter-net
```

If Docker says the network already exists, continue. That is not a problem.

### Step 4: Start the database

```bash
docker run --name lastmeter-mysql --network lastmeter-net -e MYSQL_ROOT_PASSWORD=lastmeter123 -e MYSQL_DATABASE=lastmeterdb -p 3306:3306 -d mysql:8.4
```

Wait 30 seconds before continuing. The database needs time to initialize.

### Step 5: Build the backend

```bash
docker build -t lastmeter-backend ./LastMeterBackend
```

Expected result: the build finishes without errors.

The first build may take several minutes.

### Step 6: Start the backend

```bash
docker run --name lastmeter-api --network lastmeter-net -p 8080:8080 -e DB_PASSWORD=lastmeter123 -e SPRING_DATASOURCE_URL=jdbc:mysql://lastmeter-mysql:3306/lastmeterdb -e SPRING_DATASOURCE_USERNAME=root -d lastmeter-backend
```

Wait 20 seconds before continuing.

### Step 7: Confirm it is running

Open this address in a browser:

```text
http://localhost:8080/packages/all
```

Expected result: you should see a JSON list. It may be empty:

```json
[]
```

If the browser cannot open the page, go to the troubleshooting section near the end of this manual.

## 4. Add Test Data

The application does not currently include screens for creating users, buildings, or lockers. Use this command once after the backend has started.

```bash
docker exec lastmeter-mysql mysql -uroot -plastmeter123 lastmeterdb -e "INSERT INTO users (id, first_name, last_name, email, role) VALUES (1, 'Alex', 'Ionescu', 'alex.ionescu@example.com', 'EMPLOYEE'), (2, 'Maria', 'Popescu', 'maria.popescu@example.com', 'RECEPTIONIST'), (3, 'Dana', 'Manager', 'dana.manager@example.com', 'ADMIN') ON DUPLICATE KEY UPDATE first_name=VALUES(first_name), last_name=VALUES(last_name), email=VALUES(email), role=VALUES(role); INSERT INTO buildings (id, name, address, description) VALUES (1, 'Main Office', '1 Test Street', 'Peer testing building') ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), description=VALUES(description); INSERT INTO lockers (id, locker_number, size, status, building_id) VALUES (1, 'LOCKER-001', 'SMALL', 'AVAILABLE', 1), (2, 'LOCKER-002', 'LARGE', 'AVAILABLE', 1) ON DUPLICATE KEY UPDATE locker_number=VALUES(locker_number), size=VALUES(size), status=VALUES(status), building_id=VALUES(building_id);"
```

Test users:

| ID | Name | Role |
| --- | --- | --- |
| 1 | Alex Ionescu | Employee |
| 2 | Maria Popescu | Receptionist |
| 3 | Dana Manager | Admin |

Test lockers:

| ID | Locker Number | Size | Status |
| --- | --- | --- | --- |
| 1 | LOCKER-001 | SMALL | AVAILABLE |
| 2 | LOCKER-002 | LARGE | AVAILABLE |

## 5. How to Use the Test Commands

This manual uses `curl` commands to test the backend.

Windows note: if PowerShell does not run a `curl` command correctly, replace `curl` with `curl.exe` and keep the rest of the command unchanged.

Each command is safe to copy and paste as shown.

For commands that say "replace the ID", use the ID returned by your previous command. If you followed this manual from a clean database, the example IDs usually match.

## 6. End-to-End Test Scenario

This scenario validates the main application flow.

### 6.1 Search for a user

```bash
curl "http://localhost:8080/users/search?q=Alex"
```

Expected result: the response includes Alex Ionescu.

### 6.2 View lockers

```bash
curl "http://localhost:8080/lockers/all"
```

Expected result: the response includes `LOCKER-001` and `LOCKER-002`.

### 6.3 Create an order request

```bash
curl -X POST "http://localhost:8080/order-requests" -H "Content-Type: application/json" -d "{\"description\":\"Wireless keyboard for office desk\",\"productLinks\":\"https://example.com/keyboard\",\"quantity\":1,\"requestedById\":1,\"requestedForId\":1}"
```

Expected result:

- a new order request is returned
- `status` is `PENDING`
- note the returned `id`

### 6.4 Edit the order request

This example assumes the order request ID is `1`.

```bash
curl -X PATCH "http://localhost:8080/order-requests/1" -H "Content-Type: application/json" -d "{\"description\":\"Wireless keyboard and mouse for office desk\",\"productLinks\":\"https://example.com/keyboard-mouse\",\"quantity\":2}"
```

Expected result:

- the description is updated
- the quantity is updated to `2`

### 6.5 Approve the order request

```bash
curl -X PATCH "http://localhost:8080/order-requests/1/approve" -H "Content-Type: application/json" -d "{\"managerNotes\":\"Approved for peer testing.\"}"
```

Expected result:

- `status` becomes `APPROVED`
- `managerNotes` contains the approval note

### 6.6 Fulfill the order request

```bash
curl -X PATCH "http://localhost:8080/order-requests/1/fulfill" -H "Content-Type: application/json" -d "{\"packages\":[{\"trackingNumber\":\"TRK-TEST-001\",\"description\":\"Wireless keyboard and mouse package\",\"length\":45.0,\"width\":15.0,\"height\":5.0}]}"
```

Expected result:

- the order request status becomes `ORDERED`
- a package with tracking number `TRK-TEST-001` is created

### 6.7 View all packages

```bash
curl "http://localhost:8080/packages/all"
```

Expected result: the response includes `TRK-TEST-001`.

### 6.8 Assign the package to a locker and mark it delivered

This example assumes the package ID is `1` and locker ID is `1`.

```bash
curl -X PUT "http://localhost:8080/packages/1" -H "Content-Type: application/json" -d "{\"trackingNumber\":\"TRK-TEST-001\",\"description\":\"Wireless keyboard and mouse package\",\"length\":45.0,\"width\":15.0,\"height\":5.0,\"status\":\"DELIVERED_TO_LOCKER\",\"lockerId\":1}"
```

Expected result:

- the package status becomes `DELIVERED_TO_LOCKER`
- the package is linked to `LOCKER-001`

### 6.9 Pick up the package

Before pickup, the user can read the delivery notification:

```bash
curl "http://localhost:8080/notifications/user/1"
```

Expected result:

- the response includes a notification with type `PACKAGE_DELIVERED`
- the message includes the locker and location
- `packageDetailsUrl` points to `/packages/TRK-TEST-001`

To mark the notification as read, replace `1` with the notification ID returned above:

```bash
curl -X PATCH "http://localhost:8080/notifications/1/read"
```

```bash
curl -X POST "http://localhost:8080/packages/pickup" -H "Content-Type: application/json" -d "{\"trackingNumber\":\"TRK-TEST-001\"}"
```

Expected result:

- package status becomes `PICKED_UP`
- `pickedUpAt` is filled in
- the locker becomes available again

## 7. Additional Test Cases

### Create a package manually

```bash
curl -X POST "http://localhost:8080/packages/create" -H "Content-Type: application/json" -d "{\"trackingNumber\":\"TRK-MANUAL-001\",\"description\":\"Manual test package\",\"length\":20.0,\"width\":10.0,\"height\":8.0,\"status\":\"PENDING\",\"receiverId\":2}"
```

Expected result:

- a package is created
- receiver is Maria Popescu
- status is `PENDING`

### View a package by tracking number

```bash
curl "http://localhost:8080/packages/TRK-MANUAL-001"
```

Expected result: the manually created package is returned.

### View packages for a receiver

```bash
curl "http://localhost:8080/packages/receiver/2"
```

Expected result: packages for Maria Popescu are returned.

### View packages without a receiver

```bash
curl "http://localhost:8080/packages/unassigned"
```

Expected result: packages that do not have a receiver are returned.

### Create a second order request

```bash
curl -X POST "http://localhost:8080/order-requests" -H "Content-Type: application/json" -d "{\"description\":\"Laptop stand\",\"productLinks\":\"https://example.com/laptop-stand\",\"quantity\":1,\"requestedById\":1,\"requestedForId\":1}"
```

Expected result: a second order request is created.

### Group order requests

This example assumes order request IDs `1` and `2` exist.

```bash
curl -X POST "http://localhost:8080/order-groups" -H "Content-Type: application/json" -d "{\"name\":\"Office equipment test group\",\"requestedById\":1,\"orderRequestIds\":[1,2]}"
```

Expected result:

- a group is created
- both order requests are included in the group

### View all order groups

```bash
curl "http://localhost:8080/order-groups"
```

Expected result: the created group appears.

### Delete an order group

This example assumes the group ID is `1`.

```bash
curl -X DELETE "http://localhost:8080/order-groups/1"
```

Expected result:

- the group is deleted
- the order requests remain in the system

### Reject an order request

Use this on a request that you do not need for the approved flow.

```bash
curl -X PATCH "http://localhost:8080/order-requests/2/reject" -H "Content-Type: application/json" -d "{\"managerNotes\":\"Rejected for test coverage.\"}"
```

Expected result:

- `status` becomes `REJECTED`
- `managerNotes` contains the rejection reason

## 8. Full Endpoint Reference

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/users/search?q={text}` | Search users by name |
| `GET` | `/lockers/all` | View all lockers |
| `POST` | `/order-requests` | Create an order request |
| `GET` | `/order-requests` | View all order requests |
| `GET` | `/order-requests/my/{userId}` | View requests created by one user |
| `GET` | `/order-requests/{id}` | View one order request |
| `PATCH` | `/order-requests/{id}` | Edit an order request |
| `PATCH` | `/order-requests/{id}/approve` | Approve an order request |
| `PATCH` | `/order-requests/{id}/reject` | Reject an order request |
| `PATCH` | `/order-requests/{id}/fulfill` | Fulfill a request and create packages |
| `POST` | `/packages/create` | Create a package manually |
| `GET` | `/packages/all` | View all packages |
| `GET` | `/packages/{trackingNumber}` | View package by tracking number |
| `GET` | `/packages/receiver/{receiverId}` | View packages for a receiver |
| `GET` | `/packages/unassigned` | View packages without a receiver |
| `PUT` | `/packages/{id}` | Update package details, status, or locker |
| `POST` | `/packages/pickup` | Pick up a delivered package |
| `GET` | `/notifications/user/{userId}` | View notifications for a user |
| `GET` | `/notifications/user/{userId}/unread` | View unread notifications for a user |
| `PATCH` | `/notifications/{id}/read` | Mark a notification as read |
| `POST` | `/order-groups` | Create an order group |
| `GET` | `/order-groups` | View all order groups |
| `GET` | `/order-groups/{id}` | View one order group |
| `DELETE` | `/order-groups/{id}` | Delete an order group |

## 9. Status Values

Package statuses:

- `PENDING`
- `ASSIGNED_TO_LOCKER`
- `DELIVERED_TO_LOCKER`
- `PICKED_UP`

Order request statuses:

- `PENDING`
- `APPROVED`
- `REJECTED`
- `ORDERED`

Locker statuses:

- `AVAILABLE`
- `OCCUPIED`
- `OUT_OF_SERVICE`

Locker sizes:

- `SMALL`
- `LARGE`

User roles:

- `EMPLOYEE`
- `RECEPTIONIST`
- `ADMIN`

## 10. Stop the Application

To stop the backend and database:

```bash
docker stop lastmeter-api lastmeter-mysql
```

To start them again later:

```bash
docker start lastmeter-mysql
docker start lastmeter-api
```

Wait 20 seconds after restarting before testing.

## 11. Reset Everything

Use this if you want a completely clean test run.

Warning: this removes the test database container and all test data.

```bash
docker stop lastmeter-api lastmeter-mysql
docker rm lastmeter-api lastmeter-mysql
docker rmi lastmeter-backend
```

Then repeat the setup from section 3.

## 12. Troubleshooting

### Docker says the container name is already in use

This means a previous test container already exists.

To see existing containers:

```bash
docker ps -a
```

To reset and start fresh, follow section 11.

### The browser cannot open `http://localhost:8080/packages/all`

Check whether both containers are running:

```bash
docker ps
```

Expected result: the list includes:

- `lastmeter-api`
- `lastmeter-mysql`

If `lastmeter-api` is missing, start it:

```bash
docker start lastmeter-api
```

Wait 20 seconds and try the browser again.

### The backend stopped immediately

View the logs:

```bash
docker logs lastmeter-api
```

Common causes:

- the database was still starting
- port `8080` is already used
- the database container is not running

If the database was still starting, wait 30 seconds and run:

```bash
docker start lastmeter-api
```

### Port 8080 is already in use

Run the backend on port `8081` instead:

```bash
docker run --name lastmeter-api --network lastmeter-net -p 8081:8080 -e DB_PASSWORD=lastmeter123 -e SPRING_DATASOURCE_URL=jdbc:mysql://lastmeter-mysql:3306/lastmeterdb -e SPRING_DATASOURCE_USERNAME=root -d lastmeter-backend
```

Then use:

```text
http://localhost:8081
```

instead of:

```text
http://localhost:8080
```

### User search returns an empty list

Run the test data command again from section 4.

If it still returns an empty list, confirm the backend is running and try:

```bash
curl "http://localhost:8080/users/search?q=Maria"
```

### Package pickup fails

Pickup only works when the package status is `DELIVERED_TO_LOCKER`.

Before pickup, run the package update command from section 6.8 to assign the package to a locker and mark it delivered.

### A command with tracking number already exists fails

Tracking numbers must be unique. If a command fails because `TRK-TEST-001` or `TRK-MANUAL-001` already exists, either reset everything using section 11 or change the tracking number in the command.

## 13. Notes for Stakeholders

This test build is intentionally open for local testing and does not require login.

The current repository does not include a frontend user interface. It also does not include public screens for creating users, buildings, or lockers. The manual provides test data commands so testers can still validate the main workflows independently.

The main business flows available for validation are:

- order request creation
- order request review
- order request fulfillment
- package creation and update
- locker visibility
- package pickup
- order grouping
