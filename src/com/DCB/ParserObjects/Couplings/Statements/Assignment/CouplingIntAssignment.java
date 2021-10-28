package com.DCB.ParserObjects.Couplings.Statements.Assignment;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;
import com.DCB.ParserObjects.Value.Identifiers.IntIdentifierObject;
import com.DCB.ParserObjects.Value.IntValueObject;
import com.DCB.ParserObjects.Value.Wrapper.IntValueWrapper;

import java.util.ArrayList;

public class CouplingIntAssignment extends CouplingStatement implements IntValueObject {
    private final IntIdentifierObject intIdentifierObject;
    private final IntValueObject intValueObject;

    public CouplingIntAssignment(IntIdentifierObject intIdentifierObject, IntValueObject intValueObject) {
        super(CoupleObjectType.ASSIGN_INT);
        this.intIdentifierObject = intIdentifierObject;
        this.intValueObject = intValueObject;
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
    public int getValue() {
        return intValueObject.getValue();
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | ID:" + intIdentifierObject.getStringIdentifier() + " Value:" +  intValueObject.getStringIdentifier() + " ]";
    }

    public IntIdentifierObject getIntIdentifierObject() {
        return intIdentifierObject;
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
                intValueObject.getParsedGrammar();
        return grammer;
    }

    public IntValueObject getIntValueObject() {
        return intValueObject;
    }


    @Override
    public void executeStatement() {
        //System.out.println("DEBUG Assignment: Setting value of " + intIdentifierObject.getStringIdentifier() + " from " + intIdentifierObject.getValue() + " to " + intValueObject.getValue());
        // So what this does, is force the Identifier to use a final number as its value, or else if its assigned (itself + 1) it might become recursive
        intIdentifierObject.setIntValueObject(new IntValueWrapper(new Value(KeyWord.VariableType.NUMBER,intValueObject.getValue())));
        //System.out.println("DEBUG Assignment: Value is now " +  intIdentifierObject.getValue());

    }
}