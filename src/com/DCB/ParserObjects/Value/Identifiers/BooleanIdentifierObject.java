package com.DCB.ParserObjects.Value.Identifiers;

import com.DCB.LexicalObjects.Identifier;
import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.BooleanValueObject;

/**
 * Coupling that supports identifier objects, and keep tracks of their values
 */
public class BooleanIdentifierObject extends IdentifierCoupling implements BooleanValueObject {
    private BooleanValueObject booleanValueObject;
    public BooleanIdentifierObject(Identifier identifier, BooleanValueObject booleanValueObject) {
        super(CoupleObjectType.BOOLEAN_IDENTIFIER, identifier);
        this.booleanValueObject = booleanValueObject;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public BooleanValueObject getBooleanValueObject() {
        return booleanValueObject;
    }

    public void setBooleanValueObject(BooleanValueObject booleanValueObject) {
        this.booleanValueObject = booleanValueObject;
    }

    @Override
    public boolean hasReturnType() {
        return true;
    }

    @Override
    public KeyWord.VariableType getReturnType() {
        return KeyWord.VariableType.BOOLEAN;
    }

    @Override
    public boolean getValue() {
        return booleanValueObject.getValue();
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
