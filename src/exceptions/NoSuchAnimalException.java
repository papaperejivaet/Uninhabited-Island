package exceptions;

public class NoSuchAnimalException extends RuntimeException
{
    public NoSuchAnimalException(String message)
    {
        super(message);
    }
}
