package com.automation.testdata;

import com.automation.models.User;
import org.testng.annotations.DataProvider;

/**
 * Test data provider class for User API tests
 */
public class UserTestData {
    
    @DataProvider(name = "validUsers")
    public static Object[][] getValidUsers() {
        return new Object[][] {
            {"John Doe", "Software Engineer"},
            {"Jane Smith", "QA Engineer"},
            {"Bob Johnson", "Product Manager"},
            {"Alice Brown", "DevOps Engineer"}
        };
    }
    
    @DataProvider(name = "invalidUsers")
    public static Object[][] getInvalidUsers() {
        return new Object[][] {
            {null, null},
            {"", ""},
            {"Mahmoud", null},
            {null, "Engineer"},
            {"", "Tester"},
            {"John", ""}
        };
    }
    
    @DataProvider(name = "updateUsers")
    public static Object[][] getUpdateUsers() {
        return new Object[][] {
            {"Mahmoud Updated", "Senior Software Engineer"},
            {"Sami Updated", "Lead QA Engineer"},
            {"Rizk Updated", "Senior Product Manager"},
            {"Ageena Updated", "Senior DevOps Engineer"}
        };
    }
    
    @DataProvider(name = "paginationData")
    public static Object[][] getPaginationData() {
        return new Object[][] {
            {1},
            {2},
            {3},
            {4}
        };
    }
    
    /**
     * Create a user object with the given name and job
     */
    public static User createUser(String name, String job) {
        return User.builder()
                .name(name)
                .job(job)
                .build();
    }
    
    /**
     * Create a user with random data
     */
    public static User createRandomUser() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return User.builder()
                .name("TestUser_" + timestamp)
                .job("TestJob_" + timestamp)
                .build();
    }
} 