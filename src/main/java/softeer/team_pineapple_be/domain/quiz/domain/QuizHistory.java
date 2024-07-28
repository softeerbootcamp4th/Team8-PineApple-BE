package softeer.team_pineapple_be.domain.quiz.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softeer.team_pineapple_be.domain.member.domain.Member;

import java.time.LocalDate;

// TODO: id빼고, 복합키로 사용하면 성능에 어떤 일이 생길까?
/**
 * QuizHistory의 엔티티 타입
 */
@Entity
@Getter
@NoArgsConstructor
public class QuizHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate participantDate;

    @ManyToOne
    @JoinColumn(name = "phone_number", nullable = false)
    private Member member;

    public QuizHistory(Member member) {
        this.member = member;
        this.participantDate = LocalDate.now(); // 현재 날짜로 설정
    }
}
