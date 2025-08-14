package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {



    private final Algorithm signingAlgorithm;

    public UserController(@Value("${jwt.signing-secret}") String signingSecret) {

        // this example uses a symmetric signature of the JWT token, but if you want the issuer and the
        // verifier of the JWT token to be different applications you may want to use an asymmetric
        // signature

        this.signingAlgorithm = Algorithm.HMAC256(signingSecret);
    }


    @GetMapping
    public String test() {
        return "Hello";
    }
    //  email --> itshaala@gmail.com, password : It-shaala@12345 --> TOKEN
    // otherwise --> message
    @PostMapping("/login")
    public String login(@RequestBody User user){

        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        if(user.getPassword().equals("It-shaala@12345") && user.getEmail().equals("itshaala@gmail.com") ){
            return  createJwtToken(user);
        }else{
            return "Invalid Credentials";
        }

    }


    @PostMapping("/todo")
    public String saveTodo( @RequestParam String todoTitle, @RequestParam boolean isDone,
                            @RequestHeader("Authorization") String token) throws  Exception{
        // verify is user authenticated or not
        // token
        System.out.println(token);

        String email = resolveJwtToken(token);

//

        return  email;
    }



    public String resolveJwtToken(String token) throws  Exception{
        try {
            JWTVerifier verifier = JWT.require(signingAlgorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            String email = decodedJWT.getSubject();
//            List<Role> roles = decodedJWT.getClaim(ROLES_CLAIM).asList(Role.class);

            return email;
        } catch (JWTVerificationException exception) {
            throw new Exception("JWT is not valid");
        }
    }



    public String createJwtToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 3600000; // 1 hour validity
        Date exp = new Date(expMillis);

        // List<String> roles = authUser.roles().stream().map(Role::name).toList();

        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(signingAlgorithm);
    }
}


