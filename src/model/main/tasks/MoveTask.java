package model.main.tasks;

import lombok.AllArgsConstructor;
import model.Living;
import model.Mobile;
import model.animals.Animal;
import model.main.Cell;
import model.properties.Encyclopedia;
import util.GeneralConstants;

import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Задача перемещения всех подвижных существ в одной клетке.
 * Используется на этапе "движения" в одном симуляционном цикле.
 */
@AllArgsConstructor
public class MoveTask implements Runnable
{
    private final Cell cell;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final Phaser phaser;

    /**
     * Основной метод перемещения.
     * Для каждого типа живых существ в клетке:
     * - получает всех представителей;
     * - вызывает метод {@link #move(List)} для перемещения всех подвижных особей.
     * После завершения работы — сообщает об этом {@link Phaser}.
     */
    @Override
    public void run() {
        try {
            for (Encyclopedia livingBeingType : cell.getAllLivingBeingTypes()) {
                List<Living> livingBeings = cell.getLivingBeings(livingBeingType);
                move(livingBeings);
            }
        } catch (Throwable t) {
            System.err.println("Exception in MoveTask for cell " + cell + ":");
            t.printStackTrace();
            t.getCause();
        } finally {
            phaser.arriveAndDeregister();
           // System.out.println("End of MoveTask for " + cell + ", unarrived: " + phaser.getUnarrivedParties());
        }
    }

    /**
     * Перебирает всех существ в списке и перемещает только тех,
     * которые реализуют интерфейс {@code Mobile}.
     * Метод {@code move()} вызывается у каждого подвижного существа.
     *
     * @param livingBeings список живых существ одного типа
     */
    private void move(List<Living> livingBeings)
    {
        for (Living livingBeing : livingBeings)
        {
            if (livingBeing instanceof Mobile mobile)
            {
               mobile.move();
            }
        }
    }
}
