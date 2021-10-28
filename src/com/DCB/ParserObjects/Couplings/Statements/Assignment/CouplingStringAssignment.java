package com.DCB.ParserObjects.Couplings.Statements.Assignment;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;
import com.DCB.ParserObjects.Value.Identifiers.StringIdentifierObject;
import com.DCB.ParserObjects.Value.Wrapper.IntValueWrapper;
import com.DCB.ParserObjects.Value.Wrapper.StringValueWrapper;
import com.DCB.ParserObjects.Value.StringValueObject;

import java.util.ArrayList;

public class CouplingStringAssignment extends CouplingStatement implements StringValueObject {
    private final StringIdentifierObject stringIdentifierObject;
    private final StringValueObject stringValueObject;

    public CouplingStringAssignment(StringIdentifierObject stringIdentifierObject, StringValueObject stringValueObject) {
        super(CoupleObjectType.ASSIGN_STRING);
        this.stringIdentifierObject = stringIdentifierObject;
        this.stringValueObject = stringValueObject;
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
    public String getValue() {
        return stringValueObject.getValue();
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | ID:" + stringIdentifierObject.getStringIdentifier() + " Value:" +  stringValueObject.getStringIdentifier() + " ]";
    }

    public StringIdentifierObject getStringIdentifierObject() {
        return stringIdentifierObject;
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
                stringValueObject.getParsedGrammar();
        return grammer;
    }


    @Override
    public void executeStatement() {
    	//System.out.println("DEBUG Assignment: Setting value of " + stringIdentifierObject.getStringIdentifier() + " from " + stringIdentifierObject.getValue() + " to " + stringIdentifierObject.getValue());
        // So what this does, is force the Identifier to use a final number as its value, or else if its assigned (itself + 1) it might become recursive
    	stringIdentifierObject.setStringValueObject(new StringValueWrapper(new Value(KeyWord.VariableType.STRING,stringValueObject.getValue())));
        //System.out.println("DEBUG Assignment: Value is now " +  stringIdentifierObject.getValue());
    }
}