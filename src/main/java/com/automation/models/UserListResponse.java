package com.automation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model class for user list response with pagination
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListResponse {
    
    @JsonProperty("page")
    private int page;
    
    @JsonProperty("per_page")
    private int perPage;
    
    @JsonProperty("total")
    private int total;
    
    @JsonProperty("total_pages")
    private int totalPages;
    
    @JsonProperty("data")
    private List<User> data;
    
    @JsonProperty("support")
    private Support support;
    
    /**
     * Support information from the API response
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Support {
        @JsonProperty("url")
        private String url;
        
        @JsonProperty("text")
        private String text;
    }
} 