package softeer.team_pineapple_be.domain.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.response.MemberInfoResponse;
import softeer.team_pineapple_be.domain.quiz.exception.QuizErrorCode;
import softeer.team_pineapple_be.domain.quiz.request.QuizInfoRequest;
import softeer.team_pineapple_be.domain.quiz.response.QuizContentResponse;
import softeer.team_pineapple_be.domain.quiz.response.QuizInfoResponse;
import softeer.team_pineapple_be.domain.quiz.service.QuizService;
import softeer.team_pineapple_be.global.exception.RestApiException;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QuizControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuizService quizService;

    @InjectMocks
    private QuizController quizController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("/quiz의 api 요청에 대한 응답 테스트 - SuccessCase")
    void getQuizContent_ReturnsQuizContentResponse() throws Exception {
        // Given
        Map<Integer, String> questions = new HashMap<>();
        questions.put(1, "질문 1");
        questions.put(2, "질문 2");
        questions.put(3, "질문 3");
        questions.put(4, "질문 4");

        QuizContentResponse response = new QuizContentResponse(
                1,
                "퀴즈 설명",
                questions
        );
        doReturn(response).when(quizService).getQuizContent();

        // When & Then
        mockMvc.perform(get("/quiz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quizDescription").value("퀴즈 설명"))
                .andExpect(jsonPath("$.quizQuestions['1']").value("질문 1"))
                .andExpect(jsonPath("$.quizQuestions['2']").value("질문 2"))
                .andExpect(jsonPath("$.quizQuestions['3']").value("질문 3"))
                .andExpect(jsonPath("$.quizQuestions['4']").value("질문 4"));
    }

    @Test
    @DisplayName("/quiz/answer의 api 요청에 대한 응답 테스트 - SuccessCase")
    void isCorrect_ReturnsQuizInfoResponse() throws Exception {
        // Given
        QuizInfoRequest request = new QuizInfoRequest(1, (byte) 1); // 필요한 필드 초기화
        QuizInfoResponse response = new QuizInfoResponse(true, "quizImage.png");
        doReturn(response).when(quizService).quizIsCorrect(any(QuizInfoRequest.class));

        // When & Then
        mockMvc.perform(post("/quiz/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isCorrect").value(true))
                .andExpect(jsonPath("$.quizImage").value("quizImage.png"));
    }

    @Test
    @DisplayName("/quiz/participants의 api 요청에 대한 응답 테스트 - SuccessCase")
    void setQuizHistory_ReturnsMemberInfoResponse() throws Exception {
        // Given
        Member member = new Member("010-1234-5678");
        MemberInfoResponse response = MemberInfoResponse.of(member); // 필요한 필드 초기화
        doReturn(response).when(quizService).quizHistory();

        // When & Then
        mockMvc.perform(get("/quiz/participants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").exists());
    }

}

