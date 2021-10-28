package com.DCB.ParserObjects.Value;


/**
 * Represents anything that can boil down to a Integer value
 */
public interface IntValueObject {
    int getValue();
    String getStringIdentifier();
    String getParsedGrammar();
}
