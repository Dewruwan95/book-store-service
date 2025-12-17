package com.application.bookstore.controller;

import com.application.bookstore.dto.BookDto;
import com.application.bookstore.dto.BookRequestDto;
import com.application.bookstore.service.BookService;
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
@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void should_return_all_books() throws Exception {
        Mockito.when(bookService.getAll()).thenReturn(dummyBookDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("1984"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].price").value(12.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].genre").value("Dystopian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].stock").value(50));
    }

    @Test
    void should_create_new_book() throws Exception {
        BookRequestDto inputBookDto = new BookRequestDto();
        inputBookDto.setTitle("1984");
        inputBookDto.setPrice(12.99);
        inputBookDto.setGenre("Dystopian");
        inputBookDto.setStock(50);

        Mockito.when(bookService.create(Mockito.any(BookRequestDto.class)))
                .thenReturn(dummyBookDto().getFirst());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book-store-service/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(inputBookDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("1984"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(12.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Dystopian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock").value(50));
    }

    private static List<BookDto> dummyBookDto() {
        BookDto book1 = new BookDto();
        book1.setId(1);
        book1.setTitle("1984");
        book1.setPrice(12.99);
        book1.setGenre("Dystopian");
        book1.setStock(50);

        BookDto book2 = new BookDto();
        book2.setId(2);
        book2.setTitle("Harry Potter");
        book2.setPrice(15.99);
        book2.setGenre("Fantasy");
        book2.setStock(100);

        return List.of(book1, book2);
    }

    private static String toJson(BookRequestDto bookRequestDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(bookRequestDto);

    }


}