package com.application.bookstore.controller;

import com.application.bookstore.dto.AppUserDto;
import com.application.bookstore.dto.AppUserRequestDto;
import com.application.bookstore.service.AppUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/api/book-store-service/v1/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    //------------------- Get All Users ------------------------
    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAll() {
        List<AppUserDto> response = appUserService.getAll();

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    //------------------- Get Single User By Id ------------------------
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getOneById(@PathVariable int id) {
        AppUserDto response = appUserService.getById(id);
        return ResponseEntity.ok(response);
    }

    //------------------- Create new User ------------------------
    @PostMapping
    public ResponseEntity<AppUserDto> create(@RequestBody AppUserRequestDto appUserRequestDto) {
        AppUserDto response = appUserService.create(appUserRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //------------------- Update User By Id ------------------------
    @PutMapping("/{id}")
    public ResponseEntity<AppUserDto> update(@PathVariable int id, @RequestBody AppUserRequestDto appUserRequestDto) {
        AppUserDto response = appUserService.update(id, appUserRequestDto);
        return ResponseEntity.ok(response);
    }

    //------------------- Delete User By Id ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
