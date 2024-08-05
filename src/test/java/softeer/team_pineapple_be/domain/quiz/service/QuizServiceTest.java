package softeer.team_pineapple_be.domain.quiz.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.repository.MemberRepository;
import softeer.team_pineapple_be.domain.member.response.MemberInfoResponse;
import softeer.team_pineapple_be.domain.quiz.domain.QuizContent;
import softeer.team_pineapple_be.domain.quiz.domain.QuizHistory;
import softeer.team_pineapple_be.domain.quiz.domain.QuizInfo;
import softeer.team_pineapple_be.domain.quiz.exception.QuizErrorCode;
import softeer.team_pineapple_be.domain.quiz.repository.QuizContentRepository;
import softeer.team_pineapple_be.domain.quiz.repository.QuizHistoryRepository;
import softeer.team_pineapple_be.domain.quiz.repository.QuizInfoRepository;
import softeer.team_pineapple_be.domain.quiz.request.QuizInfoRequest;
import softeer.team_pineapple_be.domain.quiz.response.QuizContentResponse;
import softeer.team_pineapple_be.domain.quiz.response.QuizInfoResponse;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
import softeer.team_pineapple_be.global.exception.RestApiException;
import java.time.LocalDate;
import java.util.Optional;

public class QuizServiceTest {

    @InjectMocks
    private QuizService quizService;

    @Mock
    private QuizContentRepository quizContentRepository;

    @Mock
    private QuizInfoRepository quizInfoRepository;

    @Mock
    private QuizHistoryRepository quizHistoryRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthMemberService authMemberService;

    private QuizContent quizContent;
    private Integer quizId;
    private Byte correctAnswerNum;
    private Byte incorrectAnswerNum;
    private String phoneNumber;
    private String quizImage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        quizId = 1;
        correctAnswerNum = (byte) 1;
        incorrectAnswerNum = (byte) 2;
        phoneNumber = "010-1234-5678";
        quizImage = "quiz_image.png";
        quizContent = new QuizContent(
                1,
                "퀴즈 설명",           // quizDescription
                "첫 번째 질문",       // quizQuestion1
                "두 번째 질문",       // quizQuestion2
                "세 번째 질문",       // quizQuestion3
                "네 번째 질문",       // quizQuestion4
                LocalDate.now()      // quizDate
        );

    }

    @Test
    @DisplayName("퀴즈 정답이 맞았을 때 결과 테스트 - SuccessCase")
    void quizIsCorrect_CorrectAnswer_ReturnsTrue() {
        // Given
        QuizContent quizContent = new QuizContent(); // QuizContent 객체 생성
        QuizInfo quizInfo = new QuizInfo(quizId, quizContent, correctAnswerNum, quizImage);
        QuizInfoRequest request = new QuizInfoRequest(quizId, correctAnswerNum);
        when(quizInfoRepository.findById(quizId)).thenReturn(Optional.of(quizInfo));

        // When
        QuizInfoResponse response = quizService.quizIsCorrect(request);

        // Then
        assertThat(response.getIsCorrect()).isTrue();
        assertThat(response.getQuizImage()).isEqualTo(quizInfo.getQuizImage());
    }

    @Test
    @DisplayName("퀴즈 정답이 틀렸을 때 결과 테스트 - FailureCase")
    void quizIsCorrect_IncorrectAnswer_ReturnsFalse() {
        // Given
        QuizContent quizContent = new QuizContent();
        QuizInfo quizInfo = new QuizInfo(quizId, quizContent, correctAnswerNum, quizImage);
        QuizInfoRequest request = new QuizInfoRequest(quizId, incorrectAnswerNum);
        when(quizInfoRepository.findById(quizId)).thenReturn(Optional.of(quizInfo));

        // When
        QuizInfoResponse response = quizService.quizIsCorrect(request);

        // Then
        assertThat(response.getIsCorrect()).isFalse();
        assertThat(response.getQuizImage()).isEqualTo(quizInfo.getQuizImage());
    }

    @Test
    @DisplayName("퀴즈 정답 제출 시 해당하는 퀴즈가 없을 때 테스트 - FailureCase")
    void quizIsCorrect_QuizNotFound_ThrowsException() {
        // Given
        QuizInfoRequest request = new QuizInfoRequest(quizId, correctAnswerNum);
        when(quizInfoRepository.findById(quizId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> {
            quizService.quizIsCorrect(request);
        }).isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(QuizErrorCode.NO_QUIZ_INFO);
                });
    }

    @Test
    @DisplayName("퀴즈 컨텐츠가 성공적으로 반한된 결과 테스트 - SuccessCase")
    void getQuizContent_QuizContentExists_ReturnsContent() {
        // Given
        when(quizContentRepository.findByQuizDate(any())).thenReturn(Optional.of(quizContent));

        // When
        QuizContentResponse response = quizService.getQuizContent();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getQuizDescription()).isEqualTo(quizContent.getQuizDescription());
        assertThat(response.getQuizQuestion1()).isEqualTo(quizContent.getQuizQuestion1());
        assertThat(response.getQuizQuestion2()).isEqualTo(quizContent.getQuizQuestion2());
        assertThat(response.getQuizQuestion3()).isEqualTo(quizContent.getQuizQuestion3());
        assertThat(response.getQuizQuestion4()).isEqualTo(quizContent.getQuizQuestion4());
    }

    @Test
    @DisplayName("퀴즈 컨텐츠가 성공적으로 반한되지 못한 결과 테스트 - FailureCase")
    void getQuizContent_QuizContentNotFound_ThrowsException() {
        // Given
        when(quizContentRepository.findByQuizDate(any())).thenReturn(Optional.empty());

        // When & Then: 예외가 발생하는지 확인
        assertThatThrownBy(() -> {
            quizService.getQuizContent();
        }).isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(QuizErrorCode.NO_QUIZ_CONTENT);
                });
    }

    @Test
    @DisplayName("퀴즈 참여기록을 성공적으로 저장했을 때 결과 테스트- SuccessCase")
    void quizHistory_QuizContentExists_And_NoParticipation_ReturnsMemberInfoResponse() {
        // Given
        Member member = new Member(phoneNumber);

        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(quizContentRepository.findByQuizDate(any())).thenReturn(Optional.of(quizContent));
        when(quizHistoryRepository.findByMemberPhoneNumberAndQuizContentId(phoneNumber, quizContent.getId())).thenReturn(Optional.empty());
        when(memberRepository.findByPhoneNumber(phoneNumber)).thenReturn(member);

        // When
        MemberInfoResponse response = quizService.quizHistory();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(member.getToolBoxCnt()).isEqualTo(1); // 툴박스 카운트 증가 확인
        verify(quizHistoryRepository).save(any(QuizHistory.class)); // 퀴즈 이력 저장 확인
    }

    @Test
    @DisplayName("퀴즈 참여기록을 성공적으로 저장하지 못했을 때 결과 테스트- FailureCase")
    void quizHistory_QuizContentDoesNotExist_ThrowsRestApiException() {
        // Given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(quizContentRepository.findByQuizDate(any())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> quizService.quizHistory())
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(QuizErrorCode.NO_QUIZ_CONTENT);
                });
    }

    @Test
    @DisplayName("퀴즈 참여기록이 이미 존재할 때 결과 테스트- FailureCase")
    void quizHistory_ParticipationExists_ThrowsRestApiException() {
        // Given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(phoneNumber);
        when(quizContentRepository.findByQuizDate(any())).thenReturn(Optional.of(quizContent));
        when(quizHistoryRepository.findByMemberPhoneNumberAndQuizContentId(phoneNumber, quizContent.getId()))
                .thenReturn(Optional.of(new QuizHistory())); // 참여 이력 존재

        // When & Then
        assertThatThrownBy(() -> quizService.quizHistory())
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(QuizErrorCode.PARTICIPATION_EXISTS);
                });
    }

}