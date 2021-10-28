package com.DCB;

import com.DCB.LexicalObjects.Identifier;
import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;

import java.util.ArrayList;

/*
 * Class:       CS 4308 Section 2
 * Term:        Fall 2019
 * Name:        Robert, Chris, James
 * Instructor:   Deepa Muralidhar
 * Project:  Deliverable 1 Scanner - Java
 */

public class LexicalAnalyzer {
    // Every token we have currently analyzed, can be of object type KeyWord, Value, or Identifier
    private final ArrayList<Object> analyzedScript = new ArrayList<>();
    private final ArrayList<Integer> scriptLines = new ArrayList<>();

    // Keeping track of our Identifiers, aka Variables as they are declared so we can reuse their objects across each instance
    private final ArrayList<Identifier> identifiers = new ArrayList<>();


    // Store the script Reader as an class instance variable so the Number override can check it
    private final ScriptReader scriptReader;
    // Current String of characters we are seeing is a valid keyword
    private String currentString;
    // A special boolean that trips when a Number grabs a extra character to see when the end of the number is
    // By tripping this we know an extra character was taken and can compensate
    private int currentLineNumber = 1; //DJM 11.13.2019: Revised to start at 1 since lines of paper start with 1.

    public LexicalAnalyzer(ScriptReader scriptReader) {
        this.scriptReader = scriptReader;
        currentString = "";

        // We keep track of every failed character we have pulled out till we get a success or run out of characters
        // Keep going on the loop till we run out of document
        // If there was a number override, we also check since a extra character has been stored
        while (scriptReader.hasNextChar()) {
            // If there is a number override we check the char we stored first instead, incase its a single char lexime
            char c = scriptReader.getNextChar();
            // We ignore spaces like a bad ass :O
            if (c == '\n') {
                currentLineNumber++;
            }
            if (c != ' ' && c != '\n' && c != '\r' && c != '\t') {
                // Add this character to our current collection of characters
                currentString += c;
            }
            if (c == ' ' || c == '\n' || !scriptReader.hasNextChar()) {
                // This if statement will check and see if it can create a valid VariableType, Operator, Function Token, or Value
                // From the Given input, if it passes, it resets the currentString and repeats the cycle
                // If it fails, like this if statement requires, then it tries to see if its a valid identifier
                if (currentString.length() > 0) {
                    if (!createOperatorToken(currentString) && !createFunctionToken(currentString) && !createValueToken(currentString)) {
                        createIdentifierToken(currentString);
                    }
                    currentString = "";
                } else {
                    System.out.println("Error at line " + currentLineNumber + ". Too many spaces.");
                    currentString = "";
                }
            }
        }
        // This ends when we run out of characters, so all the current String, aka unprocessed Characters should be done
        if (currentString.length() >= 1) {
            System.out.println("Error: Unprocessed Characters Remaining of count " + currentString.length());
            System.out.println("Invalid:" + currentString);
        }
    }




    // Return True if it can create a valid Operator Keyword from given Input
    public boolean createOperatorToken(String input) {
        switch(input) {
            case "<":
                analyzedScript.add(KeyWord.LESS_THAN);
                scriptLines.add(currentLineNumber);
                return true;
            case ">":
                analyzedScript.add(KeyWord.GREATER_THAN);
                scriptLines.add(currentLineNumber);
                return true;
            case "<=":
                analyzedScript.add(KeyWord.EQUAL_LESS_THAN);
                scriptLines.add(currentLineNumber);
                return true;
            case ">=":
                analyzedScript.add(KeyWord.EQUAL_GREATER_THAN);
                scriptLines.add(currentLineNumber);
                return true;
            case "==":
                analyzedScript.add(KeyWord.EQUAL);
                scriptLines.add(currentLineNumber);
                return true;
            case "!=":
                analyzedScript.add(KeyWord.NOT_EQUAL);
                scriptLines.add(currentLineNumber);
                return true;
            case "*":
                analyzedScript.add(KeyWord.MULTIPLY);
                scriptLines.add(currentLineNumber);
                return true;
            case "/":
                analyzedScript.add(KeyWord.DIVIDE);
                scriptLines.add(currentLineNumber);
                return true;
            case "^":
                analyzedScript.add(KeyWord.EXPONENTIAL);
                scriptLines.add(currentLineNumber);
                return true;
            case "+":
                analyzedScript.add(KeyWord.ADD);
                scriptLines.add(currentLineNumber);
                return true;
            case "-":
                analyzedScript.add(KeyWord.SUBTRACT);
                scriptLines.add(currentLineNumber);
                return true;
            case "\\":
                analyzedScript.add(KeyWord.INVERT_DIVIDE);
                scriptLines.add(currentLineNumber);
                return true;
            case "%":
                analyzedScript.add(KeyWord.MOD);
                scriptLines.add(currentLineNumber);
                return true;
            case "&&":
                analyzedScript.add(KeyWord.AND);
                scriptLines.add(currentLineNumber);
                return true;
        }
        return false;
    }

    // Return True if it can create a valid Function Keyword from given Input
    public boolean createFunctionToken(String input) {
        switch(input.toUpperCase()) {
            case "IF":
                analyzedScript.add(KeyWord.IF);
                scriptLines.add(currentLineNumber);
                return true;
            case "THEN":
                analyzedScript.add(KeyWord.THEN);
                scriptLines.add(currentLineNumber);
                return true;
            case "ELSE":
                analyzedScript.add(KeyWord.ELSE);
                scriptLines.add(currentLineNumber);
                return true;
            case "END":
                analyzedScript.add(KeyWord.END);
                scriptLines.add(currentLineNumber);
                return true;
            case "WHILE":
                analyzedScript.add(KeyWord.WHILE);
                scriptLines.add(currentLineNumber);
                return true;
                /*
            case "DO":
                analyzedScript.add(KeyWord.DO);
                scriptLines.add(currentLineNumber);
                return true;
            case "REPEAT":
                analyzedScript.add(KeyWord.REPEAT);
                scriptLines.add(currentLineNumber);
                return true;
            case "UNTIL":
                analyzedScript.add(KeyWord.UNTIL);
                scriptLines.add(currentLineNumber);
                return true;
                */
            case "PRINT":
                analyzedScript.add(KeyWord.PRINT);
                scriptLines.add(currentLineNumber);
                return true;
            case "=":
                analyzedScript.add(KeyWord.ASSIGN);
                scriptLines.add(currentLineNumber);
                return true;
            case "(":
                analyzedScript.add(KeyWord.LEFT_PARENTHESIS);
                scriptLines.add(currentLineNumber);
                return true;
            case ")":
                analyzedScript.add(KeyWord.RIGHT_PARENTHESIS);
                scriptLines.add(currentLineNumber);
                return true;
            case "FOR":
                analyzedScript.add(KeyWord.FOR);
                scriptLines.add(currentLineNumber);
                return true;
                /*
            case "FOR EACH":
                analyzedScript.add(KeyWord.FOR_EACH);
                scriptLines.add(currentLineNumber);
                */
            case ":":
                analyzedScript.add(KeyWord.COLLEN);
                scriptLines.add(currentLineNumber);
                return true;


        }
        return false;
    }


    // Return True if it can create a valid VariableType Keyword from given Input
    public boolean createIdentifierToken(String input) {
        if(input.length() > 1) {
            System.out.println("Warning at line " + currentLineNumber + " Valid identifiers can't be greater than 1 character long");
        }
        // If the Variable was already declared, search for it and add it
        for (Identifier identifier : identifiers) {
            if (identifier.getIdentifier().equals(input)) {
                Identifier newIdentifer = new Identifier(KeyWord.VariableType.TYPELESS,identifier.getIdentifier());
                analyzedScript.add(newIdentifer);
                scriptLines.add(currentLineNumber);
                return true;
            }
        }
        // This script checks, since its not already an identifier we know if the previous
        // Keyword entered was a Variable Type, which it should be since int a = 1, we are on a so the previous
        // Keyword should be int, we check that here then create a new identifier and add it to our
        // AnalyzedScript and identifiers list

        Identifier identifier = new Identifier(KeyWord.VariableType.TYPELESS, input.replace("=", ""));
        identifiers.add(identifier);
        analyzedScript.add(identifier);
        scriptLines.add(currentLineNumber);

        return false;
    }




    // Return True if it can create a valid Value from given Input
    public boolean createValueToken(String input) {

        // Check if the input has a " on each side of the input string, representing a String
        if (input.charAt(0) == '"' && input.length() > 1 && input.charAt(input.length() - 1) == '"') {
            analyzedScript.add(new Value<>(KeyWord.VariableType.STRING, input.replace("\"", "")));
            scriptLines.add(currentLineNumber);
            return true;
        } else {
            // If we know its not a string, lets see if it contains numbers, and if it is a number input
            if (Character.isDigit(input.charAt(0))) { // Check if Number
                analyzedScript.add(new Value<>(KeyWord.VariableType.NUMBER, Integer.valueOf(input)));
                scriptLines.add(currentLineNumber);
                return true;
            } else {
                if (input.toUpperCase().equals("TRUE") || input.toUpperCase().equals("FALSE")) { // Check if Boolean
                    analyzedScript.add(new Value<>(KeyWord.VariableType.BOOLEAN, input.toUpperCase().equals("TRUE")));
                    scriptLines.add(currentLineNumber);
                    return true;
                }
            }
        }

        return false;

    }








    public ArrayList<Object> getAnalyzedScript() {
        return analyzedScript;
    }

    public ArrayList<Integer> getScriptLines() {
        return scriptLines;
    }

    public ArrayList<Identifier> getIdentifiers() {
        return identifiers;
    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }
}
