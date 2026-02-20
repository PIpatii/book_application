📚 Book Store API

    🌟 Introduction
Book Store API is a modern e-commerce REST API for managing a bookstore, built using Spring Boot 3.
The project’s goal was to create a flexible, scalable, and secure backend for handling books, categories, users, shopping carts, orders, and authentication

    ⚙️ Technologies & Tools
| Category | Tools / Frameworks |
| --- | --- |
| Core Framework | Spring Boot 3 |
| Security & Auth | Spring Security, JWT |
| Data Access | Spring Data JPA, Hibernate |
| Database | MySQL 8 (via Docker) |
| Object Mapping | MapStruct |
| Validation | Jakarta Validation, Custom @FieldMatch |
| Documentation | Swagger / OpenAPI |
| Containerization | Docker, Docker Compose |
| Build Tool | Maven |
| Language | Java 17 |
| Other | Lombok, JPA Specifications API |

    🔐Authentication & Authorization
Authentication and authorization are implemented using JWT (JSON Web Token):

POST /auth/registration — register a new user
POST /auth/login — authenticate and retrieve a JWT token
Each protected endpoint must include the JWT in the header:


Authorization: Bearer <token>

Roles:

USER — can browse books, manage cart, place orders
ADMIN — can perform CRUD on books and categories, update order statuses

    🧩Main Features by Controller

📘 BookController
| Endpoint | Method | Access | Description |
| --- | --- | --- | --- |
| /books | GET | All | Retrieve all books (paginated) |
| /books/search | GET | USER | Search by title, author, or ISBN |
| /books/{id} | GET | USER | Retrieve a book by ID |
| /books | POST | ADMIN | Create a new book |
| /books/{id} | PUT | ADMIN | Update an existing book |
| /books/{id} | DELETE | ADMIN | Delete a book |

📂 CategoryController
| Endpoint | Method | Access | Description |
| --- | --- | --- | --- |
| /categories | GET | USER | Retrieve all categories |
| /categories/{id} | GET | USER | Retrieve a specific category |
| /categories/{id}/books | GET | USER | Retrieve books in category |
| /categories | POST | ADMIN | Create a new category |
| /categories/{id} | PUT | ADMIN | Update a category |
| /categories/{id} | DELETE | ADMIN | Delete a category |

🛒 CartController
| Endpoint | Method | Description |
| --- | --- | --- |
| /carts | GET | Get current user’s shopping cart |
| /carts | POST | Add a book to the cart |
| /carts/items/{id} | PUT | Update item quantity |
| /carts/items/{id} | DELETE | Remove item from cart |

📦 OrderController
| Endpoint | Method | Description |
| --- | --- | --- |
| /orders | POST | Create an order from the current user’s cart |
| /orders | GET | Retrieve all orders of the current user |
| /orders/{id} | PATCH | (ADMIN) Update order status |
| /orders/{id}/items | GET | Get all items in the order |
| /orders/{id}/items/{itemId} | GET | Get a specific order item |

👤 AuthController
| Endpoint | Method | Description |
| --- | --- | --- |
| /auth/registration | POST | Register a new user |
| /auth/login | POST | Authenticate and retrieve JWT |

    🗃 Database Model (Simplified)
![Database Model](https://github.com/user-attachments/assets/7274cd05-f71b-41a0-9ee0-09fd721f1b2e)

    🧾 .env Example
MYSQLDB_PASSWORD=yourpassword
MYSQLDB_DATABASE=bookstore_db
MYSQLDB_LOCAL_PORT=3306
SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
JWT_SECRET=your_secret_key_here
JWT_EXPIRATION=86400000

     Swagger UI will be available at:
👉 http://localhost:8080/swagger-ui/index.html

    🧩 MapStruct Integration
Mapping between Entities and DTOs is handled by MapStruct, ensuring:

  Clean separation between layers
  Minimal boilerplate
  High performance conversions
  
Main mappers include:
BookMapper, UserMapper, CategoryMapper, OrderMapper, ShoppingCartMapper, and CartItemsMapper.

    ⚠️ Error Handling
The global error handler CustomGlobalExceptionHandler:

Handles validation errors (@Valid)
Returns custom JSON responses with timestamps
Catches EntityNotFoundException and RegistrationException

    🚧 Challenges & Solutions
| Challenge | Solution |
| --- | --- |
| Secure authentication and authorization | JWT with role-based access control |
| Avoiding duplicate cart items | Logic for merging existing items in ShoppingCartServiceImpl |
| Dynamic search implementation | Utilized JPA Specifications API |
| Field validation across DTOs | Custom @FieldMatch annotation |
| Secure password storage | Used BCryptPasswordEncoder |
| Reducing repetitive mapping code | Integrated MapStruct for DTO ↔ Entity conversions |

    🎬 Demo Video
You can watch a short demo of the Book Store API here:  
👉 https://youtu.be/T5uVvfL1RhE


👤 Author
Petro Ipatii
📧 petroipatiy@gmail.com
