package softeer.team_pineapple_be.domain.draw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import softeer.team_pineapple_be.domain.draw.domain.DrawPrize;
import softeer.team_pineapple_be.domain.draw.exception.DrawErrorCode;
import softeer.team_pineapple_be.domain.draw.repository.DrawPrizeRepository;
import softeer.team_pineapple_be.global.auth.service.AuthMemberService;
import softeer.team_pineapple_be.global.exception.RestApiException;
import softeer.team_pineapple_be.global.message.MessageService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DrawPrizeServiceTest {

    @InjectMocks
    private DrawPrizeService drawPrizeService;

    @Mock
    private DrawPrizeRepository drawPrizeRepository;

    @Mock
    private MessageService messageService;

    @Mock
    private AuthMemberService authMemberService;

    private static final Long PRIZE_ID = 1L;
    private static final String OWNER_PHONE_NUMBER = "010-1234-5678";
    private static final String PRIZE_IMAGE = "prize_image_url";
    private static final Boolean VALID_STATUS = true;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("상품 당첨자가 맞는 경우 상품을 문자로 성공적으로 전송하는지 - SuccessCase")
    void sendPrizeMessage_WhenOwnerIsValid_SendMessage() {
        // Given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(OWNER_PHONE_NUMBER);
        DrawPrize prize = new DrawPrize(PRIZE_ID, PRIZE_IMAGE, VALID_STATUS, OWNER_PHONE_NUMBER, null);
        when(drawPrizeRepository.findById(PRIZE_ID)).thenReturn(Optional.of(prize));

        // When
        drawPrizeService.sendPrizeMessage(PRIZE_ID);

        // Then
        verify(messageService).sendPrizeImage(PRIZE_IMAGE);
    }

    @Test
    @DisplayName("해당하는 상품이 존재하지 않는 경우 테스트 - FailureCase")
    void sendPrizeMessage_PrizeNotFound_ThrowRestApiException() {
        // Given
        when(authMemberService.getMemberPhoneNumber()).thenReturn(OWNER_PHONE_NUMBER);
        when(drawPrizeRepository.findById(PRIZE_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> drawPrizeService.sendPrizeMessage(PRIZE_ID))
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(DrawErrorCode.NO_PRIZE);
                });
    }

    @Test
    @DisplayName("본인이 아닌 다른 경품에 대한 문자 전송을 요청하는 경우 테스트 - FailureCase")
    void sendPrizeMessage_NotPrizeOwner_ThrowRestApiException() {
        // Given
        String differentOwnerPhoneNumber = "987-654-3210";
        when(authMemberService.getMemberPhoneNumber()).thenReturn(differentOwnerPhoneNumber);
        DrawPrize prize = new DrawPrize(PRIZE_ID, PRIZE_IMAGE, VALID_STATUS, OWNER_PHONE_NUMBER, null);
        when(drawPrizeRepository.findById(PRIZE_ID)).thenReturn(Optional.of(prize));

        // When & Then
        assertThatThrownBy(() -> drawPrizeService.sendPrizeMessage(PRIZE_ID))
                .isInstanceOf(RestApiException.class)
                .satisfies(exception -> {
                    RestApiException restApiException = (RestApiException) exception; // 캐스팅
                    assertThat(restApiException.getErrorCode()).isEqualTo(DrawErrorCode.NOT_PRIZE_OWNER);
                });
    }
}