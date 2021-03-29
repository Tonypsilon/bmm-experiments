package de.berlinerschachverband.bmm;

import de.berlinerschachverband.bmm.seasons.facade.SeasonsController;
import de.berlinerschachverband.bmm.seasons.service.SeasonsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = SeasonsController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SeasonsService.class
        })
)
class BmmApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSeason() throws Exception {
        this.mockMvc
                .perform(get("/season").param("name", "2001-02"))
                .andExpect(status().isOk())
                .andExpect(content().string("only season 2001-02"));
    }

    @Test
    void testGetSeasons() throws Exception {
        this.mockMvc
                .perform(get("/seasons"))
                .andExpect(status().isOk())
                .andExpect(content().string("all seasons"));
    }

}
