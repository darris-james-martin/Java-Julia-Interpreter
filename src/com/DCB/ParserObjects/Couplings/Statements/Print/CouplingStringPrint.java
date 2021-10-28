package com.DCB.ParserObjects.Couplings.Statements.Print;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;
import com.DCB.ParserObjects.Value.StringValueObject;

import java.util.ArrayList;

public class CouplingStringPrint extends CouplingStatement implements StringValueObject {
    private final StringValueObject stringValueObject;

    public CouplingStringPrint(StringValueObject stringValueObject) {
        super(CoupleObjectType.PRINT_STRING);
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
        return null;
    }

    @Override
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | ID:" + stringValueObject.getStringIdentifier() + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
        String grammer = "";
        if(!isLateStatement()) {
            grammer += "\n<block> -> <statement> <block> \n";
        } else {
            grammer += "\n<block> -> <statement> \n";
        }

        grammer += "<statement> -> <print_statement> \n" 
        		+ "<print_statement> -> print (<arithmetic_expression>) \n"
                + stringValueObject.getParsedGrammar();
        return grammer;
    }


    @Override
    public void executeStatement() {
    	System.out.println("PRINT: " + stringValueObject.getValue());
    }
}
