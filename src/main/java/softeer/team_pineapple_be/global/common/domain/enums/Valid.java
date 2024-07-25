package softeer.team_pineapple_be.global.common.domain.enums;

import java.util.EnumSet;

import jakarta.persistence.Converter;
import lombok.Getter;
import softeer.team_pineapple_be.global.common.domain.enums.converter.AbstractEnumCodeConverter;

/**
 * 유효 여부
 */
@Getter
public enum Valid implements Codable {

  TRUE("Y"),
  FALSE("N");

  private final String code;

  Valid(String code) {
    this.code = code;
  }

  public static boolean containCode(String code) {
    return EnumSet.allOf(Valid.class).stream().anyMatch(e -> e.getCode().equals(code));
  }

  public static Valid fromCode(final String code) {
    return Codable.fromCode(Valid.class, code);
  }

  @Converter
  public static class TypeCodeConverter extends AbstractEnumCodeConverter<Valid> {
    @Override
    public Valid convertToEntityAttribute(String dbData) {
      return this.toEntityAttribute(Valid.class, dbData);
    }
  }
}