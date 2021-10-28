package com.DCB.ParserObjects.Value.Wrapper;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.StringValueObject;

/**
 * Coupling that supports Value objects, and properly encapsulates them
 */
public class StringValueWrapper extends CouplingObject implements StringValueObject {
    private final Value value;

    public StringValueWrapper(Value value) {
        super(CoupleObjectType.STRING_VALUE);
        this.value = value;
    }


    @Override
    public String getValue() {
        return (String) value.getValue();
    }

    @Override
    public boolean hasReturnType() {
        return true;
    }

    @Override
    public KeyWord.VariableType getReturnType() {
        return KeyWord.VariableType.STRING;
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + value + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        return "<arithmetic_expression> -> literal_string\n";
    }
}
