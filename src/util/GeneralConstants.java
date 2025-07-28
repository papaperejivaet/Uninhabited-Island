package util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralConstants
{
    public static final int CYCLE_TIME = 1;
    public static final int MAX_PROCESSING_THREADS = 4;
    public static final int MAX_CYCLES = 500;
    public static final int LENGTH = 20;
    public static final int HEIGHT = 20;
    public static final int CELLS_AMOUNT = LENGTH * HEIGHT;
}
