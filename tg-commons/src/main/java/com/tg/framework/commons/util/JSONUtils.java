package com.tg.framework.commons.util;

import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_H_MI_S;
import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_Y_M_D;
import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_Y_M_D_H_MI_S;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

public class JSONUtils {

  private static class ObjectMapperHolder {

    private static final ObjectMapper TRANSFER_OBJECT_MAPPER = new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(buildJavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
        .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        .setTimeZone(TimeZone.getDefault())
        .setDateFormat(JavaTimeUtils.dateTimeFormat());

    private static final ObjectMapper SERIALIZATION_OBJECT_MAPPER = new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(buildJavaTimeModule())
        .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

  }


  private JSONUtils() {
  }

  public static JavaTimeModule buildJavaTimeModule() {
    JavaTimeModule module = new JavaTimeModule();
    DateTimeFormatter dtFormatter = DateTimeFormatter
        .ofPattern(PATTERN_Y_M_D_H_MI_S, Locale.getDefault());
    DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern(PATTERN_Y_M_D, Locale.getDefault());
    DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern(PATTERN_H_MI_S, Locale.getDefault());
    module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dtFormatter));
    module.addSerializer(LocalDate.class, new LocalDateSerializer(dFormatter));
    module.addSerializer(LocalTime.class, new LocalTimeSerializer(tFormatter));
    module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dtFormatter));
    module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dFormatter));
    module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(tFormatter));
    return module;
  }

  public static ObjectMapper transferObjectMapper() {
    return ObjectMapperHolder.TRANSFER_OBJECT_MAPPER;
  }

  public static ObjectMapper serializationObjectMapper() {
    return ObjectMapperHolder.SERIALIZATION_OBJECT_MAPPER;
  }

  public static String writeValueAsString(ObjectMapper om, Object object)
      throws JsonProcessingException {
    if (om != null && object != null) {
      return om.writeValueAsString(object);
    }
    return null;
  }

  public static String writeValueAsString(Object object) throws JsonProcessingException {
    return writeValueAsString(transferObjectMapper(), object);
  }

  public static String toJSON(ObjectMapper om, Object object) {
    try {
      return writeValueAsString(om, object);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public static String toJSON(Object object) {
    return toJSON(transferObjectMapper(), object);
  }

  public static Optional<String> optional2JSON(ObjectMapper om, Object object) {
    return Optional.ofNullable(toJSON(om, object));
  }

  public static Optional<String> optional2JSON(Object object) {
    return optional2JSON(transferObjectMapper(), object);
  }

  public static String toJSON(ObjectMapper om, Object object, String defaultValue) {
    return optional2JSON(om, object).orElse(defaultValue);
  }

  public static String toJSON(Object object, String defaultValue) {
    return toJSON(transferObjectMapper(), object, defaultValue);
  }

  public static JsonNode readTree(ObjectMapper om, String json) throws IOException {
    if (om != null && json != null && json.trim().length() != 0) {
      return om.readTree(json);
    }
    return null;
  }

  public static JsonNode readTree(String json) throws IOException {
    return readTree(transferObjectMapper(), json);
  }

  public static JsonNode toJsonNode(ObjectMapper om, String json) {
    try {
      return readTree(om, json);
    } catch (IOException e) {
      return null;
    }
  }

  public static JsonNode toJsonNode(String json) {
    return toJsonNode(transferObjectMapper(), json);
  }

  public static Optional<JsonNode> optional2JsonNode(ObjectMapper om, String json) {
    return Optional.ofNullable(toJsonNode(om, json));
  }

  public static Optional<JsonNode> optional2JsonNode(String json) {
    return optional2JsonNode(transferObjectMapper(), json);
  }

  public static JsonNode toJsonNode(ObjectMapper om, String json, JsonNode defaultValue) {
    return optional2JsonNode(om, json).orElse(defaultValue);
  }

  public static JsonNode toJsonNode(String json, JsonNode defaultValue) {
    return toJsonNode(transferObjectMapper(), json, defaultValue);
  }

  public static <T> T readValue(ObjectMapper om, String json, Class<T> clazz) throws IOException {
    if (om != null && json != null && json.trim().length() != 0 && clazz != null) {
      return om.readerFor(clazz).readValue(json);
    }
    return null;
  }

  public static <T> T readValue(String json, Class<T> clazz) throws IOException {
    return readValue(transferObjectMapper(), json, clazz);
  }

  public static <T> T toObject(ObjectMapper om, String json, Class<T> clazz) {
    try {
      return readValue(om, json, clazz);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> T toObject(String json, Class<T> clazz) {
    return toObject(transferObjectMapper(), json, clazz);
  }

  public static <T> Optional<T> optional2Object(ObjectMapper om, String json, Class<T> clazz) {
    return Optional.ofNullable(toObject(om, json, clazz));
  }

  public static <T> Optional<T> optional2Object(String json, Class<T> clazz) {
    return optional2Object(transferObjectMapper(), json, clazz);
  }

  public static <T> T toObject(ObjectMapper om, String json, Class<T> clazz, T defaultValue) {
    return optional2Object(om, json, clazz).orElse(defaultValue);
  }

  public static <T> T toObject(String json, Class<T> clazz, T defaultValue) {
    return toObject(transferObjectMapper(), json, clazz, defaultValue);
  }

  public static <T> T readValue(ObjectMapper om, String json, TypeReference<T> typeReference)
      throws IOException {
    if (om != null && json != null && json.trim().length() != 0 && typeReference != null) {
      return om.readerFor(typeReference).readValue(json);
    }
    return null;
  }

  public static <T> T readValue(String json, TypeReference<T> typeReference) throws IOException {
    return readValue(transferObjectMapper(), json, typeReference);
  }

  public static <T> T toObject(ObjectMapper om, String json, TypeReference<T> typeReference) {
    try {
      return readValue(om, json, typeReference);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> T toObject(String json, TypeReference<T> typeReference) {
    return toObject(transferObjectMapper(), json, typeReference);
  }

  public static <T> Optional<T> optional2Object(ObjectMapper om, String json,
      TypeReference<T> typeReference) {
    return Optional.ofNullable(toObject(om, json, typeReference));
  }

  public static <T> Optional<T> optional2Object(String json, TypeReference<T> typeReference) {
    return optional2Object(transferObjectMapper(), json, typeReference);
  }

  public static <T> T toObject(ObjectMapper om, String json, TypeReference<T> typeReference,
      T defaultValue) {
    return optional2Object(om, json, typeReference).orElse(defaultValue);
  }

  public static <T> T toObject(String json, TypeReference<T> typeReference, T defaultValue) {
    return toObject(transferObjectMapper(), json, typeReference, defaultValue);
  }

  public static <K, V> Map<K, V> readMap(ObjectMapper om, String json, Class<K> keyType,
      Class<V> valueType) throws IOException {
    return readValue(om, json, new TypeReference<Map<K, V>>() {
    });
  }

  public static <K, V> Map<K, V> readMap(String json, Class<K> keyType, Class<V> valueType)
      throws IOException {
    return readMap(transferObjectMapper(), json, keyType, valueType);
  }

  public static <K, V> Map<K, V> toMap(ObjectMapper om, String json, Class<K> keyType,
      Class<V> valueType) {
    try {
      return readMap(om, json, keyType, valueType);
    } catch (IOException e) {
      return null;
    }
  }

  public static <K, V> Map<K, V> toMap(String json, Class<K> keyType, Class<V> valueType) {
    return toMap(transferObjectMapper(), json, keyType, valueType);
  }

  public static <K, V> Optional<Map<K, V>> optional2Map(ObjectMapper om, String json,
      Class<K> keyType, Class<V> valueType) {
    return Optional.ofNullable(toMap(om, json, keyType, valueType));
  }

  public static <K, V> Optional<Map<K, V>> optional2Map(String json, Class<K> keyType,
      Class<V> valueType) {
    return optional2Map(transferObjectMapper(), json, keyType, valueType);
  }

  public static <K, V> Map<K, V> toMap(ObjectMapper om, String json, Class<K> keyType,
      Class<V> valueType, Map<K, V> defaultValue) {
    return optional2Map(om, json, keyType, valueType).orElse(defaultValue);
  }

  public static <K, V> Map<K, V> toMap(String json, Class<K> keyType, Class<V> valueType,
      Map<K, V> defaultValue) {
    return toMap(transferObjectMapper(), json, keyType, valueType, defaultValue);
  }

  public static <V> Map<String, V> readMap(ObjectMapper om, String json, Class<V> valueType)
      throws IOException {
    return readMap(om, json, String.class, valueType);
  }

  public static <V> Map<String, V> readMap(String json, Class<V> valueType) throws IOException {
    return readMap(transferObjectMapper(), json, valueType);
  }

  public static <V> Map<String, V> toMap(ObjectMapper om, String json, Class<V> valueType) {
    try {
      return readMap(om, json, valueType);
    } catch (IOException e) {
      return null;
    }
  }

  public static <V> Map<String, V> toMap(String json, Class<V> valueType) {
    return toMap(transferObjectMapper(), json, valueType);
  }

  public static <V> Optional<Map<String, V>> optional2Map(ObjectMapper om, String json,
      Class<V> valueType) {
    return Optional.ofNullable(toMap(om, json, valueType));
  }

  public static <V> Optional<Map<String, V>> optional2Map(String json, Class<V> valueType) {
    return optional2Map(transferObjectMapper(), json, valueType);
  }

  public static <V> Map<String, V> toMap(ObjectMapper om, String json, Class<V> valueType,
      Map<String, V> defaultValue) {
    return optional2Map(om, json, valueType).orElse(defaultValue);
  }

  public static <V> Map<String, V> toMap(String json, Class<V> valueType,
      Map<String, V> defaultValue) {
    return toMap(transferObjectMapper(), json, valueType, defaultValue);
  }

  public static Map<String, Object> readMap(ObjectMapper om, String json) throws IOException {
    return readMap(om, json, Object.class);
  }

  public static Map<String, Object> readMap(String json) throws IOException {
    return readMap(transferObjectMapper(), json);
  }

  public static Map<String, Object> toMap(ObjectMapper om, String json) {
    try {
      return readMap(om, json);
    } catch (IOException e) {
      return null;
    }
  }

  public static Map<String, Object> toMap(String json) {
    return toMap(transferObjectMapper(), json);
  }

  public static Optional<Map<String, Object>> optional2Map(ObjectMapper om, String json) {
    return Optional.ofNullable(toMap(om, json));
  }

  public static Optional<Map<String, Object>> optional2Map(String json) {
    return optional2Map(transferObjectMapper(), json);
  }

  public static Map<String, Object> toMap(ObjectMapper om, String json,
      Map<String, Object> defaultValue) {
    return optional2Map(om, json).orElse(defaultValue);
  }

  public static Map<String, Object> toMap(String json, Map<String, Object> defaultValue) {
    return toMap(transferObjectMapper(), json, defaultValue);
  }

  public static <T> List<T> readList(ObjectMapper om, String json, Class<T> clazz)
      throws IOException {
    return readValue(om, json, new TypeReference<List<T>>() {
    });
  }

  public static <T> List<T> readList(String json, Class<T> clazz) throws IOException {
    return readList(transferObjectMapper(), json, clazz);
  }

  public static <T> List<T> toList(ObjectMapper om, String json, Class<T> clazz) {
    try {
      return readList(om, json, clazz);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> List<T> toList(String json, Class<T> clazz) {
    return toList(transferObjectMapper(), json, clazz);
  }

  public static <T> Optional<List<T>> optional2List(ObjectMapper om, String json, Class<T> clazz) {
    return Optional.ofNullable(toList(om, json, clazz));
  }

  public static <T> Optional<List<T>> optional2List(String json, Class<T> clazz) {
    return optional2List(transferObjectMapper(), json, clazz);
  }

  public static <T> List<T> toList(ObjectMapper om, String json, Class<T> clazz,
      List<T> defaultValue) {
    return optional2List(om, json, clazz).orElse(defaultValue);
  }

  public static <T> List<T> toList(String json, Class<T> clazz, List<T> defaultValue) {
    return toList(transferObjectMapper(), json, clazz, defaultValue);
  }

  public static <T> Set<T> readSet(ObjectMapper om, String json, Class<T> clazz)
      throws IOException {
    return readValue(om, json, new TypeReference<Set<T>>() {
    });
  }

  public static <T> Set<T> readSet(String json, Class<T> clazz) throws IOException {
    return readSet(transferObjectMapper(), json, clazz);
  }

  public static <T> Set<T> toSet(ObjectMapper om, String json, Class<T> clazz) {
    try {
      return readSet(om, json, clazz);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> Set<T> toSet(String json, Class<T> clazz) {
    return toSet(transferObjectMapper(), json, clazz);
  }

  public static <T> Optional<Set<T>> optional2Set(ObjectMapper om, String json, Class<T> clazz) {
    return Optional.ofNullable(toSet(om, json, clazz));
  }

  public static <T> Optional<Set<T>> optional2Set(String json, Class<T> clazz) {
    return optional2Set(transferObjectMapper(), json, clazz);
  }

  public static <T> Set<T> toSet(ObjectMapper om, String json, Class<T> clazz,
      Set<T> defaultValue) {
    return optional2Set(om, json, clazz).orElse(defaultValue);
  }

  public static <T> Set<T> toSet(String json, Class<T> clazz, Set<T> defaultValue) {
    return toSet(transferObjectMapper(), json, clazz, defaultValue);
  }


  public static <T> T[] readArray(ObjectMapper om, String json, Class<T> clazz) throws IOException {
    return readValue(om, json, new TypeReference<T[]>() {
    });
  }

  public static <T> T[] readArray(String json, Class<T> clazz) throws IOException {
    return readArray(transferObjectMapper(), json, clazz);
  }

  public static <T> T[] toArray(ObjectMapper om, String json, Class<T> clazz) {
    try {
      return readArray(om, json, clazz);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> T[] toArray(String json, Class<T> clazz) {
    return toArray(transferObjectMapper(), json, clazz);
  }

  public static <T> Optional<T[]> optional2Array(ObjectMapper om, String json, Class<T> clazz) {
    return Optional.ofNullable(toArray(om, json, clazz));
  }

  public static <T> Optional<T[]> optional2Array(String json, Class<T> clazz) {
    return optional2Array(transferObjectMapper(), json, clazz);
  }

  public static <T> T[] toArray(ObjectMapper om, String json, Class<T> clazz, T[] defaultValue) {
    return optional2Array(om, json, clazz).orElse(defaultValue);
  }

  public static <T> T[] toArray(String json, Class<T> clazz, T[] defaultValue) {
    return toArray(transferObjectMapper(), json, clazz, defaultValue);
  }

  public static JsonNode getJsonNode(JsonNode jsonNode, String fieldName, JsonNode defaultValue) {
    if (jsonNode != null) {
      return jsonNode.get(fieldName);
    }
    return defaultValue;
  }

  public static JsonNode getJsonNode(JsonNode jsonNode, String fieldName) {
    return getJsonNode(jsonNode, fieldName, null);
  }

  public static Optional<JsonNode> optionalGetJsonNode(JsonNode jsonNode, String fieldName) {
    return Optional.ofNullable(getJsonNode(jsonNode, fieldName));
  }

  public static String getText(JsonNode jsonNode, String fieldName, String defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::textValue).orElse(defaultValue);
  }

  public static String getText(JsonNode jsonNode, String fieldName) {
    return getText(jsonNode, fieldName, "");
  }

  public static Optional<String> optionalGetText(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::textValue);
  }

  public static String getAsText(JsonNode jsonNode, String fieldName, String defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(jn -> jn.isTextual() ? jn.textValue() : jn.toString()).orElse(defaultValue);
  }

  public static String getAsText(JsonNode jsonNode, String fieldName) {
    return getAsText(jsonNode, fieldName, "");
  }

  public static Optional<String> optionalGetAsText(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(jn -> jn.isTextual() ? jn.textValue() : jn.toString());
  }

  public static boolean getBoolean(JsonNode jsonNode, String fieldName, boolean defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::booleanValue)
        .orElse(defaultValue);
  }

  public static boolean getBoolean(JsonNode jsonNode, String fieldName) {
    return getBoolean(jsonNode, fieldName, false);
  }

  public static Optional<Boolean> optionalGetBoolean(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::booleanValue);
  }

  public static short getShort(JsonNode jsonNode, String fieldName, short defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::shortValue).orElse(defaultValue);
  }

  public static short getShort(JsonNode jsonNode, String fieldName) {
    return getShort(jsonNode, fieldName, (short) 0);
  }

  public static Optional<Short> optionalGetShort(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::shortValue);
  }

  public static int getInt(JsonNode jsonNode, String fieldName, int defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::intValue).orElse(defaultValue);
  }

  public static int getInt(JsonNode jsonNode, String fieldName) {
    return getInt(jsonNode, fieldName, 0);
  }

  public static Optional<Integer> optionalGetInt(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::intValue);
  }

  public static long getLong(JsonNode jsonNode, String fieldName, long defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::longValue).orElse(defaultValue);
  }

  public static long getLong(JsonNode jsonNode, String fieldName) {
    return getLong(jsonNode, fieldName, 0L);
  }

  public static Optional<Long> optionalGetLong(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::longValue);
  }

  public static float getFloat(JsonNode jsonNode, String fieldName, float defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::floatValue).orElse(defaultValue);
  }

  public static float getFloat(JsonNode jsonNode, String fieldName) {
    return getLong(jsonNode, fieldName, 0);
  }

  public static Optional<Float> optionalGetFloat(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::floatValue);
  }

  public static double getDouble(JsonNode jsonNode, String fieldName, double defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::doubleValue).orElse(defaultValue);
  }

  public static double getDouble(JsonNode jsonNode, String fieldName) {
    return getDouble(jsonNode, fieldName, 0D);
  }

  public static Optional<Double> optionalGetDouble(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::doubleValue);
  }

  public static BigInteger getBigInteger(JsonNode jsonNode, String fieldName,
      BigInteger defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::bigIntegerValue)
        .orElse(defaultValue);
  }

  public static BigInteger getBigInteger(JsonNode jsonNode, String fieldName) {
    return getBigInteger(jsonNode, fieldName, BigInteger.valueOf(0L));
  }

  public static Optional<BigInteger> optionalGetBigInteger(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::bigIntegerValue);
  }

  public static BigDecimal getBigDecimal(JsonNode jsonNode, String fieldName,
      BigDecimal defaultValue) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::decimalValue)
        .orElse(defaultValue);
  }

  public static BigDecimal getBigDecimal(JsonNode jsonNode, String fieldName) {
    return getBigDecimal(jsonNode, fieldName, BigDecimal.valueOf(0L));
  }

  public static Optional<BigDecimal> optionalGetBigDecimal(JsonNode jsonNode, String fieldName) {
    return optionalGetJsonNode(jsonNode, fieldName).map(JsonNode::decimalValue);
  }

}
