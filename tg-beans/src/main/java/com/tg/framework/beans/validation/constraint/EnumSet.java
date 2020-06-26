package com.tg.framework.beans.validation.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.tg.framework.beans.validation.constraint.EnumSet.EnumSetArrayValidator;
import com.tg.framework.beans.validation.constraint.EnumSet.EnumSetCollectionValidator;
import com.tg.framework.beans.validation.constraint.EnumSet.EnumSetStringArrayValidator;
import com.tg.framework.beans.validation.constraint.EnumSet.EnumSetStringCollectionValidator;
import com.tg.framework.beans.validation.constraint.EnumSet.EnumSetStringValidator;
import com.tg.framework.beans.validation.constraint.EnumSet.EnumSetValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
@Constraint(validatedBy = {EnumSetValidator.class, EnumSetArrayValidator.class,
    EnumSetCollectionValidator.class, EnumSetStringValidator.class,
    EnumSetStringArrayValidator.class, EnumSetStringCollectionValidator.class})
public @interface EnumSet {

  String ENUMS = "enums";

  String message() default "{com.tg.framework.beans.validation.constraint.EnumSet.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends Enum<?>> value();

  String[] excludes() default {};


  abstract class EnumSetValidatorSupport<V, T> implements ConstraintValidator<EnumSet, T> {

    private Set<V> enums;

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
      boolean isValid = value == null || internalValidate(value);
      if (!isValid) {
        context.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter(ENUMS, enums);
      }
      return isValid;
    }

    @Override
    public void initialize(EnumSet constraintAnnotation) {
      Stream<Enum<?>> stream = Stream.of(constraintAnnotation.value().getEnumConstants());
      if (constraintAnnotation.excludes().length > 0) {
        Set<String> excludes = new HashSet<>(Arrays.asList(constraintAnnotation.excludes()));
        stream = stream.filter(e -> excludes.contains(e.name()));
      }
      this.enums = stream.map(this::resolveEnumValue).collect(Collectors.toSet());
    }

    protected abstract V resolveEnumValue(Enum<?> constant);

    protected abstract boolean internalValidate(T value);

    protected <E> E noneConverter(E element) {
      return element;
    }

    protected <E> boolean contains(E element, Function<E, V> converter) {
      return enums.contains(converter.apply(element));
    }

    protected <E> boolean containsAll(E[] elements, Function<E, V> converter) {
      return Stream.of(elements).map(converter).allMatch(enums::contains);
    }

    protected <E, C extends Collection<? extends E>> boolean containsAll(C elements,
        Function<E, V> converter) {
      return elements.stream().map(converter).allMatch(enums::contains);
    }
  }

  /**
   * example:
   *
   * @EnumSet(Switch.class) private Switch status;
   */
  class EnumSetValidator extends EnumSetValidatorSupport<Enum<?>, Enum<?>> {

    @Override
    protected Enum<?> resolveEnumValue(Enum<?> constant) {
      return constant;
    }

    @Override
    protected boolean internalValidate(Enum<?> value) {
      return contains(value, this::noneConverter);
    }

  }

  /**
   * example:
   *
   * @EnumSet(Switch.class) private Switch[] statuses;
   */
  class EnumSetArrayValidator extends EnumSetValidatorSupport<Enum<?>, Enum<?>[]> {

    @Override
    protected Enum<?> resolveEnumValue(Enum<?> constant) {
      return constant;
    }

    @Override
    protected boolean internalValidate(Enum<?>[] values) {
      return containsAll(values, this::noneConverter);
    }

  }

  /**
   * example:
   *
   * @EnumSet(Switch.class) private List<Switch> statuses;
   */
  class EnumSetCollectionValidator extends EnumSetValidatorSupport<Enum<?>, Collection<Enum<?>>> {

    @Override
    protected Enum<?> resolveEnumValue(Enum<?> constant) {
      return constant;
    }

    @Override
    protected boolean internalValidate(Collection<Enum<?>> values) {
      return containsAll(values, this::noneConverter);
    }

  }

  /**
   * example:
   *
   * @EnumSet(Switch.class) private String status;
   */
  class EnumSetStringValidator extends EnumSetValidatorSupport<String, String> {

    @Override
    protected String resolveEnumValue(Enum<?> constant) {
      return constant.name();
    }

    @Override
    protected boolean internalValidate(String value) {
      return value.isEmpty() || contains(value, this::noneConverter);
    }

  }

  /**
   * example:
   *
   * @EnumSet(Switch.class) private String[] statuses;
   */
  class EnumSetStringArrayValidator extends EnumSetValidatorSupport<String, String[]> {

    @Override
    protected String resolveEnumValue(Enum<?> constant) {
      return constant.name();
    }

    @Override
    protected boolean internalValidate(String[] values) {
      return containsAll(values, this::noneConverter);
    }

  }

  /**
   * example:
   *
   * @EnumSet(Switch.class) private List<String> statuses;
   */
  class EnumSetStringCollectionValidator extends
      EnumSetValidatorSupport<String, Collection<String>> {

    @Override
    protected String resolveEnumValue(Enum<?> constant) {
      return constant.name();
    }

    @Override
    protected boolean internalValidate(Collection<String> values) {
      return containsAll(values, this::noneConverter);
    }

  }

}
