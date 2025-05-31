package com.automation.listeners;

import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        log.info("Starting test: {} in class: {}", 
                result.getMethod().getMethodName(),
                result.getTestClass().getName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {} in class: {}", 
                result.getMethod().getMethodName(),
                result.getTestClass().getName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {} in class: {}", 
                result.getMethod().getMethodName(),
                result.getTestClass().getName());
        
        // Log the exception
        if (result.getThrowable() != null) {
            log.error("Failure reason: {}", result.getThrowable().getMessage());
            saveFailureScreenshot(result);
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {} in class: {}", 
                result.getMethod().getMethodName(),
                result.getTestClass().getName());
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("Test failed but within success percentage: {} in class: {}", 
                result.getMethod().getMethodName(),
                result.getTestClass().getName());
    }
    
    @Override
    public void onStart(ITestContext context) {
        log.info("Starting test suite: {}", context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        log.info("Finished test suite: {}", context.getName());
    }
    
    @Attachment(value = "Failure Screenshot", type = "text/plain")
    private String saveFailureScreenshot(ITestResult result) {
        StringBuilder failureDetails = new StringBuilder();
        failureDetails.append("Test Method: ").append(result.getMethod().getMethodName()).append("\n");
        failureDetails.append("Test Class: ").append(result.getTestClass().getName()).append("\n");
        failureDetails.append("Failure Time: ").append(result.getEndMillis()).append("\n");
        
        if (result.getThrowable() != null) {
            failureDetails.append("Failure Reason: ").append(result.getThrowable().getMessage()).append("\n");
            failureDetails.append("Stack Trace:\n").append(getStackTrace(result.getThrowable()));
        }
        
        return failureDetails.toString();
    }
    
    private String getStackTrace(Throwable throwable) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            stackTrace.append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }
} 
