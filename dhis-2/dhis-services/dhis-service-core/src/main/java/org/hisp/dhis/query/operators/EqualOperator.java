/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.query.operators;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.query.QueryException;
import org.hisp.dhis.query.QueryUtils;
import org.hisp.dhis.query.Type;
import org.hisp.dhis.query.Typed;
import org.hisp.dhis.query.planner.QueryPath;
import org.hisp.dhis.schema.Property;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class EqualOperator<T extends Comparable<? super T>> extends Operator<T> {
  public EqualOperator(T arg) {
    super("eq", Typed.from(String.class, Boolean.class, Number.class, Date.class, Enum.class), arg);
  }

  public EqualOperator(String name, T arg) {
    super(name, Typed.from(String.class, Boolean.class, Number.class, Date.class, Enum.class), arg);
  }

  @Override
  public Criterion getHibernateCriterion(QueryPath queryPath) {
    Property property = queryPath.getProperty();

    if (property.isCollection()) {
      Integer value = QueryUtils.parseValue(Integer.class, args.get(0));

      if (value == null) {
        throw new QueryException(
            "Left-side is collection, and right-side is not a valid integer, so can't compare by size.");
      }

      return Restrictions.sizeEq(queryPath.getPath(), value);
    }

    return Restrictions.eq(queryPath.getPath(), args.get(0));
  }

  @Override
  public <Y> Predicate getPredicate(CriteriaBuilder builder, Root<Y> root, QueryPath queryPath) {
    Property property = queryPath.getProperty();

    if (property.isCollection()) {
      Integer value = QueryUtils.parseValue(Integer.class, args.get(0));

      if (value == null) {
        throw new QueryException(
            "Left-side is collection, and right-side is not a valid integer, so can't compare by size.");
      }

      return builder.equal(builder.size(root.get(queryPath.getPath())), value);
    }

    if (queryPath.haveAlias()) {
      Optional<Join<Y, ?>> join =
          root.getJoins().stream()
              .filter(j -> j.getAlias().equals(queryPath.getAlias()[0]))
              .findFirst();
      if (join.isPresent()) {
        return builder.equal(join.get().get(queryPath.getProperty().getFieldName()), args.get(0));
      }
    }
    return builder.equal(root.get(queryPath.getPath()), args.get(0));
  }

  @Override
  public boolean test(Object value) {
    if (args.isEmpty() || value == null) {
      return false;
    }

    Type type = new Type(value);

    if (type.isString()) {
      String s1 = getValue(String.class);
      String s2 = (String) value;

      return s2.equals(s1);
    }
    if (type.isBoolean()) {
      Boolean s1 = getValue(Boolean.class);
      Boolean s2 = (Boolean) value;

      return s2.equals(s1);
    }
    if (type.isInteger()) {
      Integer s1 = getValue(Integer.class);
      Integer s2 = (Integer) value;

      return s2.equals(s1);
    }
    if (type.isFloat()) {
      Float s1 = getValue(Float.class);
      Float s2 = (Float) value;

      return s2.equals(s1);
    }
    if (type.isCollection()) {
      Collection<?> collection = (Collection<?>) value;
      Integer size = getValue(Integer.class);

      return size != null && collection.size() == size;
    }
    if (type.isMap()) {
      Map<?, ?> map = (Map<?, ?>) value;
      Integer size = getValue(Integer.class);

      return size != null && map.size() == size;
    }
    if (type.isDate()) {
      Date s1 = getValue(Date.class);
      Date s2 = (Date) value;

      return s2.equals(s1);
    }
    if (type.isEnum()) {
      String s1 = String.valueOf(args.get(0));
      String s2 = String.valueOf(value);

      return s2.equals(s1);
    }

    return false;
  }
}
