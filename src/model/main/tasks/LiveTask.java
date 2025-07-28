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
    public void run() {
        try {
            for (Encyclopedia livingBeingType : cell.getAllLivingBeingTypes()) {
                List<Living> livingBeings = cell.getLivingBeings(livingBeingType);
                live(livingBeings);
            }
            Statistics.confirmAvailability(cell);
        } catch (Throwable t) {
            // на всякий случай залогировать, чтобы видеть, что пошло не так
            System.err.println("Exception in LiveTask for cell " + cell + ": " + t);
            t.printStackTrace();
            t.getCause();
        } finally {
            phaser.arriveAndDeregister();
        }
    }

    private void live(List<Living> livingBeings)
    {
        for (Living livingBeing : livingBeings)
        {
            livingBeing.grow();
            livingBeing.consume();
            if (Statistics.getCurrentCycleNumber() % 5 == 0)
            {
                breed(livingBeings, livingBeing);
            }
        }
    }

    private void breed(List<Living> livingBeings, Living livingBeing)
    {
        int number = livingBeings.indexOf(livingBeing);

        if (number == livingBeings.size() - 1)
        {
            return;
        }
        Living couple;

        for (int i = number; i < livingBeings.size(); i++)
        {
            couple = livingBeings.get(i);
            if (livingBeing.reproduce(couple))
            {
                return;
            }
        }
    }


}
