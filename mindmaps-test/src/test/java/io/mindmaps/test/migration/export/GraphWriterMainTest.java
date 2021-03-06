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

package io.mindmaps.test.migration.export;

import io.mindmaps.example.PokemonGraphFactory;
import io.mindmaps.migration.export.Main;
import io.mindmaps.test.migration.AbstractMindmapsMigratorTest;
import org.junit.Before;
import org.junit.Test;

public class GraphWriterMainTest extends AbstractMindmapsMigratorTest {

    @Before
    public void start() {
        PokemonGraphFactory.loadGraph(graph);
    }

    @Before
    public void expectExit(){
        exit.expectSystemExitWithStatus(0);
    }

    @Test
    public void exportOntologyToSystemOutTest(){
        runAndAssertDataCorrect("export", "-ontology", "-keyspace", graph.getKeyspace());
    }

    @Test
    public void exportDataToSystemOutTest(){
        runAndAssertDataCorrect("export", "-data", "-keyspace", graph.getKeyspace());
    }
    
    @Test
    public void exportNoArgsTest(){
        exit.expectSystemExitWithStatus(1);
        runAndAssertDataCorrect("export", "ontology");
    }

    @Test
    public void exportOnlyHelpMessageTest(){
        exit.expectSystemExitWithStatus(1);
        runAndAssertDataCorrect("export", "-h");
    }

    @Test
    public void exportEngineURLProvidedTest(){
        runAndAssertDataCorrect("export", "-data", "-uri", "localhost:4567", "-keyspace", graph.getKeyspace());
    }

    private void runAndAssertDataCorrect(String... args){
        Main.main(args);
    }
}
