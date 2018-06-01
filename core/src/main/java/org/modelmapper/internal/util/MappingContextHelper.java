package org.modelmapper.internal.util;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.modelmapper.spi.Mapping;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.spi.PropertyInfo;
import org.modelmapper.spi.PropertyMapping;

import net.jodah.typetools.TypeResolver;

/**
 *  Utility class for creating destinations
 */
public final class MappingContextHelper {
  private MappingContextHelper() {
  }

  public static <T> Collection<T> createCollection(MappingContext<?, Collection<T>> context, int length) {
      if (context.getDestinationType().isInterface())
        if (SortedSet.class.isAssignableFrom(context.getDestinationType()))
          return new TreeSet<T>();
        else if (Set.class.isAssignableFrom(context.getDestinationType()))
          return new HashSet<T>();
        else
          return new ArrayList<T>(length);
      return context.getMappingEngine().createDestination(context);
  }

  public static <T> Class<?> resolveCollectionElementType(MappingContext<?, Collection<T>> context) {
    Mapping mapping = context.getMapping();
    if (mapping instanceof PropertyMapping) {
      PropertyInfo destInfo = mapping.getLastDestinationProperty();
      Class<?> elementType = TypeResolver.resolveRawArgument(destInfo.getGenericType(),
          destInfo.getInitialType());
      return elementType == TypeResolver.Unknown.class ? Object.class : elementType;
    } else if (context.getGenericDestinationType() instanceof ParameterizedType)
      return Types.rawTypeFor(((ParameterizedType) context.getGenericDestinationType()).getActualTypeArguments()[0]);

    return Object.class;
  }
}
