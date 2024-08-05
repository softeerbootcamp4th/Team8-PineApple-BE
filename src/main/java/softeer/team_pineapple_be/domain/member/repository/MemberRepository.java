package softeer.team_pineapple_be.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softeer.team_pineapple_be.domain.member.domain.Member;

import java.util.Optional;

/**
 * 멤버 리포지토리
 */
public interface MemberRepository extends JpaRepository<Member, String> {
  Optional<Member> findByPhoneNumber(String phoneNumber);
}
