package com.DCB.ParserObjects.Couplings.Operations.Boolean;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.BooleanValueObject;
import com.DCB.ParserObjects.Value.IntValueObject;


/**
 * Coupling that supports the Boolean EqualLessThan operation
 */
public class CouplingBooleanEqualLessThan  extends CouplingObject implements BooleanValueObject {
    private final IntValueObject number1;
    private final IntValueObject number2;

    public CouplingBooleanEqualLessThan(IntValueObject number1, IntValueObject number2) {
        super(CoupleObjectType.BOOLEAN_EQUAL_LESS_THAN);
        this.number1 = number1;
        this.number2 = number2;
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
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + number1.getStringIdentifier() + " | " + number2.getStringIdentifier() + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        return "<boolean_expression> -> <relative_op> <arithmetic_expression> <arithmetic_expression>  \n"
        		+ "<relative_op> -> le_operator\n"
                + number1.getParsedGrammar()
                + number2.getParsedGrammar();
    }


    @Override
    public boolean getValue() {
        return number1.getValue() <= number2.getValue();
    }


}

