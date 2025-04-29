# Cosmetic Platform  

This project was developed as part of the **Gelecek Hayalim Java Backend Training Final Project**. It is a cosmetic platform built using **Java Spring Boot**, with essential functionalities for product management, user interactions, and authentication. The project utilizes **Docker, Maven, JUnit, and Mockito** for containerization, dependency management, and testing.

---

## Features  

The platform provides three roles:  

- **Expert:** Can add product details, write reviews, and provide ratings.  
- **Customer:** Can share product experiences by adding comments and ratings.  
- **Admin:** Manages the platform, including user accounts and product operations.  

### Core Functionalities:  
- **Product Management:** Experts can add and edit product information.  
- **Reviews & Ratings:** Both experts and customers can leave feedback and update or delete their comments.  
- **Authorization System:** Each role has specific permissions—only experts can add product details, while admins oversee platform operations.  
- **REST API:** Provides endpoints for managing products, users, reviews, and ratings.  
- **Database Operations:** CRUD operations are implemented using **Spring Data JPA** and MySQL.  

---

## Technologies Used  

- **Java Spring Boot** – Framework for RESTful API development.  
- **MySQL** – Database management system.  
- **JWT (JSON Web Token)** – Used for authentication and authorization.  
- **Docker** – Containerized deployment for isolated environments.  
- **Maven** – Dependency and build management.  
- **JUnit & Mockito** – Unit testing framework to ensure code reliability.  

---

## Installation Guide  

### Requirements:  
- Java 17 or later  
- Maven  
- MySQL  
- Docker (Optional)  

### Steps:  

#### Clone the repository:  
```bash
git clone https://github.com/username/cosmetic-platform.git
cd cosmetic-platform
```

#### Configure the database:  
Create a new database in MySQL:  
```sql
CREATE DATABASE cosmetic_platform;
```

Update the database settings in `src/main/resources/application.properties`:  
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cosmetic_platform
spring.datasource.username=user
spring.datasource.password=password
```

#### Build and run the project:  
```bash
mvn clean install
mvn spring-boot:run
```

#### Test the API endpoints:  
Once the project is running, you can test the API using **Postman** or similar tools.  

Example: **Add a Product** (Experts only):  
```http
POST /api/product
```

Example: **Add a Review**:  
```http
POST /api/product/{productId}/comment
```

---

## Running with Docker (Optional)  

If you want to run the project inside a Docker container:  

#### Build and run the Docker container:  
```bash
docker-compose up --build
```
#### The database tables will be automatically set up inside the container.  

---

## API Endpoints  

### Product Operations  
- **Add Product** (Experts Only) → `POST /api/product/add`  
- **Update Product** → `PUT /api/product/update/{id}`  
- **Delete Product** → `DELETE /api/product/delete/{id}`  
- **Get Product Details** → `GET /api/product/{id}`  

### Review & Rating Operations  
- **Add Review** → `POST /api/product/{id}/comment`  
- **Add Rating** → `POST /api/product/{id}/rating`  
- **Update Review** → `PUT /api/product/{id}/comment/{commentId}`  
- **Delete Review** → `DELETE /api/product/{id}/comment/{commentId}`  

### User Management  
- **User Registration** → `POST /api/user/register`  
- **User Login** → `POST /api/user/login`  
- **Update User Information** → `PUT /api/user/{id}`  
- **Get User Details** → `GET /api/user/{id}`  

### Admin Operations  
- **Get User List** → `GET /api/admin/user`  
- **Delete User** → `DELETE /api/admin/user/{id}`  
- **Delete Product** (Admins Only) → `DELETE /api/product/{id}`  

---

## Testing  

The project includes **unit and integration tests** using JUnit and Mockito.  
To run tests:  
```bash
mvn test
```

Tests are written for different application layers (**service, repository, controller**) to ensure stability.  

---

## Contributing  

If you’d like to contribute:  

1. Fork the repository.  
2. Create a new branch.  
3. Make your changes and commit them.  
4. Submit a **Pull Request**.  

---

## License  
This project is licensed under the [MIT License](LICENSE).  
