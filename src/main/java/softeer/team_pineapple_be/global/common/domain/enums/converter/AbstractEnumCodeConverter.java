package softeer.team_pineapple_be.global.common.domain.enums.converter;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.AttributeConverter;
import softeer.team_pineapple_be.global.common.domain.enums.Codable;

/**
 * JPA Enum 컨버터
 */
public abstract class AbstractEnumCodeConverter<E extends Enum<E> & Codable> implements AttributeConverter<E, String> {
  @Override
  public String convertToDatabaseColumn(E attribute) {
    return this.toDatabaseColumn(attribute);
  }

  @Override
  public E convertToEntityAttribute(String dbData) {
    return null;
  }

  public E toEntityAttribute(Class<E> cls, String code) {
    return StringUtils.isBlank(code) ? null : Codable.fromCode(cls, code);
  }

  private String toDatabaseColumn(E attr) {
    return (attr == null) ? null : attr.getCode();
  }
}
