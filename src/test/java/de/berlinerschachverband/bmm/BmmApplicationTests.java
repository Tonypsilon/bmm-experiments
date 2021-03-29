package de.berlinerschachverband.bmm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class BmmApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSeason() throws Exception{
        this.mockMvc
                .perform(get("/season").param("name", "2001-02"))
                .andExpect(status().isOk())
                .andExpect(content().string("only season 2001-02"));
    }

}
