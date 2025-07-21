package model.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class InfoDTO
{
    private double weight;
    private int maxCellAmount;
    private int maxSpeed;
    private double maxSaturation;
    private double maxAge;
    private char display;
    private Map<Encyclopedia, Integer> diet;
}
