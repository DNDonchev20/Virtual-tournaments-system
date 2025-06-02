package org.codingburgas.dndonchev20.nastolni_igri;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.codingburgas.dndonchev20.nastolni_igri.controllers.GameController;
import org.codingburgas.dndonchev20.nastolni_igri.services.ChatMessageService;
import org.codingburgas.dndonchev20.nastolni_igri.services.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = GameController.class)
public class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private GameService gameService;

    private ChatMessageService chatMessageService;

    @Test
    @WithMockUser(username = "testuser")
    public void testGetGamesEndpoint() throws Exception {
        when(gameService.getAllGames()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("games"))
                .andExpect(view().name("games/list"));
    }
}
