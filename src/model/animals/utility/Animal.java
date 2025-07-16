package model.animals.utility;



public abstract class Animal extends LifeForm implements Mobile
{

    public Animal(int age, long saturationLevel)
    {
        super(age, saturationLevel);
    }

    @Override
    public void move()
    {

    }

}
