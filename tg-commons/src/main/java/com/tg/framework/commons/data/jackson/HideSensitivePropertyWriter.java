package com.tg.framework.commons.data.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

public class HideSensitivePropertyWriter extends BeanPropertyWriter {

  public HideSensitivePropertyWriter(BeanPropertyWriter propertyWriter) {
    super(propertyWriter);
  }

  @Override
  public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
  }

  @Override
  public void serializeAsOmittedField(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
  }

  @Override
  public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
  }

}
