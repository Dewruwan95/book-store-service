package com.application.bookstore.service;

import com.application.bookstore.dto.AppUserDto;
import com.application.bookstore.dto.AppUserRequestDto;
import com.application.bookstore.exception.AttributeAlreadyExistsException;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.AppUser;
import com.application.bookstore.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //--------------------------------------------------------------
    //------------------- Get All AppUsers -------------------------
    //--------------------------------------------------------------
    public List<AppUserDto> getAll() {
        logger.info("Fetching all users");
        return toDto(appUserRepository.findAll());
    }

    //--------------------------------------------------------------
    //------------------- Get Single AppUser By Id -----------------
    //--------------------------------------------------------------
    public AppUserDto getById(int id) {
        logger.info("Fetching user with ID: {}", id);
        return appUserRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with id " + id);
                });
    }

    //--------------------------------------------------------------
    //------------------- Create New AppUser -----------------------
    //--------------------------------------------------------------
    public AppUserDto create(AppUserRequestDto appUserRequestDto) {

        logger.info("Creating new user with username: {}", appUserRequestDto.getUsername());

        validateAppUserRequestDto(appUserRequestDto);

        AppUser user = toEntity(appUserRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        final AppUser savedUser = appUserRepository.save(user);
        logger.info("User created successfully with ID: {} and username: {}", savedUser.getId(), savedUser.getUsername());
        return toDto(savedUser);
    }

    //--------------------------------------------------------------
    //------------------- Update AppUser ---------------------------
    //--------------------------------------------------------------
    public AppUserDto update(int id, AppUserRequestDto appUserRequestDto) {
        logger.info("Updating user with ID: {}", id);
        AppUser existingAppUser = appUserRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot update: User not found with ID: {}", id);
                    return new EntityNotFoundException("AppUser not found with id " + id);
                });

        if (appUserRequestDto.getUsername() != null) {
            existingAppUser.setUsername(appUserRequestDto.getUsername());
        }
        if (appUserRequestDto.getPassword() != null) {
            existingAppUser.setPassword(passwordEncoder.encode(appUserRequestDto.getPassword()));
        }
        if (appUserRequestDto.getRoles() != null) {
            existingAppUser.setRoles(appUserRequestDto.getRoles());
        }

        final AppUser savedUser = appUserRepository.save(existingAppUser);
        logger.info("User updated successfully with ID: {}", savedUser.getId());
        return toDto(savedUser);
    }

    //--------------------------------------------------------------
    //------------------- Delete User ------------------------------
    //--------------------------------------------------------------
    public void delete(int id) {
        logger.info("Deleting user with ID: {}", id);
        if (!appUserRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent user ID: {}", id);
            throw new EntityNotFoundException("AppUser not found with id " + id);
        }
        appUserRepository.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }



    //-------------------------------------------------------------------
    // ------------ Load AppUser By Username ----------------------------
    //-------------------------------------------------------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.debug("Loading user by username: {}", username);

        final AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            logger.warn("User not found for username: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        logger.debug("User found for authentication: {} with roles: {}", username, appUser.getRoles());
        return User.withUsername(appUser.getUsername()).password(appUser.getPassword()).roles(appUser.getRoles().split(",")).build();
    }



    //--------------------------------------------------------------
    //------------------- Validate AppUserRequestDto ---------------
    //--------------------------------------------------------------
    private void validateAppUserRequestDto(AppUserRequestDto appUserRequestDto) {
        if (appUserRequestDto.getUsername() == null) {
            throw new ValidationException("username");
        }

        if (appUserRequestDto.getPassword() == null ) {
            throw new ValidationException("password");
        }

        if (appUserRequestDto.getRoles() == null ) {
            throw new ValidationException("roles");
        }

        // Check if username already exists in database
        if (appUserRepository.findByUsername(appUserRequestDto.getUsername()) != null) {
            throw new AttributeAlreadyExistsException("AppUser", "username", appUserRequestDto.getUsername());
        }
    }

    //--------------------------------------------------------------
    //----------------- Convert AppUser to AppUserDto --------------
    //--------------------------------------------------------------
    public List<AppUserDto> toDto(List<AppUser> users) {
        return users.stream().map(this::toDto).toList();
    }

    private AppUserDto toDto(AppUser user) {
        if (user == null) {
            return null;
        }
        AppUserDto result = new AppUserDto();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setRoles(user.getRoles());

        return result;
    }

    //-------------------------------------------------------------------
    // ------------ convert AppUserRequestDto to AppUser ----------------
    //-------------------------------------------------------------------
    private AppUser toEntity(AppUserRequestDto appUserRequestDto) {
        if (appUserRequestDto == null) {
            return null;
        }
        AppUser result = new AppUser();
        result.setUsername(appUserRequestDto.getUsername());
        result.setPassword(appUserRequestDto.getPassword());
        result.setRoles(appUserRequestDto.getRoles().toUpperCase());

        return result;
    }


}
