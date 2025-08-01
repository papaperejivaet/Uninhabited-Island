package model.main.tasks;

import lombok.AllArgsConstructor;
import model.Living;
import model.main.Cell;
import model.main.Statistics;
import model.properties.Encyclopedia;

import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Задача, выполняемая в рамках симуляции одного цикла.
 * Отвечает за выполнение жизненного цикла всех существ в клетке:
 * - рост;
 * - потребление пищи;
 * - размножение (каждый 5-й цикл);
 * - регистрация статистики по клетке.
 */
@AllArgsConstructor
public class LiveTask implements Runnable
{
    private Cell cell;
    private Phaser phaser;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();


    /**
     * Основной метод выполнения задачи.
     * Для каждого типа живых существ в клетке:
     * - получает список всех представителей этого типа;
     * - вызывает метод {@link #live(List)} для обработки жизненного цикла;
     * - обновляет статистику через {@link Statistics#confirmAvailability(Cell)}.
     * После выполнения — сигнализирует синхронизатору {@link Phaser}, что работа завершена.
     */
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

    /**
     * Обрабатывает жизненный цикл для каждого существа в списке.
     * Выполняются:
     * - рост (метод {@code grow()});
     * - потребление пищи (метод {@code consume()});
     * - размножение, если цикл кратен 5 (через {@link #breed(List, Living)}).
     *
     * @param livingBeings список живых существ одного типа в клетке
     */
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
    /**
     * Пытается найти подходящего партнёра для размножения среди оставшихся в списке.
     * Если подходящий партнёр найден, вызывается метод {@code reproduce()} у текущего существа.
     *
     * @param livingBeings список всех существ одного типа в клетке
     * @param livingBeing текущее существо, ищущее партнёра
     */
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
