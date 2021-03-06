/*
 * MindmapsDB - A Distributed Semantic Database
 * Copyright (C) 2016  Mindmaps Research Ltd
 *
 * MindmapsDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MindmapsDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MindmapsDB. If not, see <http://www.gnu.org/licenses/gpl.txt>.
 */

package io.mindmaps.graql.internal.query.aggregate;

import com.google.common.collect.ImmutableSet;
import io.mindmaps.concept.Concept;
import io.mindmaps.graql.Aggregate;
import io.mindmaps.graql.NamedAggregate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Aggregates {

    private Aggregates() {}

    /**
     * Aggregate that finds average (mean) of a match query.
     */
    public static Aggregate<Map<String, Concept>, Optional<Double>> average(String varName) {
        return new AverageAggregate(varName);
    }

    /**
     * Aggregate that counts results of a match query.
     */
    public static Aggregate<Object, Long> count() {
        return new CountAggregate();
    }

    /**
     * Aggregate that groups results of a match query by variable name, applying an aggregate to each group.
     * @param <T> the type of each group
     */
    public static <T> Aggregate<Map<String, Concept>, Map<Concept, T>> group(
            String varName, Aggregate<? super Map<String, Concept>, T> innerAggregate
    ) {
        return new GroupAggregate<>(varName, innerAggregate);
    }

    /**
     * An aggregate that changes match query results into a list.
     * @param <T> the type of the results of the match query
     */
    public static <T> Aggregate<T, List<T>> list() {
        return new ListAggregate<>();
    }

    /**
     * Aggregate that finds maximum of a match query.
     */
    public static Aggregate<Map<String, Concept>, Optional<?>> max(String varName) {
        return new MaxAggregate(varName);
    }

    /**
     * Aggregate that finds median of a match query.
     */
    public static Aggregate<Map<String, Concept>, Optional<Number>> median(String varName) {
        return new MedianAggregate(varName);
    }

    /**
     * Aggregate that finds minimum of a match query.
     */
    public static Aggregate<Map<String, Concept>, Optional<?>> min(String varName) {
        return new MinAggregate(varName);
    }

    /**
     * An aggregate that combines several aggregates together into a map (where keys are the names of the aggregates)
     * @param <S> the type of the match query results
     * @param <T> the type of the aggregate results
     */
    public static <S, T> Aggregate<S, Map<String, T>> select(
            ImmutableSet<NamedAggregate<? super S, ? extends T>> aggregates
    ) {
        return new SelectAggregate<>(aggregates);
    }

    /**
     * Aggregate that sums results of a match query.
     */
    public static Aggregate<Map<String, Concept>, Number> sum(String varName) {
        return new SumAggregate(varName);
    }
}
