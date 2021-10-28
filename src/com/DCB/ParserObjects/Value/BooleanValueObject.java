package com.DCB.ParserObjects.Value;

import com.DCB.LexicalObjects.KeyWord;

/**
 * Represents anything that can boil down to a Boolean value
 */
public interface BooleanValueObject {
    boolean getValue();
    String getStringIdentifier();
    String getParsedGrammar();

}
