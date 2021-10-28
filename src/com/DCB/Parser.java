package com.DCB;

import com.DCB.ParserObjects.CouplingObjectFactory;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;

import java.util.ArrayList;

public class Parser {
    private final ArrayList<Object> analyzedScript;
    private final ArrayList<Integer> lineNumbers;
    private final CouplingObjectFactory couplingObjectFactory;
    private final int currentLineNumber;

    public Parser(ArrayList<Object> analyzedScript,ArrayList<Integer> lineNumbers, int linesRead) {
        this.analyzedScript = analyzedScript;
        this.lineNumbers = lineNumbers;
        this.currentLineNumber = linesRead;
        this.couplingObjectFactory = new CouplingObjectFactory(analyzedScript,lineNumbers,currentLineNumber);

        couplingObjectFactory.createAllCouplings();
        if(analyzedScript.get(analyzedScript.size()-1) instanceof CouplingStatement) {
            ((CouplingStatement) analyzedScript.get(analyzedScript.size()-1)).setLateStatement();
        }
    }

    public ArrayList<CouplingStatement> getParsedStatements() {
        return couplingObjectFactory.getParsedStatements();
    }

    public ArrayList<IdentifierCoupling> getIdentifers() {
        return couplingObjectFactory.getIdentifiers();
    }

    public ArrayList<Integer> getLineNumbers() {
        return lineNumbers;
    }

    public int getLinesRead() {
        return currentLineNumber;
    }
}
