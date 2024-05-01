# Emergency Notification System

Welcome to the Emergency Notification System (ENS), a robust and scalable application designed to ensure the safety and well-being of over 10 million users by providing timely alerts and notifications during emergencies. This system is built with Spring Boot, Spring Security, Spring MVC, Apache Kafka, Spring Data, and Gradle, ensuring high performance, security, and reliability.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Functional Requirements](#functional-requirements)
- [Non-Functional Requirements](#non-functional-requirements)
- [Business Requirements](#business-requirements)
- [Architecture Diagram](#architecture-diagram)
- [Enhancing Scalability and Low Latency](#enhancing-scalability-and-low-latency)
- [Enhancing Reliability](#enhancing-reliability)
- [Security Measures](#security-measures)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Code Coverage](#code-coverage)
- [Documentation](#documentation)
- [Contributing](#contributing)


## Overview

The ENS is a multi-user application that enables the registration of a vast number of users, the sending of notifications across multiple channels, and the ability for recipients to respond with their safety status or other relevant information. It also allows for the creation of notification templates for quick dispatch during critical situations.

## Features

- **Mass User Registration**: Capable of registering over 50 million users.
- **Multi-Channel Notifications**: Send notifications via SMS, email, push notifications, etc.
- **User Response Mechanism**: Recipients can respond to notifications with their safety status or additional information.
- **Notification Templates**: Create and manage pre-defined notification templates for immediate use.

## Functional Requirements

- **Register a Great Amount of Users**: The system must support the registration of a large number of users, ensuring scalability and performance.
- **Send Notifications**: Users must be able to send notifications to registered recipients through various communication channels.
- **Users Can Respond Differently**: Recipients should be able to reply to notifications with personalized information.
- **Create Notification Templates**: The system should allow for the creation and management of notification templates for rapid response.

## Non-Functional Requirements

- **High Availability**: The system must maintain high availability to ensure it is operational during peak usage or system failures.
- **Reliability**: The system must be highly reliable, ensuring 100% delivery of notifications without data loss or delays.
- **Low Latency**: The system should have high throughput to ensure quick sending and receiving of notifications.
- **Scalability**: The system must be scalable to handle increasing numbers of recipients and notifications without compromising performance.
- **Security**: The system must implement appropriate security measures to protect confidential information and prevent unauthorized access.

## Business Requirements

- **Load Capacity**: The application must support over 1 million users concurrently.
- **Daily Message Volume**: The system should be capable of sending an average of 10 million messages per day.
- **Recipient Distribution**: Each message is sent to an average of 100 recipients.
- **Bulk Messaging for Corporate Clients**: The system should support one-time messaging to up to 300,000 recipients for corporate clients.
- **Message Format**: All notifications must be in text format, with an average length of 300 characters.

## Architecture Diagram

## Enhancing Scalability and Low Latency

The system is architected to efficiently distribute notifications to a large user base promptly. It ensures scalability and low latency through the following strategies:

1. **Dynamic Partitioning:** Upon receiving a request with a sizable list of user IDs, the system dynamically assesses the number of active instances via the Eureka Discovery Server. It then divides the user ID list into equal partitions based on the current instance count. For instance, if 1,000,000 user IDs are received with 100 running instances, each instance handles batches of 10,000 user IDs, distributing them across all available instances using Apache Kafka.


2. **Concurrent Processing:** With multiple instances in operation, the system executes concurrent processing of user notifications. This concurrent processing significantly boosts overall performance and reduces notification delivery time.


3. **Aggregated Results:** Post-processing of user IDs, the system forwards processed entities back to Kafka, albeit to a distinct service tasked with actual notification delivery. This segregation of processing and delivery ensures a modular and scalable architecture.

## Enhancing Reliability

To bolster reliability within the system, the following measures are implemented leveraging the rebalancer and sender services:

1. **Sender Service:** Upon encountering an error during notification delivery, the sender service categorizes it as "RESENDING." This classification aids in identifying notifications requiring reprocessing in case of failures or inconsistencies.


2. **Rebalancer Service:** Periodically, the rebalancer service retrieves notifications marked as "RESENDING." This proactive approach monitors failed deliveries or pending notifications, preventing any potential data loss.


3. **Kafka Transmission:** After retrieving "RESENDING" notifications, the rebalancer service transmits them back to the Kafka system. This step ensures consistent notification delivery, mitigating potential delays or issues encountered during the initial sending process.

## Security Measures

The system ensures secure access and data integrity through the following procedures:

1. **Authentication:** Only authenticated clients possessing valid JWT tokens can access the system. When a client initiates a request, it includes a JWT token in the request headers. The API gateway intercepts the request, validates the JWT token using a Security service, and upon successful validation, responds with a client ID. This client ID is then added to the request headers and forwarded to downstream services.


2. **Authorization:** Downstream services utilize the client ID from the request headers to make informed decisions based on the client's identity. This ensures that only authorized clients can access specific functionalities and data within the system.


3. **Data Encryption:** Confidential information is encrypted both in transit and at rest to prevent unauthorized access. Secure communication channels, such as HTTPS, are utilized for data transmission, and sensitive data is stored in encrypted format within the system's databases.


4. **Access Control:** Role-based access control (RBAC) mechanisms are employed to restrict access to sensitive functionalities and data based on the roles and permissions assigned to each client. This granular access control ensures that clients can only perform actions and access data that are relevant to their roles within the system.


5. **Audit Logging:** All user interactions and system activities are logged to maintain an audit trail of events. These logs include details such as user actions, access attempts, and system modifications, which can be used for forensic analysis, compliance auditing, and troubleshooting purposes.


By implementing these security measures, the system safeguards sensitive information, prevents unauthorized access, and ensures the integrity and confidentiality of data across all interactions.

## Getting Started

To get started with the ENS, follow these steps:

1. Clone the repository: `git clone https://github.com/your-username/emergency-notification-system.git`
2. Navigate to the project directory: `cd emergency-notification-system`
3. Install dependencies using Gradle: `./gradlew build`
4. Configure the application properties for database connections, Kafka settings, and other necessary configurations.
5. Run the application: `./gradlew bootRun`

## Usage

Once the application is running, users can register, create notification templates, and send notifications to recipients. Recipients can then respond to these notifications with their status or other relevant information.

## Code Coverage:

## Documentation:

## Contributing

Feel free to contribute to the project by submitting bug reports, feature requests, or pull requests. Your input is valuable in enhancing the functionality and user experience of the ENS.