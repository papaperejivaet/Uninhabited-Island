package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.JsonMapConvertingException;
import model.properties.Encyclopedia;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Утильный класс для чтения и преобразования JSON-файлов в типизированные {@code Map},
 * где в качестве ключей используется {@link Encyclopedia} (перечисление живых существ).
 * <p>
 * Использует {@link ObjectMapper} из библиотеки Jackson.
 */
public class JsonHandler
{
    private static final ObjectMapper mapper = new ObjectMapper();


    /**
     * Загружает JSON-файл и преобразовывает его в {@code Map<Encyclopedia, T>}.
     * <p>
     * Исходный JSON должен представлять собой объект вида {@code Map<String, T>},
     * где ключи — это имена существ, соответствующие значениям {@link Encyclopedia}.
     *
     * @param path       путь к JSON-файлу
     * @param valueClass класс значений, которые будут храниться в мапе
     * @param <T>        тип значений
     * @return {@code Map<Encyclopedia, T>} — десериализованный и приведённый по ключам JSON
     * @throws JsonMapConvertingException если возникла ошибка при чтении или преобразовании
     */
    public static <T> Map<Encyclopedia, T> parseData(String path, Class<T> valueClass)
    {
        try
        {
            Map<String, T> rawData = mapper.readValue(
                    new File(path),
                    mapper.getTypeFactory().constructMapType(Map.class, String.class, valueClass));
            Map<Encyclopedia, T> result = new EnumMap<>(Encyclopedia.class);

            for (Map.Entry<String, T> livingBeingEntry : rawData.entrySet())
            {
                Encyclopedia livingBeing = Encyclopedia.getLivingBeing(livingBeingEntry.getKey());
                result.put(livingBeing, livingBeingEntry.getValue());
            }
            return result;
        }
        catch (IOException _)
        {
            throw new JsonMapConvertingException("Произошла ошибка во время конвертации json в Map с помощью Class<T>");
        }
    }

    /**
     * Альтернативная версия {@link #parseData(String, Class)}, использующая {@link TypeReference}.
     * <p>
     * Подходит для более сложных структур (например, {@code Map<String, List<T>>}).
     *
     * @param path    путь к JSON-файлу
     * @param typeRef TypeReference с полной информацией о generic-структуре (ключ — строка)
     * @param <T>     тип значений
     * @return {@code Map<Encyclopedia, T>} — десериализованная мапа с преобразованными ключами
     * @throws JsonMapConvertingException если возникла ошибка при чтении или преобразовании
     */
    public static <T> Map<Encyclopedia, T> parseData(String path, TypeReference<Map<String, T>> typeRef)
    {
        try
        {
            Map<String, T> rawData = mapper.readValue(new File(path), typeRef);
            Map<Encyclopedia, T> result = new EnumMap<>(Encyclopedia.class);

            for (Map.Entry<String, T> livingBeingEntry : rawData.entrySet())
            {
                Encyclopedia livingBeing = Encyclopedia.getLivingBeing(livingBeingEntry.getKey());
                result.put(livingBeing, livingBeingEntry.getValue());
            }

            return result;
        }
        catch (IOException _)
        {
            throw new JsonMapConvertingException("Произошла ошибка во время конвертации json в Map с помощью TypeReference");
        }
    }

    /**
     * Загружает JSON-файл с вложенной структурой {@code Map<String, Map<String, T>>} и преобразует его в
     * {@code Map<Encyclopedia, Map<Encyclopedia, T>>}.
     * <p>
     * Параметр {@code valueClass} передаётся для совместимости с сигнатурой,
     * но не используется напрямую, так как используется {@link TypeReference}.
     * <p>
     * Метод полезен, например, для представления вероятностей взаимодействия между видами (например, шанс съедения).
     *
     * @param path       путь к JSON-файлу
     * @param valueClass класс значений, хранящихся во внутренней мапе
     * @param <T>        тип значений
     * @return {@code Map<Encyclopedia, Map<Encyclopedia, T>>} — десериализованные и приведённые ключи
     */
    @SuppressWarnings("unused")
    public static <T> Map<Encyclopedia, Map<Encyclopedia, T>> parseCodependentData(String path, Class<T> valueClass)
    {
        // Загружаем JSON-файл вида Map<String, Map<String, T>>
        // и сразу преобразуем внешние ключи в Encyclopedia
        Map<Encyclopedia, Map<String, T>> rawData = parseData(path,
                new TypeReference<>()
                {
                });

        // Преобразование внутренних ключей (строк) в Encyclopedia
        return rawData.entrySet().stream()
                .collect(Collectors.toMap(
                        // 1-й аргумент (поедающий): внешний ключ типа Encyclopedia
                        Map.Entry::getKey,

                        // 2-й аргумент (поедающий): внешнее значение. Преобразование вложенной Map<String, T> в Map<Encyclopedia, T>
                        predatorEntry -> predatorEntry.getValue().entrySet().stream()
                                .collect(Collectors.toMap(

                                        // 1-й аргумент (жертва): внутренний ключ. Преобразование String в Encyclopedia
                                        preyEntry -> Encyclopedia.getLivingBeing(preyEntry.getKey()),

                                        // 2-й аргумент (жертва): внутреннее значение
                                        Map.Entry::getValue,

                                        // 3-й аргумент (жертва): функция слияния значений при совпадении ключей
                                        (a, b) -> b,

                                        // 4-й аргумент (жертва): EnumMap как итоговая структура
                                        () -> new EnumMap<>(Encyclopedia.class)
                                )),

                        // 3-й аргумент (поедающий): функция слияния значений при совпадении ключей (просто берём второй)
                        (a, b) -> b,

                        // 4-й аргумент (поедающий): EnumMap как итоговая структура
                        () -> new EnumMap<>(Encyclopedia.class)
                ));
    }
}
