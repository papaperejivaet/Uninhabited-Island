package model.main.tasks;

import lombok.AllArgsConstructor;
import model.Living;
import model.main.Cell;
import model.main.Statistics;
import model.properties.Encyclopedia;

import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class LiveTask implements Runnable
{
    private Cell cell;
    private Phaser phaser;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();



    @Override
    public void run()
    {
        phaser.register();
        for (Encyclopedia livingBeingType : cell.getAllLivingBeingTypes())
        {
            List<Living> livingBeings = cell.getLivingBeings(livingBeingType);
            live(livingBeings);
        }
        Statistics.confirmAvailability(cell);
        phaser.arriveAndDeregister();
    }

    private void live(List<Living> livingBeings)
    {
        for (Living livingBeing : livingBeings)
        {
            livingBeing.grow();
            livingBeing.consume();
            boolean isFound;
            do
            {
                isFound = findCouple(livingBeings, livingBeing);
            }
            while (isFound);

        }
    }

    private boolean findCouple(List<Living> livingBeings, Living livingBeing)
    {
        int number = livingBeings.indexOf(livingBeing);
        if (number == livingBeings.size() - 1)
        {
            return false;
        }
        Living couple;

        for (int i = number; i < livingBeings.size(); i++)
        {
            couple = livingBeings.get(i);
            if (livingBeing.reproduce(couple))
            {
                return true;
            }
        }
        return false;
    }


}
