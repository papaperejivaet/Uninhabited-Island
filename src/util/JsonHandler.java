package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.JsonMapConvertingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.properties.Encyclopedia;
import model.properties.InfoDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;


/**
 * Утильный класс для чтения и преобразования JSON-файлов в типизированные {@code Map},
 * где в качестве ключей используется {@link Encyclopedia} (перечисление живых существ).
 * <p>
 * Использует {@link ObjectMapper} из библиотеки Jackson.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonHandler
{
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Загружает JSON-файл и преобразовывает его в {@code Map<Encyclopedia, T>}.
     * <p>
     * Исходный JSON должен представлять собой объект вида {@code Map<String, T>},
     * где ключи — это имена существ, соответствующие значениям {@link Encyclopedia}.
     *
     * @param filePath       путь к JSON-файлу
     * @param valueClass класс значений, которые будут храниться в мапе
     * @param <T>        тип значений
     * @return {@code Map<Encyclopedia, T>} — десериализованный и приведённый по ключам JSON
     * @throws JsonMapConvertingException если возникла ошибка при чтении или преобразовании
     */
    public static <T> Map<Encyclopedia, T> parseData(String filePath, Class<T> valueClass)
    {
        String path = checkPath(filePath);
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


     public static <T> Map<Encyclopedia, T> parseDataCheck(String path, TypeReference<Map<String, T>> typeRef) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("Файл не найден: " + file.getAbsolutePath());
            }

            Map<String, T> rawData = mapper.readValue(file, typeRef);
            System.out.println("Успешно считан JSON: " + rawData.size() + " записей");

            Map<Encyclopedia, T> result = new EnumMap<>(Encyclopedia.class);

            for (Map.Entry<String, T> entry : rawData.entrySet()) {
                String key = entry.getKey();
                T value = entry.getValue();
                Encyclopedia e = Encyclopedia.getLivingBeing(key);
                result.put(e, value);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace(); // ← увидим, что реально упало
            throw new JsonMapConvertingException("Ошибка чтения JSON-файла: " + e.getMessage());
        }
    }


    /**
     * Загружает и десериализует информацию о живых существах из JSON-файла,
     * возвращая отображение {@link Encyclopedia} → {@link InfoDTO}.
     * <p>
     * Ожидаемый формат JSON:
     * <pre>{@code
     * {
     *   "WOLF": {
     *     "weight": 50.0,
     *     "maxCellAmount": 30,
     *     "maxSpeed": 3,
     *     "saturation": 8.0,
     *     "maxAge": 6.0
     *     "diet": {
     *       "HORSE": 10,
     *       "DEER": 15,
     *       "RABBIT": 60
     *       }
     *   },
     *   ...
     * }
     * }</pre>
     * Значения ключей должны совпадать (регистр не важен) с {@code name()}  элементов {@link Encyclopedia}.
     * <p>
     * Используется универсальный метод {@code parseData()} с поддержкой {@code TypeReference}
     * для преобразования вложенных структур.
     *
     * @param path путь к JSON-файлу с параметрами существ
     * @return отображение {@code Encyclopedia → LifeFormInfo}, содержащее параметры всех существ
     * @throws JsonMapConvertingException если произошла ошибка при чтении или преобразовании JSON
     * @see JsonHandler#parseData(String, TypeReference)
     */

    public static Map<Encyclopedia, InfoDTO> parseLifeFormInfo(String path) {
        return parseData(path, new TypeReference<>() {});
    }

    private static String checkPath(String path)
    {
        Path filePath = Path.of(path);
        if (filePath.isAbsolute())
        {
            return path;
        }

        return filePath.toAbsolutePath().toString();
    }

}
