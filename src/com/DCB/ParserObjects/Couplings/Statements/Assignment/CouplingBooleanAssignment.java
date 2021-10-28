package com.DCB.ParserObjects.Couplings.Statements.Assignment;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Value.BooleanValueObject;
import com.DCB.ParserObjects.Value.Identifiers.BooleanIdentifierObject;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;
import com.DCB.ParserObjects.Value.Wrapper.BooleanValueWrapper;
import com.DCB.ParserObjects.Value.Wrapper.IntValueWrapper;

import java.util.ArrayList;

public class CouplingBooleanAssignment extends CouplingAssignment implements BooleanValueObject {
    private final BooleanIdentifierObject booleanIdentifierObject;
    private final BooleanValueObject booleanValueObject;

    public CouplingBooleanAssignment(BooleanIdentifierObject booleanIdentifierObject, BooleanValueObject booleanValueObject) {
        super(CoupleObjectType.ASSIGN_BOOLEAN);
        this.booleanIdentifierObject = booleanIdentifierObject;
        this.booleanValueObject = booleanValueObject;
    }

    @Override
    public boolean hasReturnType() {
        return false;
    }

    @Override
    public KeyWord.VariableType getReturnType() {
        return null;
    }

    @Override
    public boolean getValue() {
        return booleanValueObject.getValue();
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | ID:" + booleanIdentifierObject.getStringIdentifier() + " Value:" +  booleanValueObject.getStringIdentifier() + " ]";
    }

    public BooleanIdentifierObject getBooleanIdentifierObject() {
        return booleanIdentifierObject;
    }

    //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        String grammer = "";
        if (!isLateStatement()) {
            grammer += "\n<block> -> <statement> <block> \n";
        } else {
            grammer += "\n<block> -> <statement> \n";
        }

        grammer += "<statement> -> <assignment_statement> \n" +
                "<assignment_statement> -> id assignment_operator <arithmetic_expression> \n" +
                booleanValueObject.getParsedGrammar();
        return grammer;
    }


    @Override
    public void executeStatement() {
        //System.out.println("DEBUG Assignment: Setting value of " + booleanIdentifierObject.getStringIdentifier() + " from " + booleanIdentifierObject.getValue() + " to " + booleanValueObject.getValue());
        // So what this does, is force the Identifier to use a final number as its value, or else if its assigned (itself + 1) it might become recursive
        booleanIdentifierObject.setBooleanValueObject(new BooleanValueWrapper(new Value(KeyWord.VariableType.BOOLEAN,booleanValueObject.getValue())));
        //System.out.println("DEBUG Assignment: Value is now " +  booleanIdentifierObject.getValue());
    }
}