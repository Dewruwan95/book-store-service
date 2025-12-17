package com.application.bookstore.controller;

import com.application.bookstore.dto.AuthorDto;
import com.application.bookstore.dto.AuthorRequestDto;
import com.application.bookstore.service.AuthorService;
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

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @Test
    void should_return_all_authors() throws Exception {
        Mockito.when(authorService.getAll()).thenReturn(dummyAuthorDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/authors"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("George"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value("Orwell"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value("george.orwell@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nationality").value("British"));
    }

    @Test
    void should_create_new_author() throws Exception {
        AuthorRequestDto inputAuthorDto = new AuthorRequestDto();
        inputAuthorDto.setFirstName("George");
        inputAuthorDto.setLastName("Orwell");
        inputAuthorDto.setEmail("george.orwell@email.com");
        inputAuthorDto.setNationality("British");

        Mockito.when(authorService.create(Mockito.any(AuthorRequestDto.class)))
                .thenReturn(dummyAuthorDto().getFirst());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book-store-service/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(inputAuthorDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("George"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Orwell"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("george.orwell@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nationality").value("British"));
    }

    private static List<AuthorDto> dummyAuthorDto() {
        AuthorDto author1 = new AuthorDto();
        author1.setId(1);
        author1.setFirstName("George");
        author1.setLastName("Orwell");
        author1.setEmail("george.orwell@email.com");
        author1.setNationality("British");

        AuthorDto author2 = new AuthorDto();
        author2.setId(2);
        author2.setFirstName("J.K.");
        author2.setLastName("Rowling");
        author2.setEmail("jk.rowling@email.com");
        author2.setNationality("British");


        return List.of(author1, author2);
    }

    private static String toJson(AuthorRequestDto authorRequestDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(authorRequestDto);
    }
}