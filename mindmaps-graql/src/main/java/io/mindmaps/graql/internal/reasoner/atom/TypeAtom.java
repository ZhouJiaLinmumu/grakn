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
package io.mindmaps.graql.internal.reasoner.atom;

import io.mindmaps.concept.Type;
import io.mindmaps.graql.admin.VarAdmin;
import io.mindmaps.graql.internal.reasoner.query.Query;

public class TypeAtom extends Binary{

    public TypeAtom(VarAdmin pattern) {
        super(pattern);
    }
    public TypeAtom(VarAdmin pattern, Query par) {
        super(pattern, par);
    }
    private TypeAtom(TypeAtom a) { super(a);}

    @Override
    protected String extractValueVariableName(VarAdmin var) {
        return var.getProperties().findFirst().orElse(null).getTypes().findFirst().orElse(null).getName();
    }

    @Override
    protected void setValueVariable(String var) {
        valueVariable = var;
        atomPattern.asVar().getProperties().findFirst().orElse(null).getTypes().findFirst().orElse(null).setName(var);
    }

    @Override
    public Atomic clone(){
        return new TypeAtom(this);
    }

    @Override
    public boolean isType(){ return true;}

    @Override
    public Type getType(){
        return getPredicate() != null? getParentQuery().getGraph().orElse(null).getType(getPredicate().getPredicateValue()) : null;
    }
}

