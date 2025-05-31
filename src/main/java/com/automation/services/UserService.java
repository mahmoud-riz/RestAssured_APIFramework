package com.automation.services;

import com.automation.models.User;
import com.automation.models.UserListResponse;
import com.automation.models.UserResponse;
import com.automation.utils.ConfigManager;
import com.automation.utils.RestAssuredConfigUtil;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;


@Slf4j
public class UserService {
    
    private final ConfigManager configManager;
    private final String usersEndpoint;
    
    public UserService() {
        this.configManager = ConfigManager.getInstance();
        this.usersEndpoint = configManager.getUsersEndpoint();
    }
    
    
    @Step("Create user with name: {user.name} and job: {user.job}")
    public Response createUser(User user) {
        log.info("Creating user with name: {} and job: {}", user.getName(), user.getJob());
        
        return given()
                .spec(RestAssuredConfigUtil.getCommonRequestSpec())
                .body(user)
                .when()
                .post(usersEndpoint)
                .then()
                .extract()
                .response();
    }
    
  
    @Step("Get user with ID: {userId}")
    public Response getUserById(String userId) {
        log.info("Retrieving user with ID: {}", userId);
        
        return given()
                .spec(RestAssuredConfigUtil.getCommonRequestSpec())
                .pathParam("id", userId)
                .when()
                .get(usersEndpoint + "/{id}")
                .then()
                .extract()
                .response();
    }
    

    @Step("Update user with ID: {userId}")
    public Response updateUser(String userId, User user) {
        log.info("Updating user with ID: {} with name: {} and job: {}", 
                userId, user.getName(), user.getJob());
        
        return given()
                .spec(RestAssuredConfigUtil.getCommonRequestSpec())
                .pathParam("id", userId)
                .body(user)
                .when()
                .put(usersEndpoint + "/{id}")
                .then()
                .extract()
                .response();
    }
    

    @Step("Delete user with ID: {userId}")
    public Response deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        
        return given()
                .spec(RestAssuredConfigUtil.getCommonRequestSpec())
                .pathParam("id", userId)
                .when()
                .delete(usersEndpoint + "/{id}")
                .then()
                .extract()
                .response();
    }
    

    @Step("Get list of users - page: {page}")
    public Response getUsersList(Integer page) {
        log.info("Retrieving users list for page: {}", page);
        
        if (page != null) {
            return given()
                    .spec(RestAssuredConfigUtil.getCommonRequestSpec())
                    .queryParam("page", page)
                    .when()
                    .get(usersEndpoint)
                    .then()
                    .extract()
                    .response();
        } else {
            return given()
                    .spec(RestAssuredConfigUtil.getCommonRequestSpec())
                    .when()
                    .get(usersEndpoint)
                    .then()
                    .extract()
                    .response();
        }
    }
    
   
    @Step("Get list of users")
    public Response getUsersList() {
        return getUsersList(null);
    }
    
 
    public User parseUserResponse(Response response) {
        return response.as(User.class);
    }
    
 
    public UserResponse parseUserResponseWrapper(Response response) {
        return response.as(UserResponse.class);
    }
    

    public UserListResponse parseUserListResponse(Response response) {
        return response.as(UserListResponse.class);
    }
} 