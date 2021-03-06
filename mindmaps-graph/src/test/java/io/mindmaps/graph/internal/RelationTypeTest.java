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

package io.mindmaps.graph.internal;

import io.mindmaps.concept.RelationType;
import io.mindmaps.concept.RoleType;
import io.mindmaps.exception.ConceptException;
import io.mindmaps.util.ErrorMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RelationTypeTest extends GraphTestBase{
    private RelationType relationType;
    private RoleType role1;
    private RoleType role2;
    private RoleType role3;

    @Before
    public void setUp() throws ConceptException {
        //Building
        relationType = mindmapsGraph.putRelationType("relationType");
        role1 = mindmapsGraph.putRoleType("role1");
        role2 = mindmapsGraph.putRoleType("role2");
        role3 = mindmapsGraph.putRoleType("role3");

        relationType.hasRole(role1);
        relationType.hasRole(role2);
        relationType.hasRole(role3);
    }

    @Test
    public void updateBaseTypeCheck(){
        RelationType relationType = mindmapsGraph.putRelationType("Test");
        RelationType relationType2 = mindmapsGraph.putRelationType("Test");
        assertEquals(relationType, relationType2);
    }

    @Test
    public void overrideFail(){
        RoleType original = mindmapsGraph.putRoleType("Role Type");

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(allOf(
                containsString(ErrorMessage.ID_ALREADY_TAKEN.getMessage(original.getId(), original.toString()))
        ));

        mindmapsGraph.putRelationType(original.getId());
    }

    @Test
    public void testGetRoles() throws Exception {
        Collection<RoleType> roles = relationType.hasRoles();
        assertEquals(3, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
        assertTrue(roles.contains(role3));
    }

    @Test
    public void testRoleType(){
        RelationType c1 = mindmapsGraph.putRelationType("c1");
        RoleType c2 = mindmapsGraph.putRoleType("c2");
        RoleType c3 = mindmapsGraph.putRoleType("c3");
        assertNull(c2.relationType());

        c1.hasRole(c2);
        c1.hasRole(c3);
        assertTrue(c1.hasRoles().contains(c2));
        assertTrue(c1.hasRoles().contains(c3));

        c1.deleteHasRole(c2);
        assertFalse(c1.hasRoles().contains(c2));
        assertTrue(c1.hasRoles().contains(c3));
    }


}