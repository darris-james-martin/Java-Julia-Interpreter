package com.DCB.ParserObjects.Couplings.Operations.Parentheses;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.StringValueObject;


/**
 * Coupling that supports the String Parentheses Operation
 */
public class CouplingStringParentheses extends CouplingObject implements StringValueObject {
    private final StringValueObject stringValueObject;

    public CouplingStringParentheses(StringValueObject stringValueObject) {
        super(CoupleObjectType.STRING_PARENTHESES);
        this.stringValueObject = stringValueObject;
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
    public String getValue() {
        return stringValueObject.getValue();
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + stringValueObject.getStringIdentifier() + "]";
    }

    @Override
    public String getParsedGrammar() {
        return stringValueObject.getParsedGrammar();
    }
}
