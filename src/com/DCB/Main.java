package com.DCB;

import com.DCB.Interpreter.InterpreterManager;
import com.DCB.ParserObjects.CouplingObject;

import java.io.*;
import java.util.Scanner;

/*
 * Class:       CS 4308 Section 2
 * Term:        Fall 2019
 * Name:        Robert, Chris, James
 * Instructor:   Deepa Muralidhar
 * Project:  Deliverable 1 Scanner - Java
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("Files/TestDocument.julia");
        Scanner scanner = new Scanner(file);
        ScriptReader scriptReader = new ScriptReader(scanner);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(scriptReader);
        for(Object o: lexicalAnalyzer.getAnalyzedScript()) {
            System.out.println(o);
        }
        try {
            usingBufferedWriter(lexicalAnalyzer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Identifiers]=========================");
        Parser parser = new Parser(lexicalAnalyzer.getAnalyzedScript(),lexicalAnalyzer.getScriptLines(),lexicalAnalyzer.getCurrentLineNumber());
        for(int i = 0; i < parser.getParsedStatements().size(); i++) {
            System.out.println(parser.getParsedStatements().get(i).getStringIdentifier());
        }

        System.out.println("[Grammer]=========================");
        System.out.println("\n<program> -> function id ( ) <block> end");


        for(int i = 0; i < parser.getParsedStatements().size(); i++) {
            Object o = parser.getParsedStatements().get(i);
            System.out.println(parser.getParsedStatements().get(i).getParsedGrammar());
        }


        System.out.println("[Interpreter]=========================");
        InterpreterManager interpreterManager = new InterpreterManager(parser.getParsedStatements(),parser.getIdentifers());
        interpreterManager.executeAllStatements();

    }


    public static void usingBufferedWriter(LexicalAnalyzer lexicalAnalyzer) throws IOException
    {
        String fileContent = "";
        for(Object o: lexicalAnalyzer.getAnalyzedScript()) {
            fileContent += o + "\n";
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("Files/OutputFile.txt"));
        writer.write(fileContent);
        writer.close();
    }
}
