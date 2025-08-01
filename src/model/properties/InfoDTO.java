package model.properties;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
/**
 * DTO-объект, представляющий параметры формы жизни, получаемые из JSON.
 * Используется для инициализации значений из конфигурационного файла.
 */
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class InfoDTO
{
    private double weight;
    private int maxCellAmount;
    private int maxSpeed;
    @JsonAlias("saturation")
    private double maxSaturation;
    private double maxAge;
    private String display;
    private Map<String, Integer> diet;
}
