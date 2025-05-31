package com.automation.utils;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Utility class for handling test retries
 */
@Slf4j
public class RetryUtil implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private final int maxRetryCount;
    private final int retryInterval;
    
    public RetryUtil() {
        ConfigManager configManager = ConfigManager.getInstance();
        this.maxRetryCount = configManager.getRetryCount();
        this.retryInterval = configManager.getRetryInterval();
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            log.warn("Retrying test '{}' - Attempt {} of {}", 
                    result.getMethod().getMethodName(), retryCount, maxRetryCount);
            
            // Wait before retry
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted during retry wait", e);
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * Reset retry count
     */
    public void resetRetryCount() {
        retryCount = 0;
    }
    
    /**
     * Get current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
    
    /**
     * Get maximum retry count
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }
} 