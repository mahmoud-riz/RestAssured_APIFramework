package com.automation.base;

import com.automation.services.UserService;
import com.automation.utils.ConfigManager;
import com.automation.utils.RestAssuredConfigUtil;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;


@Slf4j
public abstract class BaseTest {
    
    protected UserService userService;
    protected ConfigManager configManager;
    protected SoftAssert softAssert;
    
    @BeforeClass(alwaysRun = true)
    @Step("Initialize test framework")
    public void setUpClass() {
        log.info("Setting up test framework...");
        
       
        configManager = ConfigManager.getInstance();
        
       
        RestAssuredConfigUtil.configureRestAssured();
        
       
        userService = new UserService();
        
        log.info("Test framework setup completed successfully");
    }
    
    @BeforeMethod(alwaysRun = true)
    @Step("Initialize test method: {method.name}")
    public void setUpMethod(Method method) {
        log.info("Starting test method: {}", method.getName());
        softAssert = new SoftAssert();
    }
    
    @AfterMethod(alwaysRun = true)
    @Step("Cleanup test method")
    public void tearDownMethod(Method method) {
        log.info("Completed test method: {}", method.getName());
        
       
        if (softAssert != null) {
            softAssert.assertAll();
        }
    }
    
  
    
   
    @Step("Validate response status code is {expectedStatusCode}")
    protected void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        log.info("Validating status code. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
        assertEquals(actualStatusCode, expectedStatusCode, 
                "Status code validation failed");
    }
    
   
    @Step("Validate response time is less than {maxTimeInMs} ms")
    protected void validateResponseTime(Response response, long maxTimeInMs) {
        long responseTime = response.getTime();
        log.info("Response time: {} ms (max allowed: {} ms)", responseTime, maxTimeInMs);
        assertTrue(responseTime < maxTimeInMs, 
                "Response time " + responseTime + "ms exceeded maximum allowed " + maxTimeInMs + "ms");
    }
    
    
    @Step("Validate response contains required fields")
    protected void validateResponseFields(Response response, String... requiredFields) {
        for (String field : requiredFields) {
            log.debug("Validating presence of field: {}", field);
            response.then().body(field, notNullValue());
        }
    }
    
   
    @Step("Validate response against JSON schema")
    protected void validateJsonSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            log.info("JSON schema validation passed for schema: {}", schemaPath);
        } catch (Exception e) {
            log.error("JSON schema validation failed for schema: {}", schemaPath, e);
            fail("JSON schema validation failed: " + e.getMessage());
        }
    }
    
   
    @Step("Log response details")
    protected void logResponseDetails(Response response) {
        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Time: {} ms", response.getTime());
        log.info("Response Body: {}", response.getBody().asString());
        log.info("Response Headers: {}", response.getHeaders().toString());
    }
    
   
    @Step("Validate basic response properties")
    protected void validateBasicResponse(Response response) {
        assertNotNull(response, "Response should not be null");
        assertTrue(response.getStatusCode() > 0, "Status code should be greater than 0");
        assertNotNull(response.getBody(), "Response body should not be null");
    }
    
   
    protected void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread interrupted during wait", e);
        }
    }
    
    
    protected String generateUniqueTestData(String prefix) {
        return prefix + "_" + System.currentTimeMillis();
    }
    
    protected String generateRandomEmail() {
        return "test_" + System.currentTimeMillis() + "@automation.com";
    }
} 