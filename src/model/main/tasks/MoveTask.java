package model.main.tasks;

import lombok.AllArgsConstructor;
import model.Living;
import model.animals.Animal;
import model.main.Cell;
import model.properties.Encyclopedia;
import util.GeneralConstants;

import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class MoveTask implements Runnable
{
    private final Cell cell;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final Phaser phaser;



    @Override
    public void run()
    {
        for (Encyclopedia livingBeingType : cell.getAllLivingBeingTypes())
        {
            List<Living> livingBeings = cell.getLivingBeings(livingBeingType);
            move(livingBeings);
        }
        phaser.arriveAndDeregister();

    }


    private void move(List<Living> livingBeings)
    {
        for (Living livingBeing : livingBeings)
        {
            if (livingBeing instanceof Animal animal && !animal.hasMoved())
            {
               animal.move();
            }
        }
    }
}
