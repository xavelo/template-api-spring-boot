package com.xavelo.template.adapter.in.http.asyncoperation;

import com.xavelo.template.application.domain.AsyncOperationResult;
import com.xavelo.template.application.port.in.asyncoperation.AsyncOperationUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AsyncOperationController.class)
class AsyncOperationControllerAsyncTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AsyncOperationUseCase asyncOperationUseCase;

    @Test
    void triggerAsyncOperationReturnsAsyncResponse() throws Exception {
        AsyncOperationResult result = new AsyncOperationResult("Async operation completed", 750L);
        Mockito.when(asyncOperationUseCase.triggerAsyncOperationAsync())
                .thenReturn(CompletableFuture.completedFuture(result));

        MvcResult mvcResult = mockMvc.perform(get("/api/async-operation"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Async operation completed"))
                .andExpect(jsonPath("$.durationMs").value(750));
    }
}
