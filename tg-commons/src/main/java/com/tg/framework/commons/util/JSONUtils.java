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
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.tg.framework.commons.jackson.SensitivePropertySerializerModifier;
import com.tg.framework.commons.lang.StringOptional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

public class JSONUtils {

  private static final ObjectMapper TRANSFER_OBJECT_MAPPER = new ObjectMapper()
      .setSerializerFactory(BeanSerializerFactory.instance
          .withSerializerModifier(new SensitivePropertySerializerModifier()))
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

  private JSONUtils() {
  }

  private static JavaTimeModule buildJavaTimeModule() {
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
    return TRANSFER_OBJECT_MAPPER;
  }

  public static ObjectMapper serializationObjectMapper() {
    return SERIALIZATION_OBJECT_MAPPER;
  }

  private static String writeValueAsString(Object object) {
    try {
      return transferObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public static Optional<String> toJSON(Object object) {
    return Optional.ofNullable(object).map(JSONUtils::writeValueAsString);
  }

  public static String toJSON(Object object, String defaultValue) {
    return toJSON(object).orElse(defaultValue);
  }

  private static JsonNode readTree(String json) {
    try {
      return transferObjectMapper().readTree(json);
    } catch (IOException e) {
      return null;
    }
  }

  public static Optional<JsonNode> toJsonNode(String json) {
    return StringOptional.ofNullable(json).map(JSONUtils::readTree);
  }

  public static JsonNode toJsonNode(String json, JsonNode defaultValue) {
    return toJsonNode(json).orElse(defaultValue);
  }

  private static <T> Map<String, T> readValueAsMap(String json) {
    try {
      return transferObjectMapper().readerFor(Map.class).readValue(json);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> Optional<Map<String, T>> toMap(String json) {
    return StringOptional.ofNullable(json).map(JSONUtils::readValueAsMap);
  }

  public static <T> Map<String, T> toMap(String json, Map<String, T> defaultValue) {
    Optional<Map<String, T>> optional = toMap(json);
    return optional.orElse(defaultValue);
  }

  private static <T> T readValueAs(String json, Class<T> clazz) {
    try {
      return transferObjectMapper().readerFor(clazz).readValue(json);
    } catch (Exception e) {
      return null;
    }
  }

  private static <T> T readValueAs(String json, TypeReference<T> typeReference) {
    try {
      return transferObjectMapper().readValue(json, typeReference);
    } catch (Exception e) {
      return null;
    }
  }

  public static <T> Optional<T> toObject(String json, Class<T> clazz) {
    return StringOptional.ofNullable(json)
        .map(j -> Optional.ofNullable(clazz).map(c -> readValueAs(j, c)).orElse(null));
  }

  public static <T> T toObject(String json, Class<T> clazz, T defaultValue) {
    return toObject(json, clazz).orElse(defaultValue);
  }

  public static <T> Optional<T> toObject(String json, TypeReference<T> typeReference) {
    return StringOptional.ofNullable(json)
        .map(j -> Optional.ofNullable(typeReference).map(t -> readValueAs(j, t)).orElse(null));
  }

  public static <T> T toObject(String json, TypeReference<T> typeReference, T defaultValue) {
    return toObject(json, typeReference).orElse(defaultValue);
  }

  public static boolean getBoolean(JsonNode jsonNode, String fieldName, boolean defaultValue) {
    return Optional.ofNullable(jsonNode).map(j -> j.get(fieldName)).map(JsonNode::booleanValue)
        .orElse(defaultValue);
  }

  public static short getShort(JsonNode jsonNode, String fieldName, short defaultValue) {
    return Optional.ofNullable(jsonNode).map(j -> j.get(fieldName)).map(JsonNode::shortValue)
        .orElse(defaultValue);
  }

  public static int getInt(JsonNode jsonNode, String fieldName, int defaultValue) {
    return Optional.ofNullable(jsonNode).map(j -> j.get(fieldName)).map(JsonNode::intValue)
        .orElse(defaultValue);
  }

  public static long getLong(JsonNode jsonNode, String fieldName, long defaultValue) {
    return Optional.ofNullable(jsonNode).map(j -> j.get(fieldName)).map(JsonNode::longValue)
        .orElse(defaultValue);
  }

  public static float getFloat(JsonNode jsonNode, String fieldName, float defaultValue) {
    return Optional.ofNullable(jsonNode).map(j -> j.get(fieldName)).map(JsonNode::floatValue)
        .orElse(defaultValue);
  }

  public static double getDouble(JsonNode jsonNode, String fieldName, double defaultValue) {
    return Optional.ofNullable(jsonNode).map(j -> j.get(fieldName)).map(JsonNode::doubleValue)
        .orElse(defaultValue);
  }

  public static String getString(JsonNode jsonNode, String fieldName, String defaultValue) {
    return Optional.ofNullable(jsonNode).map(j -> j.get(fieldName)).map(JsonNode::asText)
        .orElse(defaultValue);
  }

  public static JsonNode getNode(JsonNode jsonNode, String fieldName, JsonNode defaultValue) {
    return Optional.ofNullable(jsonNode).map(j -> j.get(fieldName)).orElse(defaultValue);
  }

}
