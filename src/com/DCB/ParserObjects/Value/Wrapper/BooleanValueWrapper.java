package com.DCB.ParserObjects.Value.Wrapper;


import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.BooleanValueObject;

/**
 * Coupling that supports Value objects, and properly encapsulates them
 */
public class BooleanValueWrapper extends CouplingObject implements BooleanValueObject {
    private final Value value;
    public BooleanValueWrapper(Value value) {
        super(CoupleObjectType.BOOLEAN_VALUE);
        this.value = value;
    }


    @Override
    public boolean getValue() {
        return (boolean) value.getValue();
    }

    @Override
    public boolean hasReturnType() {
        return true;
    }

    @Override
    public KeyWord.VariableType getReturnType() {
        return KeyWord.VariableType.NUMBER;
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + value + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        return "<arithmetic_expression> -> literal_boolean\n";
    }
}
