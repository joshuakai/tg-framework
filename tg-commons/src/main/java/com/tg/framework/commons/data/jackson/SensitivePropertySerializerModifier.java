package com.tg.framework.commons.data.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.tg.framework.beans.jackson.Mask;
import com.tg.framework.beans.jackson.SensitiveProperty;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensitivePropertySerializerModifier extends BeanSerializerModifier {

  private static Logger logger = LoggerFactory.getLogger(SensitivePropertySerializerModifier.class);

  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
      BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
    for (int i = 0; i < beanProperties.size(); i++) {
      BeanPropertyWriter writer = beanProperties.get(i);
      BeanPropertyWriter wrapWrite = Optional
          .ofNullable(writer.getAnnotation(SensitiveProperty.class)).map(sp -> {
            switch (sp.value()) {
              case HIDE:
                return new HideSensitivePropertyWriter(writer);
              case MASK:
                return new MaskSensitivePropertyWriter(writer, writer.getAnnotation(Mask.class));
              default:
                return writer;
            }
          }).orElse(null);
      if (wrapWrite != null) {
        logger
            .debug("SensitiveProperty annotation found, wrap BeanPropertyWriter {} with {}", writer,
                wrapWrite.getClass());
        beanProperties.set(i, wrapWrite);
      }
    }
    return beanProperties;
  }
}
