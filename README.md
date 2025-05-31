# RestAssured API Automation Framework

A comprehensive REST API automation framework built with **RestAssured**, **TestNG**, and **Maven** following **Page Object Model (POM)** design pattern and **layered architecture**.

## 🚀 Features

- ✅ **RestAssured** 
- ✅ **TestNG**
- ✅ **Maven** 
- ✅ **POM Design Pattern** 
- ✅ **Layered Architecture**
- ✅ **Configuration Management**
- ✅ **Allure Reporting** 
- ✅ **Logging** 
- ✅ **Error Handling** 
- ✅ **JSON Schema Validation**
- ✅ **Parallel Test Execution**

## 🏗️ Project Structure

```
restassured-automation-framework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/automation/
│   │   │       ├── exceptions/          # Custom exceptions
│   │   │       │   └── APIException.java
│   │   │       ├── models/              # POJO classes for request/response
│   │   │       │   ├── User.java
│   │   │       │   ├── UserResponse.java
│   │   │       │   └── UserListResponse.java
│   │   │       ├── services/            # API service classes (POM pattern)
│   │   │       │   └── UserService.java
│   │   │       └── utils/               # Utility classes
│   │   │           ├── ConfigManager.java
│   │   │           ├── RestAssuredConfigUtil.java
│   │   │           └── RetryAnalyzer.java
│   │   └── resources/
│   │       ├── config.properties        # Configuration properties
│   │       └── logback.xml             # Logging configuration
│   └── test/
│       ├── java/
│       │   └── com/automation/
│       │       ├── base/                # Base test classes
│       │       │   └── BaseTest.java
│       │       └── tests/               # Test classes
│       │           └── UserAPITests.java
│       └── resources/
│           └── testng.xml              # TestNG configuration
├── target/                             # Build artifacts
├── logs/                              # Log files
├── pom.xml                           # Maven configuration
└── README.md                         # Project documentation
```

## 🛠️ Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Git** (for version control)
- Internet connection (for downloading dependencies)

## 📥 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd restassured-automation-framework
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Verify Installation
```bash
mvn test -Dtest=UserAPITests#testGetUsersList
```

## 🚀 Running Tests

### Execute All Tests
```bash
mvn test
```

### Execute Specific Test Class
```bash
mvn test -Dtest=UserAPITests
```

### Execute Specific Test Method
```bash
mvn test -Dtest=UserAPITests#testCreateUser
```

### Execute with TestNG XML
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Execute Smoke Tests
```bash
mvn test -Dgroups=smoke
```

### Execute with Different Environment
```bash
mvn test -Dapi.base.url=https://different-api.com
```

## 🧪 Test Scenarios

The framework implements the following test scenarios for the **ReqRes API** (https://reqres.in):

### 1. **Create User** (`POST /api/users`)
- ✅ Create user with valid name and job
- ✅ Validate response contains user ID and timestamps
- ✅ Verify status code 201

### 2. **Retrieve User** (`GET /api/users/{id}`)
- ✅ Get user by valid ID
- ✅ Validate user data structure
- ✅ Verify status code 200

### 3. **Update User** (`PUT /api/users/{id}`)
- ✅ Update user with new name and job
- ✅ Validate updated fields
- ✅ Verify status code 200

### 4. **Delete User** (`DELETE /api/users/{id}`)
- ✅ Delete user by ID
- ✅ Verify empty response body
- ✅ Verify status code 204

### 5. **List Users** (`GET /api/users`)
- ✅ Retrieve paginated user list
- ✅ Validate pagination metadata
- ✅ Verify user data structure

### 6. **Error Handling**
- ✅ Handle non-existent user (404)
- ✅ Handle invalid requests
- ✅ Validate error responses

## 📊 Reporting

### Generate Allure Reports
```bash
# Run tests and generate Allure results
mvn test

# Generate and serve Allure report
mvn allure:serve
```

### View Test Logs
```bash
# View real-time logs
tail -f logs/automation.log

# View specific log level
grep "ERROR" logs/automation.log
```

## ⚙️ Configuration

### Environment Properties (`src/main/resources/config.properties`)
```properties
# API Configuration
api.base.url=https://reqres.in
api.users.endpoint=/api/users

# Timeouts
request.timeout=30000
connection.timeout=10000

# Retry Configuration
retry.count=2
retry.interval=1000
```

### Override Properties via Command Line
```bash
mvn test -Dapi.base.url=https://staging-api.com -Drequest.timeout=60000
```

## 🔧 Framework Components

### **Service Layer (POM Pattern)**
- `UserService.java` - Encapsulates all user-related API operations
- Methods for CRUD operations and response parsing
- Allure step annotations for reporting

### **Model Layer**
- `User.java` - User entity with builder pattern
- `UserResponse.java` - Single user response wrapper
- `UserListResponse.java` - Paginated user list response

### **Utility Layer**
- `ConfigManager.java` - Configuration management singleton
- `RestAssuredConfigUtil.java` - RestAssured configuration
- `RetryAnalyzer.java` - Test retry mechanism

## 🐛 Troubleshooting

### Common Issues

1. **Tests failing with connection timeout**
   ```bash
   # Increase timeout in config.properties
   request.timeout=60000
   connection.timeout=30000
   ```

2. **Maven dependency issues**
   ```bash
   mvn clean install -U
   ```

3. **Java version issues**
   ```bash
   java -version
   export JAVA_HOME=/path/to/java21
   ```

### Debug Mode
```bash
mvn test -X -Dlog.level=DEBUG
```

## 📈 Best Practices Implemented

1. **Separation of Concerns** - Clear layer separation
2. **DRY Principle** - Reusable components and utilities
3. **Error Handling** - Comprehensive exception handling
4. **Logging** - Detailed logging for debugging
5. **Configuration Management** - Externalized configuration
6. **Test Data Management** - Dynamic test data generation
7. **Reporting** - Rich Allure reports with steps
8. **Maintainability** - Clean, readable code structure
