package softeer.team_pineapple_be.domain.draw.contorller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import softeer.team_pineapple_be.domain.draw.controller.DrawController;
import softeer.team_pineapple_be.domain.draw.request.SendPrizeRequest;
import softeer.team_pineapple_be.domain.draw.response.DrawLoseResponse;
import softeer.team_pineapple_be.domain.draw.response.DrawWinningResponse;
import softeer.team_pineapple_be.domain.draw.service.DrawPrizeService;
import softeer.team_pineapple_be.domain.draw.service.DrawService;
import softeer.team_pineapple_be.global.common.response.SuccessResponse;

public class DrawControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DrawService drawService;

    @Mock
    private DrawPrizeService drawPrizeService;

    @InjectMocks
    private DrawController drawController;

    private DrawWinningResponse winningResponse;
    private DrawLoseResponse loseResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(drawController).build();
        winningResponse = new DrawWinningResponse("당첨 메시지", "경품 이름", "image_url", 1L, true, 3);
        loseResponse = new DrawLoseResponse("꽝 메시지", "꽝 시나리오", "image_url", false, 0);
    }

    @Test
    public void enterDraw_ShouldReturnWinningResponse_WhenDrawIsWin() throws Exception {
        // Given
        when(drawService.enterDraw()).thenReturn(winningResponse);

        // When & Then
        mockMvc.perform(post("/draw")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDrawWin").value(true))
                .andExpect(jsonPath("$.dailyWinningMessage").value("당첨 메시지"))
                .andExpect(jsonPath("$.prizeName").value("경품 이름"));
    }

    @Test
    public void enterDraw_ShouldReturnLoseResponse_WhenDrawIsLose() throws Exception {
        // Given
        when(drawService.enterDraw()).thenReturn(loseResponse);

        // When & Then
        mockMvc.perform(post("/draw")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDrawWin").value(false))
                .andExpect(jsonPath("$.dailyLoseMessage").value("꽝 메시지"))
                .andExpect(jsonPath("$.dailyLoseScenario").value("꽝 시나리오"));
    }

    @Test
    public void sendPrize_ShouldReturnSuccessResponse() throws Exception {
        // Given
        Long prizeId = 1L; // 예시
        SendPrizeRequest request = new SendPrizeRequest(prizeId);
        SuccessResponse successResponse = new SuccessResponse();

        // When & Then
        mockMvc.perform(post("/draw/rewards/send-prize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prizeId\": " + prizeId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(successResponse.getStatus()))
                .andExpect(jsonPath("$.message").value(successResponse.getMessage()));

        verify(drawPrizeService).sendPrizeMessage(prizeId);
    }
}
