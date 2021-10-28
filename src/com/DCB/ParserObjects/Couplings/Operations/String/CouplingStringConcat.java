package com.DCB.ParserObjects.Couplings.Operations.String;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.StringValueObject;


/**
 * Coupling that supports the String concat operation
 */
public class CouplingStringConcat extends CouplingObject implements StringValueObject {
    private final StringValueObject string1;
    private final StringValueObject string2;

    public CouplingStringConcat(StringValueObject string1, StringValueObject string2) {
        super(CoupleObjectType.STRING_CONCAT);
        this.string1 = string1;
        this.string2 = string2;
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
        return "[" + coupleObjectType + " | " + string1.getStringIdentifier() + " | " + string2.getStringIdentifier() + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        return "<arithmetic_expression> -> <binary_expression> \n"
                + "<binary_expression> -> <arithmetic_op> <arithmetic_expression> <arithmetic_expression> \n"
                + "<arithmetic_op> -> add_operator\n"
                + string1.getParsedGrammar() + string2.getParsedGrammar();
    }


    @Override
    public String getValue() {
        return string1.getValue() + string2.getValue();
    }


}
