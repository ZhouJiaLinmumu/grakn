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

package io.mindmaps.graql.internal.template;

import io.mindmaps.exception.GraqlParsingException;
import io.mindmaps.graql.internal.antlr.GraqlTemplateLexer;
import io.mindmaps.graql.internal.antlr.GraqlTemplateParser;
import io.mindmaps.graql.internal.parser.GraqlErrorListener;
import io.mindmaps.graql.internal.template.macro.DoubleMacro;
import io.mindmaps.graql.internal.template.macro.IntMacro;
import io.mindmaps.graql.internal.template.macro.Macro;
import io.mindmaps.graql.internal.template.macro.NoescpMacro;
import io.mindmaps.graql.internal.template.macro.EqualsMacro;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.Map;

public class TemplateParser {

    private final Map<String, Macro<Object>> macros = new HashMap<>();

    /**
     * Create a template parser
     */
    private TemplateParser(){
        registerDefaultMacros();
    }

    /**
     * Create a template parser
     * @return a template parser
     */
    public static TemplateParser create(){
        return new TemplateParser();
    }

    public void registerMacro(String name, Macro macro){
        macros.put(name, macro);
    }

    /**
     * Parse and resolve a graql template
     * @param templateString a string representing a graql template
     * @param data data to use in template
     * @return resolved graql query string
     */
    public String parseTemplate(String templateString, Map<String, Object> data){
        GraqlErrorListener errorListener = new GraqlErrorListener(templateString);

        CommonTokenStream tokens = lexGraqlTemplate(templateString, errorListener);
        ParseTree tree = parseGraqlTemplate(tokens, errorListener);

        TemplateVisitor visitor = new TemplateVisitor(tokens, data, macros);
        return visitor.visit(tree).toString();
    }


    private CommonTokenStream lexGraqlTemplate(String templateString, GraqlErrorListener errorListener){
        ANTLRInputStream inputStream = new ANTLRInputStream(templateString);
        GraqlTemplateLexer lexer = new GraqlTemplateLexer(inputStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        return new CommonTokenStream(lexer);
    }

    private ParseTree parseGraqlTemplate(CommonTokenStream tokens, GraqlErrorListener errorListener){
        GraqlTemplateParser parser = new GraqlTemplateParser(tokens);
        parser.setBuildParseTree(true);

        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.template();

        if(errorListener.hasErrors()){
            throw new GraqlParsingException(errorListener.toString());
        }

        return tree;
    }

    /**
     * Register the default macros that can be used by the visitor
     */
    private void registerDefaultMacros(){

        registerMacro("noescp", new NoescpMacro());
        registerMacro("int", new IntMacro());
        registerMacro("double", new DoubleMacro());
        registerMacro("equals", new EqualsMacro());
    }
}
