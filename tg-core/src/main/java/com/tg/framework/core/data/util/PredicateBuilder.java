package com.tg.framework.core.data.util;

import com.tg.framework.commons.lang.StringOptional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.persistence.criteria.Predicate;

public class PredicateBuilder {

  private List<Predicate> predicates;

  private PredicateBuilder() {
    this.predicates = new ArrayList<>();
  }

  private PredicateBuilder(int initialCapacity) {
    this.predicates = new ArrayList<>(initialCapacity);
  }

  public static Predicate[] empty() {
    return null;
  }

  public static PredicateBuilder instance() {
    return new PredicateBuilder();
  }

  public static PredicateBuilder of(int initialCapacity) {
    return new PredicateBuilder(initialCapacity);
  }

  private void addPredicate(Predicate predicate) {
    predicates.add(predicate);
  }

  public <T> PredicateBuilder addOnCondition(boolean condition, T value,
      Function<T, Predicate> mapper) {
    if (condition) {
      predicates.add(mapper.apply(value));
    }
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

  public <T, C extends Collection<T>> PredicateBuilder addIfy(C value,
      Function<C, Predicate> mapper, boolean skipEmpty) {
    if (skipEmpty) {
      Optional.ofNullable(value).filter(v -> v.size() > 0).map(mapper).ifPresent(this::addPredicate);
      return this;
    }
    Optional.ofNullable(value).map(mapper).ifPresent(this::addPredicate);
    return this;
  }

  public <T, C extends Collection<T>> PredicateBuilder addIfy(C value,
      Function<C, Predicate> mapper) {
    return addIfy(value, mapper, false);
  }

  public <T> PredicateBuilder addIfy(T[] value, Function<T[], Predicate> mapper,
      boolean skipEmpty) {
    if (skipEmpty) {
      Optional.ofNullable(value).filter(v -> v.length > 0).map(mapper).ifPresent(this::addPredicate);
      return this;
    }
    Optional.ofNullable(value).map(mapper).ifPresent(this::addPredicate);
    return this;
  }

  public <T> PredicateBuilder addIfy(T[] value, Function<T[], Predicate> mapper) {
    return addIfy(value, mapper, false);
  }

  public PredicateBuilder addOnBoolean(Boolean value, Function<Void, Predicate> trueMapper,
      Function<Void, Predicate> falseMapper) {
    Optional.ofNullable(value).map(v -> value ? trueMapper.apply(null) : falseMapper.apply(null))
        .ifPresent(this::addPredicate);
    return this;
  }

  public Predicate[] toArray() {
    return this.toArray(new Predicate[this.predicates.size()]);
  }

  public Predicate[] toArray(Predicate[] arr) {
    return this.predicates.toArray(arr);
  }

}
