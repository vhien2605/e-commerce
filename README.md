# E-Commerce APIs
- This e-commerce APIs project includes almost full services of other ecommerce systems in real life.
## Technology Stack
- **Architecture**: Layer Architecture, Restful APIs
- **Framework**: Spring boot, Spring Security, Spring Data JPA
- **Database**: MySQL
- **Caching technology**: Redis
- **Containerization & Deployment**: Docker
- **Other Technologies**:Java Mail, Cloundinary APIs, VNPAY APIs
## Getting Started
### Dependencies
- Java 21+
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Redis
- Docker (optional), you can run this project by your local maven
### Environment Setup
When all your dependencies are ready,turn to enviroment setup step.
Set up all your properties as my one and create a `.env` file follow by below properties.

**To get VNPAY properties.Please go to https://sandbox.vnpayment.vn/apis/vnpay-demo/ for registration**

**To get JavaMail properties. Check out google mail service and generate your application password at https://myaccount.google.com/apppasswords**
- databasePassword=Yours
- databaseUrl=jdbc:mysql://localhost:3306/e-commerce-project
- databaseUsername=Yours
- redisHost=your redis host
- redisPassword=your redis password
- redisPort=your port
- accessKey=your key
- refreshKey=your key
- resetKey=your key
- cloudName=your cloud name
- apiKey=your key
- apiSecret=your secret
- vnPay.url=your url
- vnPay.tmnCode=your vnpay code
- vnPay.secretKey=vnpay secret
- vnPay.returnUrl=vnpay return url
- vnPay.version=version
- vnPay.command=pay
- vnPay.orderType=other
- mail.username=your username
- mail.password=your password
### Running the service
You can run this service by several ways
- Run by maven command (mvn spring-boot:run)
- Run in your IDE (vscode, IDE)
- Run in docker container


## Project Structure
```text
e-commerce/
├── .idea/
├── .mvn/
├── src/
│   └── main/
│       ├── java/
│       │   └── single/
│       │       └── project/
│       │           └── e_commerce/
│       │               ├── configuration/        # Project configuration
│       │               │   ├── filters/          # Security filter for authentication and authorization
│       │               │   ├── handlers/         # Handlers code for filters
│       │               │   └── securityModels/   # Security Model in Spring Security Context
│       │               │       ├── AsyncConfig        # Async config for multi-threading
│       │               │       ├── CloudinaryConfig   # File service config
│       │               │       ├── MailServerConfig   # Mail service config
│       │               │       ├── RedisConfig        # Redis config
│       │               │       ├── SecurityConfig     # General Security config
│       │               │       └── VNPayConfig        # VNPay APIs config
│       │               ├── controllers/           # Controller Layer
│       │               ├── dto/                   # Data Transfer Objects
│       │               ├── exceptions/            # Exception handling methods
│       │               ├── mappers/               # Mapper services
│       │               ├── models/                # Entities
│       │               ├── repositories/          # Persistent Layer
│       │               ├── services/              # Service Layer
│       │               ├── utils/                 # Utility Layer
│       │               └── ECommerceApplication   # Main application file
│       └── resources/
│           └── application.properties             # Application environment properties
```




