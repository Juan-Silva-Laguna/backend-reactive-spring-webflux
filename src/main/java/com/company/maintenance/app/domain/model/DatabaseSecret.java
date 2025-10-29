package com.company.maintenance.app.domain.model;


import lombok.Data;

@Data
public class DatabaseSecret {
    private String username;
    private String password;
    private String host;
    private String port;
}
