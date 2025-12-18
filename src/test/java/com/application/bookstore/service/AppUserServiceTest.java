package com.application.bookstore.service;

import com.application.bookstore.dto.AppUserDto;
import com.application.bookstore.dto.AppUserRequestDto;
import com.application.bookstore.exception.AttributeAlreadyExistsException;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.AppUser;
import com.application.bookstore.repository.AppUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
class AppUserServiceTest {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private AppUserService appUserService;

    @BeforeEach
    void beforeEachTest() {
        appUserRepository = Mockito.mock(AppUserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        appUserService = new AppUserService(appUserRepository, passwordEncoder);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(appUserService);
        Assertions.assertNotNull(appUserRepository);
        Assertions.assertNotNull(passwordEncoder);
    }

    @Test
    void should_return_all_users() {
        Mockito.when(appUserRepository.findAll()).thenReturn(dummyUsers());

        List<AppUserDto> result = appUserService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals(1, result.getFirst().getId());
        Assertions.assertEquals("admin", result.getFirst().getUsername());
        Assertions.assertEquals("ADMIN", result.getFirst().getRoles());

        Assertions.assertEquals(2, result.get(1).getId());
        Assertions.assertEquals("user", result.get(1).getUsername());
        Assertions.assertEquals("USER", result.get(1).getRoles());
    }

    @Test
    void should_create_new_user() {
        AppUserRequestDto request = new AppUserRequestDto();
        request.setUsername("new_user");
        request.setPassword("newpass123");
        request.setRoles("USER");

        AppUser savedUser = new AppUser();
        savedUser.setId(3);
        savedUser.setUsername("new_user");
        savedUser.setPassword("encoded_password");
        savedUser.setRoles("USER");

        Mockito.when(appUserRepository.findByUsername("new_user")).thenReturn(null);
        Mockito.when(passwordEncoder.encode("newpass123")).thenReturn("encoded_password");
        Mockito.when(appUserRepository.save(Mockito.any(AppUser.class))).thenReturn(savedUser);

        AppUserDto result = appUserService.create(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.getId());
        Assertions.assertEquals("new_user", result.getUsername());
        Assertions.assertEquals("USER", result.getRoles());
    }

    @Test
    void should_throw_validation_exception_when_username_is_null() {
        AppUserRequestDto request = new AppUserRequestDto();
        request.setUsername(null);
        request.setPassword("password123");
        request.setRoles("USER");

        Assertions.assertThrows(ValidationException.class, () -> {
            appUserService.create(request);
        });
    }

    @Test
    void should_throw_validation_exception_when_password_is_null() {
        AppUserRequestDto request = new AppUserRequestDto();
        request.setUsername("test_user");
        request.setPassword(null);
        request.setRoles("USER");

        Assertions.assertThrows(ValidationException.class, () -> {
            appUserService.create(request);
        });
    }

    @Test
    void should_throw_validation_exception_when_roles_is_null() {
        AppUserRequestDto request = new AppUserRequestDto();
        request.setUsername("test_user");
        request.setPassword("password123");
        request.setRoles(null);

        Assertions.assertThrows(ValidationException.class, () -> {
            appUserService.create(request);
        });
    }

    @Test
    void should_throw_already_exist_exception_when_username_already_exists() {
        AppUserRequestDto request = new AppUserRequestDto();
        request.setUsername("admin_user");
        request.setPassword("password123");
        request.setRoles("ADMIN");

        AppUser existingUser = new AppUser();
        existingUser.setId(1);
        existingUser.setUsername("admin_user");
        existingUser.setPassword("encoded_pass");
        existingUser.setRoles("ADMIN");

        Mockito.when(appUserRepository.findByUsername("admin_user")).thenReturn(existingUser);

        Assertions.assertThrows(AttributeAlreadyExistsException.class, () -> {
            appUserService.create(request);
        });
    }

    private static List<AppUser> dummyUsers() {
        AppUser admin = new AppUser();
        admin.setId(1);
        admin.setUsername("admin");
        admin.setPassword("encoded_admin_password");
        admin.setRoles("ADMIN");

        AppUser user = new AppUser();
        user.setId(2);
        user.setUsername("user");
        user.setPassword("encoded_user_password");
        user.setRoles("USER");

        return List.of(admin, user);
    }
}