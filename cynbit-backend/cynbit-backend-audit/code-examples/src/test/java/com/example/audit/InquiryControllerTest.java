package com.example.audit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(InquiryController.class)
@Import({InquiryService.class, ApiExceptionHandler.class})
class InquiryControllerTest {
    @Autowired MockMvc mockMvc;

    @Test
    void acceptsValidInquiry() throws Exception {
        String body = "{\"fullName\":\"Alex Rivera\",\"email\":\"alex@example.com\","
            + "\"company\":\"Example Ltd\",\"industry\":\"Software\","
            + "\"message\":\"Please contact me about a project.\"}";
        mockMvc.perform(post("/api/v1/inquiries").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    void rejectsInvalidEmail() throws Exception {
        String body = "{\"fullName\":\"Alex Rivera\",\"email\":\"not-an-email\","
            + "\"company\":\"\",\"industry\":\"\",\"message\":\"Please contact me.\"}";
        mockMvc.perform(post("/api/v1/inquiries").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Request validation failed"));
    }
}
