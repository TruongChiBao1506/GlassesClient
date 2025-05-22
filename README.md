👓 Glasses Client Application
This is a Spring Boot client application designed to interact with the Glasses Store backend API. It provides a seamless user experience for authentication, product browsing, and order management through RESTful API calls.

✨ Overview
The Glasses Client Application serves as the frontend interface for the Glasses Store e-commerce platform. It enables users to log in, browse eyewear products, manage their shopping cart, and handle orders by communicating with the backend API hosted on an AWS EC2 instance.

🧩 Features

User Authentication – Login and registration  
Password Management – Forgot and reset password functionality  
OTP Verification – Secure account verification  
Product Catalog Browsing – Explore the eyewear catalog  
Shopping Cart – Add and manage items in the cart  
Order Management – Place and track orders


📂 Project Structure

src/main/java – Contains the Spring Boot application code  
src/main/resources – Configuration files (e.g., application.properties)  
pom.xml – Maven dependencies and build configuration


📦 Prerequisites

Java 17+  
Maven  
Internet Connection – To access the backend API


⚙️ Configuration
The application connects to the Glasses Store backend API hosted on an AWS EC2 instance. Configure the backend URL in the application.properties file:
backend.api.url=http://<ec2-instance-ip>:8080


Note: If the EC2 instance is restarted, update the backend.api.url with the new IP address.


🚀 Getting Started

Clone the repository:
git clone <repository-url>


Build the project:
mvn clean install


Run the application:
mvn spring-boot:run


Open your browser and navigate to:
http://localhost:8080


🌐 API Endpoints
The client application interacts with the following backend API endpoints:

User Login: /api/auth/login  
User Registration: /api/auth/register  
Password Recovery: /api/auth/forgot-password  
Password Reset: /api/auth/reset-password  
OTP Verification: /api/auth/verify-otp  
User Logout: /api/auth/logout


🛠️ Technologies Used

Spring Boot – Core framework for the application  
Spring Web – For building RESTful clients  
RestClient – For API communication  
Jackson – For JSON processing  
Cookie-based Authentication – For secure session management


🔧 Development
To set up the development environment:

Import the project into your preferred IDE.  
Configure Java 17+ in your IDE.  
Set up Maven for dependency management.  
Ensure the backend API is running and accessible.


🚀 Deployment
The application can be deployed as a standalone Spring Boot application or containerized using Docker. For production:

Configure the backend.api.url to point to the production backend.  
Enable HTTPS for secure communication.  
Set up proper monitoring and logging.


🙌 Acknowledgements

Spring Boot – For the robust application framework  
The open-source community – For the libraries and tools used in this project

