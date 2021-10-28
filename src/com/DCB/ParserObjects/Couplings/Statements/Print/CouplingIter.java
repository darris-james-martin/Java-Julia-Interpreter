package com.DCB.ParserObjects.Couplings.Statements.Print;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Couplings.Statements.Assignment.CouplingIntAssignment;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;
import com.DCB.ParserObjects.Value.IntValueObject;

import java.util.ArrayList;

/**
 * Used to represent the For loop Iter object
 */
public class CouplingIter extends CouplingStatement {
    private final CouplingIntAssignment couplingIntAssignment;
    private final IntValueObject intValueObject;

    public CouplingIter(CouplingIntAssignment couplingIntAssignment, IntValueObject intValueObject) {
        super(CoupleObjectType.ITER);
        this.couplingIntAssignment = couplingIntAssignment;
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
    public String getStringIdentifier() {
        return "[" + coupleObjectType + " | " + couplingIntAssignment.getStringIdentifier() +  " | " + intValueObject.getStringIdentifier() + " ]";
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
    	String grammer = "";
        grammer += "<iter> -> <arithmetic_expression> : <arithmetic_expression>\n"
                + couplingIntAssignment.getParsedGrammar()
                + intValueObject.getParsedGrammar();
        return grammer;
    }

    public CouplingIntAssignment getCouplingIntAssignment() {
        return couplingIntAssignment;
    }

    public IntValueObject getIntValueObject() {
        return intValueObject;
    }

    @Override
    public void executeStatement() {
        // Nothing to write here! This statement does nothing!
    }
}
