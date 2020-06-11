package com.tg.framework.commons.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.tg.framework.beans.jackson.Mask;
import com.tg.framework.beans.jackson.MaskType;
import com.tg.framework.commons.util.MaskUtils;

public class MaskSensitivePropertyWriter extends BeanPropertyWriter {

  private MaskType maskType = MaskType.CENTER;

  private int minMaskLength = 3;
  private int maxLeftLength = 3;
  private int maxRightLength = 3;
  private char symbol = '*';
  private int symbolLength = 3;
  private String defaultValue = "";

  public MaskSensitivePropertyWriter(BeanPropertyWriter propertyWriter, Mask mask) {
    super(propertyWriter);
    if (mask != null) {
      maskType = mask.value();
      minMaskLength = mask.minMaskLength();
      maxLeftLength = mask.maxLeftLength();
      maxRightLength = mask.maxRightLength();
      symbol = mask.symbol();
      symbolLength = mask.symbolLength();
      defaultValue = mask.defaultValue();
    }
  }

  @Override
  public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
    gen.writeObjectField(getName(), getMaskedProperty(bean));
  }

  @Override
  public void serializeAsOmittedField(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
    gen.writeObjectField(getName(), getMaskedProperty(bean));
  }

  @Override
  public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
    gen.writeObjectField(getName(), getMaskedProperty(bean));
  }

  private String getMaskedProperty(Object bean) throws Exception {
    final String value = (String) ((_accessorMethod == null) ? _field.get(bean)
        : _accessorMethod.invoke(bean, (Object[]) null));
    switch (maskType) {
      case LEFT:
        return MaskUtils
            .maskLeft(value, minMaskLength, maxRightLength, symbol, symbolLength, defaultValue);
      case CENTER:
        return MaskUtils
            .maskCenter(value, minMaskLength, maxLeftLength, maxRightLength, symbol, symbolLength,
                defaultValue);
      case RIGHT:
        return MaskUtils
            .maskRight(value, minMaskLength, maxLeftLength, symbol, symbolLength, defaultValue);
      default:
        throw new IllegalArgumentException("Unknown MaskType " + maskType);
    }
  }

}
