package softeer.team_pineapple_be.global.common.domain.enums;

import java.util.stream.Stream;

/**
 * code 가지는 Enum 공통 인터페이스
 */
public interface Codable {
  static <E extends Enum<E> & Codable> E fromCode(Class<E> cls, String code) {
    if (code == null) {
      return null;
    }
    final String t = code.trim();
    return Stream.of(cls.getEnumConstants())
                 .filter(e -> e.getCode().equalsIgnoreCase(t))
                 .findAny()
                 .orElseThrow(
                     () -> new IllegalArgumentException(String.format("%s에 %s가 존재하지 않습니다.", cls.getName(), code)));
  }

  String getCode();
}
