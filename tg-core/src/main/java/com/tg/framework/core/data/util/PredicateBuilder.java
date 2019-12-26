package com.tg.framework.core.data.util;

import com.tg.framework.commons.lang.StringOptional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Predicate;

public class PredicateBuilder {

  private List<Predicate> predicates;

  private PredicateBuilder() {
    this.predicates = new ArrayList<>();
  }

  private PredicateBuilder(int maxSize) {
    this.predicates = new ArrayList<>(maxSize + 1);
  }

  public static Predicate[] empty() {
    return null;
  }

  public static PredicateBuilder instance() {
    return new PredicateBuilder();
  }

  public static PredicateBuilder of(int maxSize) {
    return new PredicateBuilder(maxSize);
  }

  public PredicateBuilder addPredicate(Predicate predicate) {
    predicates.add(predicate);
    return this;
  }

  public <T> PredicateBuilder addOnCondition(boolean condition, T value,
      Function<T, Predicate> mapper) {
    if (condition) {
      predicates.add(mapper.apply(value));
    }
    return this;
  }

  public PredicateBuilder addOnBoolean(Boolean value, Function<Void, Predicate> trueMapper,
      Function<Void, Predicate> falseMapper) {
    Optional.ofNullable(value).map(v -> value ? trueMapper.apply(null) : falseMapper.apply(null))
        .ifPresent(this::addPredicate);
    return this;
  }

  public <T> PredicateBuilder addIfy(T value, Function<T, Predicate> mapper) {
    Optional.ofNullable(value).map(mapper).ifPresent(this::addPredicate);
    return this;
  }

  public PredicateBuilder addIfy(String value, Function<String, Predicate> mapper,
      boolean skipEmpty) {
    if (skipEmpty) {
      StringOptional.ofNullable(value).map(mapper).ifPresent(this::addPredicate);
    }
    Optional.ofNullable(value).map(mapper).ifPresent(this::addPredicate);
    return this;
  }

  public PredicateBuilder addIfy(String value, Function<String, Predicate> mapper) {
    return addIfy(value, mapper, true);
  }

  public PredicateBuilder addStartsWithIfy(String value, Function<String, Predicate> mapper,
      boolean skipEmpty) {
    if (skipEmpty) {
      StringOptional.ofNullable(value).map(SQLUtils::startsWith).map(mapper)
          .ifPresent(this::addPredicate);
    }
    Optional.ofNullable(value).map(SQLUtils::startsWith).map(mapper).ifPresent(this::addPredicate);
    return this;
  }

  public PredicateBuilder addStartsWithIfy(String value, Function<String, Predicate> mapper) {
    return addStartsWithIfy(value, mapper, true);
  }

  public PredicateBuilder addEndsWithIfy(String value, Function<String, Predicate> mapper,
      boolean skipEmpty) {
    if (skipEmpty) {
      StringOptional.ofNullable(value).map(SQLUtils::endsWith).map(mapper)
          .ifPresent(this::addPredicate);
    }
    Optional.ofNullable(value).map(SQLUtils::endsWith).map(mapper).ifPresent(this::addPredicate);
    return this;
  }

  public PredicateBuilder addEndsWithIfy(String value, Function<String, Predicate> mapper) {
    return addEndsWithIfy(value, mapper, true);
  }

  public PredicateBuilder addContainsIfy(String value, Function<String, Predicate> mapper,
      boolean skipEmpty) {
    if (skipEmpty) {
      StringOptional.ofNullable(value).map(SQLUtils::contains).map(mapper)
          .ifPresent(this::addPredicate);
    }
    Optional.ofNullable(value).map(SQLUtils::endsWith).map(mapper).ifPresent(this::addPredicate);
    return this;
  }

  public PredicateBuilder addContainsIfy(String value, Function<String, Predicate> mapper) {
    return addContainsIfy(value, mapper, true);
  }

  public <T, C extends Collection<T>> PredicateBuilder addInIfy(C value, Function<C, In<T>> mapper,
      boolean skipEmpty) {
    if (value != null && (skipEmpty || !value.isEmpty())) {
      In<T> in = mapper.apply(value);
      value.forEach(in::value);
      addPredicate(in);
    }
    return this;
  }

  public <T, C extends Collection<T>> PredicateBuilder addInIfy(C value,
      Function<C, In<T>> mapper) {
    return addInIfy(value, mapper, false);
  }

  public <T> PredicateBuilder addInIfy(T[] value, Function<T[], In<T>> mapper, boolean skipEmpty) {
    if (value != null && (skipEmpty || value.length != 0)) {
      In<T> in = mapper.apply(value);
      Stream.of(value).forEach(in::value);
      addPredicate(in);
    }
    return this;
  }

  public <T> PredicateBuilder addInIfy(T[] value, Function<T[], In<T>> mapper) {
    return addInIfy(value, mapper, false);
  }

  public <T, C extends Collection<T>> PredicateBuilder addNotInIfy(C value,
      Function<C, In<T>> mapper, boolean skipEmpty) {
    if (value != null && (skipEmpty || !value.isEmpty())) {
      In<T> in = mapper.apply(value);
      value.forEach(in::value);
      addPredicate(in.not());
    }
    return this;
  }

  public <T, C extends Collection<T>> PredicateBuilder addNotInIfy(C value,
      Function<C, In<T>> mapper) {
    return addNotInIfy(value, mapper, false);
  }

  public <T> PredicateBuilder addNotInIfy(T[] value, Function<T[], In<T>> mapper,
      boolean skipEmpty) {
    if (value != null && (skipEmpty || value.length != 0)) {
      In<T> in = mapper.apply(value);
      Stream.of(value).forEach(in::value);
      addPredicate(in.not());
    }
    return this;
  }

  public <T> PredicateBuilder addNotInIfy(T[] value, Function<T[], In<T>> mapper) {
    return addNotInIfy(value, mapper, false);
  }

  public Predicate[] toArray() {
    return this.toArray(new Predicate[this.predicates.size()]);
  }

  public Predicate[] toArray(Predicate[] arr) {
    return this.predicates.toArray(arr);
  }

}
