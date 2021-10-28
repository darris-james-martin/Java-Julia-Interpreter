package com.DCB.ParserObjects.Value;


/**
 * Represents anything that can boil down to a String value
 */
public interface StringValueObject {
    String getValue();
    String getStringIdentifier();
    String getParsedGrammar();
}
