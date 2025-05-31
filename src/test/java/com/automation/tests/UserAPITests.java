package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.models.User;
import com.automation.models.UserListResponse;
import com.automation.models.UserResponse;
import com.automation.testdata.UserTestData;
import com.automation.utils.RetryUtil;
import io.qameta.allure.*;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


@Slf4j
@Epic("User Management API")
@Feature("User CRUD Operations")
public class UserAPITests extends BaseTest {
    
    private Integer createdUserId;
    private final RetryUtil retryUtil;
    
    public UserAPITests() {
        this.retryUtil = new RetryUtil();
    }
    
    @Test(priority = 1, description = "Create a new user successfully", 
          dataProvider = "validUsers", dataProviderClass = UserTestData.class)
    @Story("Create User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test creating a new user with valid name and job details")
    public void testCreateUser(String name, String job) {
        User newUser = User.builder()
                .name(name)
                .job(job)
                .build();
        
     
        Response response = userService.createUser(newUser);
        
      
        validateBasicResponse(response);
        validateStatusCode(response, 201);
        validateResponseTime(response, 5000);
        
       
        User createdUser = response.as(User.class);
        
       
        assertNotNull(createdUser.getId(), "Created user ID should not be null");
        assertEquals(createdUser.getName(), name, "User name should match");
        assertEquals(createdUser.getJob(), job, "User job should match");
        
       
        createdUserId = createdUser.getId();
        log.info("User created successfully with ID: {}", createdUserId);
    }
    
    @Test(priority = 2, description = "Create user with invalid data", dataProvider = "invalidUsers", dataProviderClass = UserTestData.class)
    @Story("Create User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test creating a user with invalid data")
    public void testCreateUserWithInvalidData(String name, String job) {
        User invalidUser = UserTestData.createUser(name, job);
        
       
        Response response = userService.createUser(invalidUser);
        
      
        validateBasicResponse(response);
        validateResponseTime(response, 5000);
        
      
        logResponseDetails(response);
        
        log.info("Response for invalid user creation: Status Code = {}", response.getStatusCode());
    }
    
    @Test(priority = 4, description = "Update user information successfully", 
          dataProvider = "updateUsers", dataProviderClass = UserTestData.class,
          dependsOnMethods = "testCreateUser")
    @Story("Update User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test updating user information with new name and job")
    public void testUpdateUser(String name, String job) {
        
        if (createdUserId == null) {
            User newUser = User.builder()
                    .name("Initial User")
                    .job("Initial Job")
                    .build();
            
            Response createResponse = userService.createUser(newUser);
            validateBasicResponse(createResponse);
            validateStatusCode(createResponse, 201);
            
            User createdUser = createResponse.as(User.class);
            createdUserId = createdUser.getId();
            log.info("Created new user for update test with ID: {}", createdUserId);
        }
        
    
        User updateData = User.builder()
                .name(name)
                .job(job)
                .build();
        
        log.info("Updating user with ID: {} with new name: {} and job: {}", 
                createdUserId, name, job);
        
        
        Response response = userService.updateUser(String.valueOf(createdUserId), updateData);
        
        
        validateBasicResponse(response);
        validateStatusCode(response, 200);
        validateResponseTime(response, 3000);
        
        
        User updatedUser = response.as(User.class);
        assertEquals(updatedUser.getName(), name, "Updated name should match");
        assertEquals(updatedUser.getJob(), job, "Updated job should match");
        assertNotNull(updatedUser.getUpdatedAt(), "Updated timestamp should be present");
        
        log.info("User updated successfully. Name: {}, Job: {}", 
                updatedUser.getName(), updatedUser.getJob());
    }
    
    @Test(priority = 5, description = "Delete user successfully", 
          dependsOnMethods = "testUpdateUser")
    @Story("Delete User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test deleting a user by their ID")
    public void testDeleteUser() {
        
        String userIdToDelete = (createdUserId != null) 
                                ? String.valueOf(createdUserId) : "2";  // Fallback to hardcoded ID
        
        log.info("Attempting to delete user with ID: {}", userIdToDelete);
        
       
        Response response = userService.deleteUser(userIdToDelete);
        
        
        validateBasicResponse(response);
        validateStatusCode(response, 204);
        validateResponseTime(response, 3000);
        
       
        assertTrue(response.getBody().asString().isEmpty() || 
                  response.getBody().asString().trim().isEmpty(),
                  "Response body should be empty for delete operation");
        
        log.info("User deleted successfully with ID: {}", userIdToDelete);
    }
    
    @Test(priority = 6, description = "Retrieve users list successfully")
    @Story("List Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test retrieving paginated list of users")
    public void testGetUsersList() {
        log.info("Retrieving list of users");
        
       
        Response response = userService.getUsersList();
        
      
        validateBasicResponse(response);
        validateStatusCode(response, 200);
        validateResponseTime(response, 3000);
        
      
        UserListResponse userList = response.as(UserListResponse.class);
        
        
        assertNotNull(userList.getData(), "User list should not be null");
        assertTrue(userList.getData().size() > 0, "User list should contain users");
        assertTrue(userList.getPage() > 0, "Page number should be greater than 0");
        assertTrue(userList.getPerPage() > 0, "Per page count should be greater than 0");
        assertTrue(userList.getTotal() > 0, "Total count should be greater than 0");
        assertTrue(userList.getTotalPages() > 0, "Total pages should be greater than 0");
        
       
        User firstUser = userList.getData().get(0);
        assertNotNull(firstUser.getId(), "User ID should be present");
        assertTrue(firstUser.getId() > 0, "User ID should be positive");
        assertNotNull(firstUser.getEmail(), "User email should be present");
        assertTrue(firstUser.getEmail().contains("@"), "Email should be valid");
        assertNotNull(firstUser.getFirstName(), "User first name should be present");
        assertNotNull(firstUser.getLastName(), "User last name should be present");
        assertNotNull(firstUser.getAvatar(), "User avatar should be present");
        assertTrue(firstUser.getAvatar().startsWith("https://"), "Avatar should be a valid HTTPS URL");
        
        
        validateJsonSchema(response, "schemas/user-list-schema.json");
        
        log.info("Successfully retrieved {} users from page {} of {}", 
                userList.getData().size(), userList.getPage(), userList.getTotalPages());
    }
    
    
    private void validateUserData(User user, String expectedName, String expectedJob) {
        assertNotNull(user, "User object should not be null");
        
        if (expectedName != null && !expectedName.trim().isEmpty()) {
            assertEquals(user.getName(), expectedName, "User name should match expected value");
        }
        
        if (expectedJob != null && !expectedJob.trim().isEmpty()) {
            assertEquals(user.getJob(), expectedJob, "User job should match expected value");
        }
        
        if (user.getEmail() != null) {
            assertTrue(user.getEmail().contains("@"), "Email should contain @ symbol");
        }
    }
}