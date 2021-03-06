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

import io.mindmaps.concept.Rule;
import io.mindmaps.concept.RuleType;
import io.mindmaps.concept.Type;
import io.mindmaps.exception.InvalidConceptValueException;
import io.mindmaps.graql.Pattern;
import io.mindmaps.util.ErrorMessage;
import io.mindmaps.util.Schema;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RuleTest extends GraphTestBase{
    private Pattern lhs;
    private Pattern rhs;

    @Before
    public void setupRules(){
        lhs = mindmapsGraph.graql().parsePattern("$x isa entity-type");
        rhs = mindmapsGraph.graql().parsePattern("$x isa entity-type");
    }

    @Test
    public void testType() {
        RuleType conceptType = mindmapsGraph.putRuleType("A Thing");
        Rule rule = mindmapsGraph.addRule(lhs, rhs, conceptType);
        assertNotNull(rule.type());
        assertEquals(conceptType, rule.type());
    }

    @Test
    public void testRuleValues() throws Exception {
        RuleType conceptType = mindmapsGraph.putRuleType("A Thing");
        Rule rule = mindmapsGraph.addRule(lhs, rhs, conceptType);
        assertEquals(lhs, rule.getLHS());
        assertEquals(rhs, rule.getRHS());

        expectedException.expect(InvalidConceptValueException.class);
        expectedException.expectMessage(allOf(
                containsString(ErrorMessage.NULL_VALUE.getMessage(Schema.ConceptProperty.RULE_LHS))
        ));

        mindmapsGraph.addRule(null, null, conceptType);
    }

    @Test
    public void testExpectation() throws Exception {
        RuleType conceptType = mindmapsGraph.putRuleType("A Thing");
        Rule rule = mindmapsGraph.addRule(lhs, rhs, conceptType);
        assertFalse(rule.getExpectation());
        rule.setExpectation(true);
        assertTrue(rule.getExpectation());
    }

    @Test
    public void testMaterialise() throws Exception {
        RuleType conceptType = mindmapsGraph.putRuleType("A Thing");
        Rule rule = mindmapsGraph.addRule(lhs, rhs, conceptType);
        assertFalse(rule.isMaterialise());
        rule.setMaterialise(true);
        assertTrue(rule.isMaterialise());
    }

    @Test
    public void testAddHypothesis() throws Exception {
        RuleType conceptType = mindmapsGraph.putRuleType("A Thing");
        Rule rule = mindmapsGraph.addRule(lhs, rhs, conceptType);
        Vertex ruleVertex = mindmapsGraph.getTinkerPopGraph().traversal().V(((RuleImpl) rule).getBaseIdentifier()).next();
        Type type1 = mindmapsGraph.putEntityType("A Concept Type 1");
        Type type2 = mindmapsGraph.putEntityType("A Concept Type 2");
        assertFalse(ruleVertex.edges(Direction.BOTH, Schema.EdgeLabel.HYPOTHESIS.getLabel()).hasNext());
        rule.addHypothesis(type1).addHypothesis(type2);
        assertTrue(ruleVertex.edges(Direction.BOTH, Schema.EdgeLabel.HYPOTHESIS.getLabel()).hasNext());
    }

    @Test
    public void testAddConclusion() throws Exception {
        RuleType conceptType = mindmapsGraph.putRuleType("A Thing");
        Rule rule = mindmapsGraph.addRule(lhs, rhs, conceptType);
        Vertex ruleVertex = mindmapsGraph.getTinkerPopGraph().traversal().V(((RuleImpl) rule).getBaseIdentifier()).next();
        Type type1 = mindmapsGraph.putEntityType("A Concept Type 1");
        Type type2 = mindmapsGraph.putEntityType("A Concept Type 2");
        assertFalse(ruleVertex.edges(Direction.BOTH, Schema.EdgeLabel.CONCLUSION.getLabel()).hasNext());
        rule.addConclusion(type1).addConclusion(type2);
        assertTrue(ruleVertex.edges(Direction.BOTH, Schema.EdgeLabel.CONCLUSION.getLabel()).hasNext());
    }

    @Test
    public void testHypothesisTypes(){
        RuleType ruleType = mindmapsGraph.putRuleType("A Rule Type");
        Rule rule = mindmapsGraph.addRule(lhs, rhs, ruleType);
        assertEquals(0, rule.getHypothesisTypes().size());

        Type ct1 = mindmapsGraph.putEntityType("A Concept Type 1");
        Type ct2 = mindmapsGraph.putEntityType("A Concept Type 2");
        rule.addHypothesis(ct1).addHypothesis(ct2);
        assertEquals(2, rule.getHypothesisTypes().size());
        assertTrue(rule.getHypothesisTypes().contains(ct1));
        assertTrue(rule.getHypothesisTypes().contains(ct2));
    }

    @Test
    public void testConclusionTypes(){
        RuleType ruleType = mindmapsGraph.putRuleType("A Rule Type");
        Rule rule = mindmapsGraph.addRule(lhs, rhs, ruleType);
        assertEquals(0, rule.getConclusionTypes().size());

        Type ct1 = mindmapsGraph.putEntityType("A Concept Type 1");
        Type ct2 = mindmapsGraph.putEntityType("A Concept Type 2");
        rule.addConclusion(ct1).addConclusion(ct2);
        assertEquals(2, rule.getConclusionTypes().size());
        assertTrue(rule.getConclusionTypes().contains(ct1));
        assertTrue(rule.getConclusionTypes().contains(ct2));
    }

}