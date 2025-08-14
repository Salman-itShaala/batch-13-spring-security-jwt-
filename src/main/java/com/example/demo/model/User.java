package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Setter
@Getter
public class User {
    private String id;
    private String email;
    private String password;

    public String getEmail(){
        return  this.email;
    }

    public String getPassword(){
        return  this.password;
    }

}
