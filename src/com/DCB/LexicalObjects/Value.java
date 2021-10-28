package com.DCB.LexicalObjects;

/**
 * Variable Values
 * @param <T>
 */

/*
 * Class:       CS 4308 Section 2
 * Term:        Fall 2019
 * Name:        Robert, Chris, James
 * Instructor:   Deepa Muralidhar
 * Project:  Deliverable 1 Scanner - Java
 */

public class Value<T> {
    private final KeyWord.VariableType variableType;
    private final T value;
    public Value(KeyWord.VariableType variableType, T value) {
        this.variableType = variableType;
        this.value = value;
    }

    public KeyWord.VariableType getVariableType() {
        return variableType;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[Lexime Value " + variableType + " " + value + "]";
    }
}
