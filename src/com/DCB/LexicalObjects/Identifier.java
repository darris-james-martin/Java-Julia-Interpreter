package com.DCB.LexicalObjects;

/**
 * Variable Names, "Bob" ect
 */

/*
 * Class:       CS 4308 Section 2
 * Term:        Fall 2019
 * Name:        Robert, Chris, James
 * Instructor:   Deepa Muralidhar
 * Project:  Deliverable 1 Scanner - Java
 */

public class Identifier {
    private KeyWord.VariableType variableType;
    private final String identifier;

    public Identifier(KeyWord.VariableType variableType, String identifier) {
        this.variableType = variableType;
        this.identifier = identifier;
    }

    public void setVariableType(KeyWord.VariableType variableType) {
        this.variableType = variableType;
    }

    public KeyWord.VariableType getVariableType() {
        return variableType;
    }

    public String getIdentifier() {
        return identifier;
    }


    @Override
    public String toString() {
        return "[Lexime Identifier " + " " + variableType + " " + identifier + " "  + "]";


    }
}
