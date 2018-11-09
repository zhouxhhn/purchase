/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class JsonTransformer {

  private static Gson gson = new GsonBuilder().
      registerTypeAdapter(
          Integer.class, new JsonDeserializer<Integer>() {
            @Override public Integer deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
              try {
                return json.getAsInt();
              } catch (NumberFormatException e) {
                return null;
              }
            }
          }
      ).registerTypeAdapter(
      BigDecimal.class, new JsonDeserializer<BigDecimal>() {
        @Override public BigDecimal deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
          try {
            return json.getAsBigDecimal();
          } catch (NumberFormatException e) {
            return null;
          }
        }
      }).registerTypeAdapter(
      Long.class, new JsonDeserializer<Long>() {
        @Override public Long deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
          try {
            return json.getAsLong();
          } catch (NumberFormatException e) {
            return null;
          }
        }
      })
      .registerTypeAdapter(
          Date.class,
          new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(
                JsonElement json, Type type, JsonDeserializationContext ctx
            )
                throws JsonParseException {
              if (json == null) {
                return null;
              }
              SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              try {
                return parser.parse(json.getAsJsonPrimitive().getAsString());
              } catch (ParseException ignored) {
                return null;
              }
            }
          }
      )
      .setPrettyPrinting()
      .serializeNulls().create();

  /**
   * 设置未定义属性，不解析 不报错
   */
  private final static ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public static <T> T toObject(String json, Class<T> clazz) throws IOException {

    return gson.fromJson(json, clazz);
  }

  public static String toJson(Object object) {
    return gson.toJson(object);
  }

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }
}
