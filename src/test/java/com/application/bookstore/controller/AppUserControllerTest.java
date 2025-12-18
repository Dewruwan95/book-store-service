package com.application.bookstore.controller;

import com.application.bookstore.dto.AppUserDto;
import com.application.bookstore.dto.AppUserRequestDto;
import com.application.bookstore.service.AppUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AppUserController.class)
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppUserService appUserService;

    @Test
    void should_return_all_users() throws Exception {
        Mockito.when(appUserService.getAll()).thenReturn(dummyUserDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].username").value("admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].roles").value("ADMIN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].username").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].roles").value("USER"));
    }

    @Test
    void should_return_no_content_when_no_users() throws Exception {
        Mockito.when(appUserService.getAll()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void should_get_user_by_id() throws Exception {
        AppUserDto userDto = new AppUserDto(1, "admin", "ADMIN");
        Mockito.when(appUserService.getById(1)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").value("ADMIN"));
    }

    @Test
    void should_create_new_user() throws Exception {
        AppUserRequestDto inputUserDto = new AppUserRequestDto();
        inputUserDto.setUsername("new_user");
        inputUserDto.setPassword("newpass123");
        inputUserDto.setRoles("USER");

        AppUserDto responseDto = new AppUserDto(3, "new_user", "USER");

        Mockito.when(appUserService.create(Mockito.any(AppUserRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book-store-service/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(inputUserDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("new_user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").value("USER"));
    }

    @Test
    void should_update_user() throws Exception {
        AppUserRequestDto updateUserDto = new AppUserRequestDto();
        updateUserDto.setUsername("updated_user");
        updateUserDto.setPassword("updatedpass123");
        updateUserDto.setRoles("ADMIN,USER");

        AppUserDto responseDto = new AppUserDto(1, "updated_user", "ADMIN,USER");

        Mockito.when(appUserService.update(Mockito.eq(1), Mockito.any(AppUserRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book-store-service/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateUserDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("updated_user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").value("ADMIN,USER"));
    }

    @Test
    void should_delete_user() throws Exception {
        Mockito.doNothing().when(appUserService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book-store-service/v1/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private static List<AppUserDto> dummyUserDto() {
        AppUserDto admin = new AppUserDto(1, "admin", "ADMIN");
        AppUserDto customer = new AppUserDto(2, "user", "USER");

        return List.of(admin, customer);
    }

    private static String toJson(AppUserRequestDto appUserRequestDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(appUserRequestDto);
    }

}