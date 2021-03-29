package de.berlinerschachverband.bmm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class BmmApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSeason() throws Exception {
        String name = "2001-02";
        this.mockMvc
                .perform(post("/createSeason").param("name", name));
        this.mockMvc
                .perform(get("/season").param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().string("only season " + name));
    }

    @Test
    void testGetSeasons() throws Exception {
        this.mockMvc
                .perform(get("/seasons"))
                .andExpect(status().isOk())
                .andExpect(content().string("all seasons"));
    }

}
