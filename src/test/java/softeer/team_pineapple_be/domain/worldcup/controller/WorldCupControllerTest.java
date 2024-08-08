package softeer.team_pineapple_be.domain.worldcup.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import softeer.team_pineapple_be.domain.worldcup.request.WorldCupResultRequest;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupParticipateResponse;
import softeer.team_pineapple_be.domain.worldcup.response.WorldCupResultResponse;
import softeer.team_pineapple_be.domain.worldcup.service.WorldCupService;
import softeer.team_pineapple_be.global.common.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class WorldCupControllerTest {

    @InjectMocks
    private WorldCupController worldCupController;

    @Mock
    private WorldCupService worldCupService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(worldCupController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("/worldcup/participate의 get 요청 api 요청에 대한 응답 테스트 - SuccessCase")
    void getParticipate_MemberParticipated_ReturnsSuccessResponse() throws Exception {
        // given
        WorldCupParticipateResponse response = new WorldCupParticipateResponse(true);
        when(worldCupService.isMemberParticipated()).thenReturn(response);

        // when & then
        mockMvc.perform(get("/worldcup/participants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.car").value(true)); // 응답의 car 필드 검증
    }

    @Test
    @DisplayName("/worldcup/participate의 post 요청 api 요청에 대한 응답 테스트 - SuccessCase")
    void participate_ParticipationIsSuccessfulReturnsSuccessResponse() throws Exception {
        // given
        doNothing().when(worldCupService).participateWorldCup();
        SuccessResponse successResponse = new SuccessResponse();

        // when & then
        mockMvc.perform(post("/worldcup/participants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(successResponse.getStatus())) // status 필드 검증
                .andExpect(jsonPath("$.message").value(successResponse.getMessage())); // message 필드 검증
    }

    @Test
    @DisplayName("월드컵 결과 추가 API 요청에 대한 응답 테스트 - SuccessCase")
    void postResult_ResultIsAdded_ReturnsSuccessResponse() throws Exception {
        // given
        WorldCupResultRequest request = new WorldCupResultRequest(1);
        doNothing().when(worldCupService).addWorldCupResult(any(WorldCupResultRequest.class));
        SuccessResponse successResponse = new SuccessResponse();

        // when & then
        mockMvc.perform(post("/worldcup/results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(successResponse.getStatus())) // status 필드 검증
                .andExpect(jsonPath("$.message").value(successResponse.getMessage())); // message 필드 검증
    }

    @Test
    @DisplayName("월드컵 결과 가져오기 API 요청에 대한 응답 테스트 - SuccessCase")
    void getResult_ResultsAreFetched_ReturnsListWorldCupResultResponse() throws Exception {
        // given
        WorldCupResultResponse response = new WorldCupResultResponse();
        List<WorldCupResultResponse> responseList = Collections.singletonList(response);
        when(worldCupService.getWorldCupResults()).thenReturn(responseList);

        // when & then
        mockMvc.perform(get("/worldcup/results")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists()); // 결과가 존재하는지 검증
    }
}