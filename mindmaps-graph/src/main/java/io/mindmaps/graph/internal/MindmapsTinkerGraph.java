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

import io.mindmaps.util.ErrorMessage;
import io.mindmaps.util.REST;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

/**
 * A mindmaps graph which uses a Tinkergraph backend.
 * Primarily used for testing
 */
public class MindmapsTinkerGraph extends AbstractMindmapsGraph<TinkerGraph> {
    public MindmapsTinkerGraph(TinkerGraph tinkerGraph, String name, String engineUrl, boolean batchLoading){
        super(tinkerGraph, name, engineUrl, batchLoading);
    }

    @Override
    public void clear(){
        super.clear();
        EngineCommunicator.contactEngine(getCommitLogEndPoint(), REST.HttpConn.DELETE_METHOD);
        initialiseMetaConcepts();
    }

    @Override
    public ConceptImpl getConceptByBaseIdentifier(Object baseIdentifier) {
        return super.getConceptByBaseIdentifier(Long.valueOf(baseIdentifier.toString()));
    }

    @Override
    public void rollback(){
        throw new UnsupportedOperationException(ErrorMessage.UNSUPPORTED_GRAPH.getMessage(getTinkerPopGraph().getClass().getName(), "rollback"));
    }
}
