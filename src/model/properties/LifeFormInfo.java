package model.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeFormInfo
{
    private double weight;
    private int maxCellAmount;
    private int maxSpeed;
    private double saturation;
    private double maxAge;
}
