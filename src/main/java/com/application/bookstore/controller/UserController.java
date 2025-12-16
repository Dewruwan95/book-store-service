//package com.application.bookstore.controller;
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//
//@RestController
//@SecurityRequirement(name = "basicAuth")
//public class UserController {
//
//    @GetMapping("/home")
//    public String home(Principal principal) {
//        return "Home " + extractUser(principal);
//    }
//
//    @GetMapping("/hello")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String hello(Principal principal) {
//        return "Hello " + extractUser(principal);
//    }
//
//
//    @GetMapping("/world")
//    @PreAuthorize("hasRole('MANAGER','USER')")
//    public String world(Principal principal) {
//        return "World " + extractUser(principal);
//    }
//
//    private String extractUser(Principal principal) {
//        return principal != null ? principal.getName() : "Anonymous";
//    }
//}
