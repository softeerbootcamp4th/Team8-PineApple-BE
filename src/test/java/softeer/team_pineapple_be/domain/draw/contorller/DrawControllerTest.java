package softeer.team_pineapple_be.domain.draw.contorller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private ObjectMapper objectMapper;

    private DrawWinningResponse winningResponse;
    private DrawLoseResponse loseResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(drawController).build();
        winningResponse = new DrawWinningResponse("당첨 메시지", "경품 이름", "image_url", 1L, true, 3);
        loseResponse = new DrawLoseResponse("꽝 메시지", "꽝 시나리오", "image_url", false, 0);
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("응모가 성공적으로 참여하여 당첨된 경우 - SuccessCase")
    public void enterDraw_DrawIsWin_ReturnWinningResponse() throws Exception {
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
    @DisplayName("응모가 성공적으로 참여하여 낙첨된 경우 - SuccessCase")
    public void enterDraw_DrawIsLose_ReturnLoseResponse() throws Exception {
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
    @DisplayName("상품 전송을 하는 경우 - SuccessCase")
    public void sendPrize_ReturnSuccessResponse() throws Exception {
        // Given
        Long prizeId = 1L; // 예시
        SendPrizeRequest request = new SendPrizeRequest(prizeId);
        SuccessResponse successResponse = new SuccessResponse();

        // When & Then
        mockMvc.perform(post("/draw/rewards/send-prize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(successResponse.getStatus()))
                .andExpect(jsonPath("$.message").value(successResponse.getMessage()));

        verify(drawPrizeService).sendPrizeMessage(prizeId);
    }
}
