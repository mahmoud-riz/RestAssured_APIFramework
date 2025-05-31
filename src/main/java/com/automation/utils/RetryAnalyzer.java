package com.automation.utils;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for handling flaky tests
 */
@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private final int maxRetryCount;
    
    public RetryAnalyzer() {
        this.maxRetryCount = ConfigManager.getInstance().getRetryCount();
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            log.warn("Retrying test '{}' - Attempt {} of {}", 
                    result.getMethod().getMethodName(), retryCount, maxRetryCount);
            
            // Wait before retry
            try {
                Thread.sleep(ConfigManager.getInstance().getRetryInterval());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted during retry wait", e);
            }
            
            return true;
        }
        return false;
    }
} 