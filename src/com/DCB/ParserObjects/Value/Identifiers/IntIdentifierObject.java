package com.DCB.ParserObjects.Value.Identifiers;

import com.DCB.LexicalObjects.Identifier;
import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.IntValueObject;

/**
 * Coupling that supports identifier objects, and keep tracks of their values
 */
public class IntIdentifierObject  extends IdentifierCoupling implements IntValueObject {
    private IntValueObject intValueObject;
    public IntIdentifierObject(Identifier identifier, IntValueObject intValueObject) {
        super(CoupleObjectType.INT_IDENTIFIER, identifier);
        this.intValueObject = intValueObject;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public IntValueObject getIntValueObject() {
        return intValueObject;
    }

    public void setIntValueObject(IntValueObject intValueObject) {
        this.intValueObject = intValueObject;
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
    public int getValue() {
        return intValueObject.getValue();
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + identifier + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        return "<arithmetic_expression> -> id\n";
    }
}
