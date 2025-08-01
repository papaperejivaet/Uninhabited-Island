package model.properties;
/**
 * Причина завершения симуляции.
 * Используется для отображения финального экрана.
 */
public enum EndReason
{
    ALL_DEAD,
    ONLY_CARNIVORE_LEFT,
    ONLY_HERBIVORE_LEFT,
    NO_PLANTS_LEFT,
    TIMEOUT
}
