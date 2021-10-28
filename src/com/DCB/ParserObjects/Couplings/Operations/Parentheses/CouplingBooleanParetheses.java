package com.DCB.ParserObjects.Couplings.Operations.Parentheses;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.BooleanValueObject;

/**
 * Coupling that supports the Boolean Parentheses operation
 */
public class CouplingBooleanParetheses extends CouplingObject implements BooleanValueObject {
    private final BooleanValueObject booleanValueObject;

    public CouplingBooleanParetheses(BooleanValueObject booleanValueObject) {
        super(CoupleObjectType.BOOLEAN_PARENTHESES);
        this.booleanValueObject = booleanValueObject;
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
    public boolean getValue() {
        return booleanValueObject.getValue();
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + booleanValueObject.getStringIdentifier() + "]";
    }

    @Override
    public String getParsedGrammar() {
        return booleanValueObject.getParsedGrammar();
    }
}

