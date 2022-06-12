package ru.antipant.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private String name;
    private String job;
    private String id;
    private String updatedAt;
    private String createdAt;
}
