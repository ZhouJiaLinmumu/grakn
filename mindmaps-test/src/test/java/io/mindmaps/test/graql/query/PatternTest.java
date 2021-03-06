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

package io.mindmaps.test.graql.query;

import io.mindmaps.graql.Graql;
import io.mindmaps.graql.Pattern;
import org.junit.Test;

import static io.mindmaps.graql.Graql.and;
import static io.mindmaps.graql.Graql.or;
import static io.mindmaps.graql.Graql.var;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatternTest {

    @Test
    public void testVarPattern() {
        Pattern x = var("x");

        assertTrue(x.admin().isVar());
        assertFalse(x.admin().isDisjunction());
        assertFalse(x.admin().isConjunction());

        assertEquals(x.admin(), x.admin().asVar());
    }

    @Test
    public void testDisjunction() {
        Pattern disjunction = or();

        assertFalse(disjunction.admin().isVar());
        assertTrue(disjunction.admin().isDisjunction());
        assertFalse(disjunction.admin().isConjunction());

        //noinspection AssertEqualsBetweenInconvertibleTypes
        assertEquals(disjunction.admin(), disjunction.admin().asDisjunction());
    }

    @Test
    public void testConjunction() {
        Pattern conjunction = and();

        assertFalse(conjunction.admin().isVar());
        assertFalse(conjunction.admin().isDisjunction());
        assertTrue(conjunction.admin().isConjunction());

        //noinspection AssertEqualsBetweenInconvertibleTypes
        assertEquals(conjunction.admin(), conjunction.admin().asConjunction());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConjunctionAsVar() {
        Graql.and(var("x").isa("movie"), var("x").isa("person")).admin().asVar();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDisjunctionAsConjunction() {
        Graql.or(var("x").isa("movie"), var("x").isa("person")).admin().asConjunction();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVarAsDisjunction() {
        var("x").isa("movie").admin().asDisjunction();
    }
}
