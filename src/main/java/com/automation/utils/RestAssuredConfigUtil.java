package com.automation.utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RestAssuredConfigUtil {
    private static final ConfigManager configManager = ConfigManager.getInstance();
    private static boolean isConfigured = false;

    public static void configureRestAssured() {
        if (isConfigured) {
            return;
        }


        RestAssured.baseURI = configManager.getBaseUrl();
        log.info("RestAssured base URI set to: {}", RestAssured.baseURI);

  
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", configManager.getConnectionTimeout())
                        .setParam("http.socket.timeout", configManager.getRequestTimeout())
                        .setParam("http.connection-manager.timeout", configManager.getRequestTimeout()))
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
                        .enablePrettyPrinting(true));


        RestAssured.filters(new AllureRestAssured());

        isConfigured = true;
        log.info("RestAssured configuration completed successfully");
    }

  
    public static RequestSpecification getCommonRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "RestAssured-Automation-Framework/1.0.0")
                .addHeader("x-api-key", configManager.getApiToken())
                .addHeader("Authorization", "Bearer " + configManager.getApiToken())
                .log(LogDetail.ALL)
                .build();
    }


    public static ResponseSpecification getSuccessResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }


    public static ResponseSpecification getCreatedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }


    public static ResponseSpecification getDeletedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(204)
                .log(LogDetail.ALL)
                .build();
    }


    public static ResponseSpecification getNotFoundResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .log(LogDetail.ALL)
                .build();
    }


    public static ResponseSpecification getBadRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .log(LogDetail.ALL)
                .build();
    }


    public static ResponseSpecification getUnauthorizedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .log(LogDetail.ALL)
                .build();
    }

   
    public static void resetRestAssured() {
        RestAssured.reset();
        isConfigured = false;
        log.info("RestAssured configuration reset");
    }
} 