package model.main;

import lombok.AllArgsConstructor;
import model.animals.utility.Animal;
import model.animals.utility.Living;
import model.properties.Encyclopedia;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class SimulationTask implements Runnable
{
    private Cell cell;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();


    @Override
    public void run()
    {
        for (Encyclopedia livingBeingType : cell.getAllLivingBeingTypes())
        {
            List<Living> livingBeings = cell.getLivingBeings(livingBeingType);
            live(livingBeings);
        }
    }

    private void live(List<Living> livingBeings)
    {
        for (Living livingBeing : livingBeings)
        {
            if (livingBeing instanceof Animal animal && !animal.isHasMoved())
            {
                animal.move();
            }


        }
    }


}
