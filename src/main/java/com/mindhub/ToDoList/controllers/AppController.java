package com.mindhub.ToDoList.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppController {


    @GetMapping("/user")
    public String getUsername(Authentication authentication){

        return authentication.getName();
    }

    @GetMapping("/admin")
    public String getAdminName(Authentication authentication){

        return authentication.getName();
    }

    @Operation(summary = "Public greeting endpoint", description = "This endpoint is public and returns a simple greeting message.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Greeting message returned successfully.")
    })
    @GetMapping("/public/welcome")
    public ResponseEntity<?> getGreeting(){
        return ResponseEntity.ok("You can see this because it is a public endpoint");
    }

}
