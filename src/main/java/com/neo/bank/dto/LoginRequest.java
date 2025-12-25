package com.neo.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
    public boolean isValid(){
        if(username == null || password == null){
            return false;
        }else{
            return  true;
        }
    }
}
