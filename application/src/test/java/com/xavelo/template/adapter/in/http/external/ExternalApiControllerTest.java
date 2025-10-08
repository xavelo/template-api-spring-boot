package com.xavelo.template.adapter.in.http.external;

import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.port.in.CallExternalApiUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExternalApiController.class)
class ExternalApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CallExternalApiUseCase callExternalApiUseCase;

    @Test
    void randomExternalApiCallReturnsPayload() throws Exception {
        given(callExternalApiUseCase.callExternalApi(200))
                .willReturn(new ExternalApiResult("200", 200, "200 OK", "https://httpstat.us/200"));

        mockMvc.perform(get("/api/external/random").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("200"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.value").value("200 OK"))
                .andExpect(jsonPath("$.url").value("https://httpstat.us/200"));
    }
}
