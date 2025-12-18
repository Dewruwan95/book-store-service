package com.application.bookstore.repository;

import com.application.bookstore.model.AppUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import javax.sql.DataSource;
import java.util.List;

@DataJpaTest
@AutoConfigureMockMvc(addFilters = false)
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {

        AppUser admin = new AppUser();
        admin.setUsername("admin_user");
        admin.setPassword("admin123");
        admin.setRoles("ADMIN");

        appUserRepository.save(admin);

        // Create second user
        AppUser user = new AppUser();
        user.setUsername("customer_user");
        user.setPassword("customer123");
        user.setRoles("USER");

        appUserRepository.save(user);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(appUserRepository);
        Assertions.assertNotNull(dataSource);
    }

    @Test
    void should_return_all_users() {
        List<AppUser> result = appUserRepository.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
    }

}