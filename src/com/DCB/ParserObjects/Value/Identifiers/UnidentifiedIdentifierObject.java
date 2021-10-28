package com.DCB.ParserObjects.Value.Identifiers;

import com.DCB.LexicalObjects.Identifier;
import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;

public class UnidentifiedIdentifierObject extends IdentifierCoupling {

    public UnidentifiedIdentifierObject(Identifier identifier) {
        super(CoupleObjectType.UNDECLARED_IDENTIFIER, identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public boolean hasReturnType() {
        return true;
    }

    @Override
    public KeyWord.VariableType getReturnType() {
        return KeyWord.VariableType.TYPELESS;
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + identifier + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        return "<arithmetic_expression> -> literal_undeclared\n";
    }
}
