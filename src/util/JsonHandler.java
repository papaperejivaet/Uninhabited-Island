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
     * Загружает JSON-файл и преобразует его в {@code Map<Encyclopedia, T>} по предоставленному классу значений.
     * <p>
     * Ожидаемый формат входного JSON:
     * <pre>{@code
     * {
     *   "WOLF": {...},
     *   "DEER": {...}
     * }
     * }</pre>
     * где ключи соответствуют значениям {@link Encyclopedia#name()}.
     *
     * @param filePath путь к JSON-файлу (можно относительный или абсолютный)
     * @param valueClass класс значений (например, {@code InfoDTO.class})
     * @param <T> тип значений, содержащихся в JSON
     * @return {@code Map<Encyclopedia, T>} — карта, где ключи приведены к Enum-типу {@link Encyclopedia}
     * @throws JsonMapConvertingException при ошибке чтения или преобразования файла
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
     * Загружает JSON-файл и преобразует его в {@code Map<Encyclopedia, T>} с помощью {@link TypeReference}.
     * <p>
     * Подходит для чтения вложенных структур, например:
     * {@code Map<String, List<Something>>} или {@code Map<String, Map<OtherKey, Value>>}.
     *
     * @param filePath путь к JSON-файлу
     * @param typeRef {@link TypeReference} с полной generic-структурой исходного объекта
     * @param <T> тип значений
     * @return карта с приведёнными ключами типа {@link Encyclopedia}
     * @throws JsonMapConvertingException при ошибке чтения или преобразования файла
     */
    public static <T> Map<Encyclopedia, T> parseData(String filePath, TypeReference<Map<String, T>> typeRef)
    {
        String path = checkPath(filePath);
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
     * Вспомогательный метод для отладки. Аналог {@link #parseData(String, TypeReference)},
     * но выводит дополнительную информацию в консоль.
     *
     * @param path путь к JSON-файлу
     * @param typeRef тип структуры JSON
     * @param <T> тип значений
     * @return карта с ключами {@link Encyclopedia}
     * @throws JsonMapConvertingException при ошибке десериализации
     */
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
    /**
     * Преобразует путь к файлу в абсолютный, если он указан как относительный.
     *
     * @param path путь к файлу
     * @return абсолютный путь к файлу
     */
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
