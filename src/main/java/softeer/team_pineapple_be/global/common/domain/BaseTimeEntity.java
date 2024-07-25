package softeer.team_pineapple_be.global.common.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * 등록/수정일시 엔티티
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
  /**
   * 등록일시
   */
  @CreatedDate
  @Column(name = "CRE_DTM", updatable = false, nullable = false)
  protected LocalDateTime createAt;
  /**
   * 수정일시
   */
  @CreatedDate
  @Column(name = "UPD_DTM", nullable = false)
  protected LocalDateTime updatedAt;
}
