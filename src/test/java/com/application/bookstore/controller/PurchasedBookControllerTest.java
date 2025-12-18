package com.application.bookstore.controller;

import com.application.bookstore.dto.PurchasedBookDto;
import com.application.bookstore.service.PurchasedBookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PurchasedBookController.class)
class PurchasedBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PurchasedBookService purchasedBookService;

    @Test
    void should_return_all_purchased_books() throws Exception {
        Mockito.when(purchasedBookService.getAll()).thenReturn(dummyPurchasedBookDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/purchase"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].purchaseId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].customerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].bookId").value(1));
    }

    @Test
    void should_create_new_purchased_book() throws Exception {

        Mockito.when(purchasedBookService.create(Mockito.any()))
                .thenReturn(dummyPurchasedBookDto().getFirst());

        // Provide PurchasedBookRequestDto, using simple JSON
        String requestJson = "{\"customerId\": 1, \"bookId\": 1}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book-store-service/v1/purchase")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.purchaseId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(1));
    }

    private static List<PurchasedBookDto> dummyPurchasedBookDto() {
        PurchasedBookDto purchased1 = new PurchasedBookDto();
        purchased1.setPurchaseId(1);
        purchased1.setCustomerId(1);
        purchased1.setBookId(1);
        purchased1.setPurchaseDate(LocalDate.now());

        PurchasedBookDto purchased2 = new PurchasedBookDto();
        purchased2.setPurchaseId(2);
        purchased2.setCustomerId(1);
        purchased2.setBookId(2);
        purchased2.setPurchaseDate(LocalDate.now().minusDays(1));

        return List.of(purchased1, purchased2);
    }
}