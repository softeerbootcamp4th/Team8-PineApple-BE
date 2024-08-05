package softeer.team_pineapple_be.domain.quiz.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softeer.team_pineapple_be.domain.member.domain.Member;
import softeer.team_pineapple_be.domain.member.exception.MemberErrorCode;
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
import java.time.LocalTime;

//TODO: 예외처리(구조 맞추기 위해 남겨둠)

/**
 * QuizContent의 요청에 대한 처리를 담당하는 클래스
 */
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizContentRepository quizContentRepository;
    private final QuizInfoRepository quizInfoRepository;
    private final QuizHistoryRepository quizHistoryRepository;
    private final MemberRepository memberRepository;  //멤버 서비스가 생성될 시에 리팩토링
    private final AuthMemberService authMemberService;

    //TODO: 선착순 처리 되어야함(선착순 처리에 대한 정보 전송할 시에 Response까지 수정되어야 함.)

    /**
     * 퀴즈 정답에 대한 여부를 판단하고 내용을 전송해주는 메서드
     * @param quizInfoRequest 퀴즈 번호와 사용자가 제출한 정답을 받아오기 위한 객체
     * @return 정답 안내 정보에 대한 내용
     */
    @Transactional
    public QuizInfoResponse quizIsCorrect(QuizInfoRequest quizInfoRequest) {
        QuizInfo quizInfo = quizInfoRepository.findById(quizInfoRequest.getQuizId())
                .orElseThrow(() -> new RestApiException(QuizErrorCode.NO_QUIZ_INFO));
        if(quizInfoRequest.getAnswerNum().equals(quizInfo.getAnswerNum())){
            return QuizInfoResponse.of(quizInfo, true);
        }
        return QuizInfoResponse.of(quizInfo, false);
    }

    /**
     * 현재 날짜에 대한 이벤트 내용을 전송해주는 메서드
     * @return 현재 날짜의 이벤트 내용
     */
    @Transactional
    public QuizContentResponse getQuizContent() {
        QuizContent quizContent = quizContentRepository.findByQuizDate(determineQuizDate())
                .orElseThrow(() -> new RestApiException(QuizErrorCode.NO_QUIZ_CONTENT));
        return QuizContentResponse.of(quizContent);
    }

    /**
     * 퀴즈 참여 여부를 저장하는 메서드
     * @return 참여 여부가 등록된 사용자의 툴박스 개수
     */
    @Transactional
    public MemberInfoResponse quizHistory() {
        String phoneNumber = authMemberService.getMemberPhoneNumber(); // 세션 없을 시 여기서 검증됨
        Member member = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new RestApiException(MemberErrorCode.NO_MEMBER));
        QuizContent quizContent = quizContentRepository.findByQuizDate(determineQuizDate())
                .orElseThrow(() -> new RestApiException(QuizErrorCode.NO_QUIZ_CONTENT));
        quizHistoryRepository.findByMemberPhoneNumberAndQuizContentId(phoneNumber, quizContent.getId())
                .ifPresent(quizHistory -> {
                    throw new RestApiException(QuizErrorCode.PARTICIPATION_EXISTS);
                });

        member.incrementToolBoxCnt();
        memberRepository.save(member);
        QuizHistory quizHistory = new QuizHistory(member, quizContent);
        quizHistoryRepository.save(quizHistory);
        return MemberInfoResponse.of(member);
    }

    private LocalDate determineQuizDate() {
        LocalTime onePm = LocalTime.of(13, 0);
        LocalTime atNoon = LocalTime.of(12, 0);
        // 현재 시간이 2시 이전인지 확인
        if (LocalTime.now().isBefore(atNoon)) {
            return LocalDate.now().minusDays(1);
        }

        if(LocalTime.now().isBefore(onePm) && LocalTime.now().isAfter(atNoon)) {
            throw new RestApiException(QuizErrorCode.NO_QUIZ_CONTENT);
        }

        return LocalDate.now();
    }
}
