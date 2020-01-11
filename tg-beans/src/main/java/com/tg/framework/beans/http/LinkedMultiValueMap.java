package com.tg.framework.beans.http;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class LinkedMultiValueMap<K, V> extends LinkedHashMap<K, List<V>> implements
    MultiValueMap<K, V> {

  private static final long serialVersionUID = 1804164090368597681L;

  private V getFirst(List<V> values) {
    return values != null && !values.isEmpty() ? values.get(0) : null;
  }

  @Override
  public V getFirst(K key) {
    return Optional.ofNullable(get(key))
        .map(this::getFirst)
        .orElse(null);
  }

  @Override
  public void add(K key, V value) {
    computeIfAbsent(key, k -> new ArrayList<>()).add(value);
  }

  @Override
  public void addAll(K key, List<? extends V> values) {
    computeIfAbsent(key, k -> new ArrayList<>()).addAll(values);
  }

  @Override
  public void addAll(MultiValueMap<K, V> values) {
    values.forEach(this::addAll);
  }

  @Override
  public void set(K key, V value) {
    List<V> values = new ArrayList<>();
    values.add(value);
    put(key, values);
  }

  @Override
  public void setAll(Map<K, V> values) {
    values.forEach(this::set);
  }

  @Override
  public Map<K, V> toSingleValueMap() {
    return entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> getFirst(e.getValue())));
  }
}
