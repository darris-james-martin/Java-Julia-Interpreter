package com.DCB.ParserObjects;

import com.DCB.LexicalObjects.KeyWord;


/**
 * This is a Coupling Object that is used to encapsulate all Operations and Statements that allows for proper parsing via
 * the grammer encapsulation assignment method
 */
public abstract class CouplingObject {
    protected final CoupleObjectType coupleObjectType;
    public CouplingObject(CoupleObjectType coupleObjectType) {
        this.coupleObjectType = coupleObjectType;
    }

    public CoupleObjectType getCoupleObjectType() {
        return coupleObjectType;
    }

    public abstract boolean hasReturnType();
    public abstract KeyWord.VariableType getReturnType();

    public abstract String getStringIdentifier();
    public abstract String getParsedGrammar();


    public enum CoupleObjectType {
        // Completed
        BOOLEAN_VALUE,
        INT_VALUE,
        STRING_VALUE,

        UNDECLARED_IDENTIFIER,
        BOOLEAN_IDENTIFIER,
        INT_IDENTIFIER,
        STRING_IDENTIFIER,

        BOOLEAN_PARENTHESES,
        INT_PARENTHESES,
        STRING_PARENTHESES,
        // No worry

        STRING_CONCAT,
        BOOLEAN_LESS_THAN,
        BOOLEAN_GREATER_THAN,
        BOOLEAN_EQUAL_LESS_THAN,
        BOOLEAN_EQUAL_GREATER_THAN,
        BOOLEAN_AND,
        BOOLEAN_EQUAl,
        BOOLEAN_NOT_EQUAl,
        NUMBER_ADD,
        NUMBER_SUBTRACT,
        NUMBER_MULTIPLY,
        NUMBER_DIVIDE,
        NUMBER_INVERT_DIVIDE,
        NUMBER_EXPONTENTIAL,
        NUMBER_MOD,
        NUMBER_IDENTITY, // NO
        NUMBER_INVERT_IDENTITY, // NO


        IF,
        WHILE,
        FOR,
        //DO,
        //REPEAT,
        PRINT_INT,
        PRINT_STRING,
        PRINT_BOOLEAN,

        ASSIGN_INT,
        ASSIGN_STRING,
        ASSIGN_BOOLEAN,

        ITER,

        //FOR_EACH,
    }
}
