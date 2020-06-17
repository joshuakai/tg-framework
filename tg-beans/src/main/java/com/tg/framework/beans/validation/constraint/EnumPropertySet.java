package com.tg.framework.beans.validation.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.tg.framework.beans.validation.constraint.EnumPropertySet.EnumPropertySetArrayValidator;
import com.tg.framework.beans.validation.constraint.EnumPropertySet.EnumPropertySetCollectionValidator;
import com.tg.framework.beans.validation.constraint.EnumPropertySet.EnumPropertySetStringArrayValidator;
import com.tg.framework.beans.validation.constraint.EnumPropertySet.EnumPropertySetStringCollectionValidator;
import com.tg.framework.beans.validation.constraint.EnumPropertySet.EnumPropertySetStringValidator;
import com.tg.framework.beans.validation.constraint.EnumPropertySet.EnumPropertySetValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumPropertySetValidator.class, EnumPropertySetArrayValidator.class,
    EnumPropertySetCollectionValidator.class, EnumPropertySetStringValidator.class,
    EnumPropertySetStringArrayValidator.class, EnumPropertySetStringCollectionValidator.class})
public @interface EnumPropertySet {

  String ENUM_PROPERTIES = "enumProperties";

  String message() default "{com.tg.framework.beans.validation.constraint.EnumPropertySet.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends Enum<?>> value();

  String[] excludes() default {};

  String property() default "getValue";

  boolean usingGetter() default true;

  String separator() default "";

  abstract class EnumPropertySetValidatorSupport<T> implements
      ConstraintValidator<EnumPropertySet, T> {

    private Set<String> enumProperties;

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
      boolean isValid = value == null || internalValidate(value);
      if (!isValid) {
        context.unwrap(HibernateConstraintValidatorContext.class)
            .addMessageParameter(ENUM_PROPERTIES, enumProperties);
      }
      return isValid;
    }

    @Override
    public void initialize(EnumPropertySet constraintAnnotation) {
      Stream<Enum<?>> stream = Stream.of(constraintAnnotation.value().getEnumConstants());
      if (constraintAnnotation.excludes().length > 0) {
        Set<String> excludes = new HashSet<>(Arrays.asList(constraintAnnotation.excludes()));
        stream = stream.filter(e -> excludes.contains(e.name()));
      }

      final String property = constraintAnnotation.property();
      Function<Enum<?>, String> propertyAccessor;
      if (constraintAnnotation.usingGetter()) {
        Method method;
        try {
          method = constraintAnnotation.value().getDeclaredMethod(property);
        } catch (NoSuchMethodException e) {
          throw new IllegalArgumentException("Property is not found: " + property, e);
        }
        propertyAccessor = enumConstant -> {
          try {
            //noinspection unchecked
            return Objects.requireNonNull(method.invoke(enumConstant)).toString();
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Property is not accessible: " + property, e);
          }
        };
        this.enumProperties = stream.map(propertyAccessor).collect(Collectors.toSet());
      } else {
        Field field;
        boolean accessible;
        try {
          field = constraintAnnotation.value().getDeclaredField(property);
          accessible = field.isAccessible();
        } catch (NoSuchFieldException e) {
          throw new IllegalArgumentException("Property is not found: " + property, e);
        }
        field.setAccessible(true);
        propertyAccessor = enumConstant -> {
          try {
            //noinspection unchecked
            return Objects.requireNonNull(field.get(enumConstant)).toString();
          } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Property is not accessible: " + property, e);
          }
        };
        try {
          this.enumProperties = stream.map(propertyAccessor).collect(Collectors.toSet());
        } finally {
          field.setAccessible(accessible);
        }
      }
    }

    protected abstract boolean internalValidate(T value);

    protected boolean contains(Object element) {
      return enumProperties.contains(Objects.toString(element, null));
    }

    protected boolean containsAll(Object[] elements) {
      return Stream.of(elements)
          .map(e -> Objects.toString(e, null))
          .allMatch(enumProperties::contains);
    }

    protected <C extends Collection<?>> boolean containsAll(C elements) {
      return elements.stream()
          .map(e -> Objects.toString(e, null))
          .allMatch(enumProperties::contains);
    }
  }

  /**
   * example:
   *
   * @EnumPropertySet(Switch.class) private Integer status;
   */
  class EnumPropertySetValidator extends EnumPropertySetValidatorSupport<Object> {

    @Override
    protected boolean internalValidate(Object value) {
      return contains(value);
    }

  }

  /**
   * example:
   *
   * @EnumPropertySet(Switch.class) private Integer[] statuses;
   */
  class EnumPropertySetArrayValidator extends EnumPropertySetValidatorSupport<Object[]> {

    @Override
    protected boolean internalValidate(Object[] values) {
      return containsAll(values);
    }

  }

  /**
   * example:
   *
   * @EnumPropertySet(Switch.class) private List<Integer> statuses;
   */
  class EnumPropertySetCollectionValidator extends EnumPropertySetValidatorSupport<Collection<?>> {

    @Override
    protected boolean internalValidate(Collection<?> values) {
      return containsAll(values);
    }

  }

  /**
   * example:
   *
   * @EnumPropertySet(Switch.class) private String status;
   * <p>
   * example:
   * @EnumPropertySet(value = Switch.class, separator = ",") private String statuses;
   */
  class EnumPropertySetStringValidator extends EnumPropertySetValidatorSupport<String> {

    private String separator;

    @Override
    public void initialize(EnumPropertySet constraintAnnotation) {
      super.initialize(constraintAnnotation);
      separator = constraintAnnotation.separator();
    }

    @Override
    protected boolean internalValidate(String value) {
      return value.isEmpty() || (separator.isEmpty() ? contains(value)
          : containsAll(value.split(separator)));
    }

  }

  /**
   * example:
   *
   * @EnumPropertySet(Switch.class) private String[] statuses;
   */
  class EnumPropertySetStringArrayValidator extends EnumPropertySetValidatorSupport<String[]> {

    @Override
    protected boolean internalValidate(String[] values) {
      return containsAll(values);
    }

  }

  /**
   * example:
   *
   * @EnumPropertySet(Switch.class) private String<String> statuses;
   */
  class EnumPropertySetStringCollectionValidator extends
      EnumPropertySetValidatorSupport<Collection<String>> {

    @Override
    protected boolean internalValidate(Collection<String> values) {
      return containsAll(values);
    }

  }

}
