package com.DCB.ParserObjects.Couplings.ControlStatements.Conditionals;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Couplings.ControlStatements.CouplingControlStatement;
import com.DCB.ParserObjects.Couplings.Statements.Print.CouplingIter;
import com.DCB.ParserObjects.Value.Wrapper.IntValueWrapper;

import java.util.ArrayList;

/**
 * Control Statement encapsulation coupling for the For Control Statement
 */
public class CouplingForStatement extends CouplingControlStatement {
    private final CouplingIter couplingIter;
    private final ArrayList<CouplingStatement> containedStatements;

    public CouplingForStatement(CouplingIter couplingIter, ArrayList<CouplingStatement> containedStatements) {
        super(CoupleObjectType.FOR);
        this.couplingIter = couplingIter;
        this.containedStatements = containedStatements;
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
        String identifier = "";
        identifier += "[" + coupleObjectType + " \n";
        identifier += " Iter Statement: " + couplingIter.getStringIdentifier() + " \n";
        identifier += " Contained Statements\n";
        for(int i = 0; i < containedStatements.size(); i++) {
            identifier += "~     " + containedStatements.get(i).getStringIdentifier() + "\n";
        }
        identifier += "]\n";
        return identifier;
    }

    //The Parsed Grammar below will check if it is the last block in the array. If so It will not request another block. 
    //After it will lay out the grammar as requested. It does this by grabbing that coupling and seeing what it contains. 
    //If anything extra is added, that piece will have a note.
    @Override
    public String getParsedGrammar() {
    	
    	//The For statement has a code block. This loop will pull everything from that block into the following string.
    	//It will then be placed at the end of the for grammar block. 
    	String containGrammer = "\n";
    	for (int i = 0; i<containedStatements.size();i++)
    	{
    		containGrammer += containedStatements.get(i).getParsedGrammar();
    	}
    	
    	String grammer = "";
        if(!isLateStatement()) {
            grammer += "\n<block> -> <statement> <block> \n";
        } else {
            grammer += "\n<block> -> <statement> \n";
        }

        grammer += "<statement> -> <for_statement>\r\n"
        		+ "<for_statement> -> for id = <iter> <block> end\n"
                + couplingIter.getParsedGrammar()
                + containGrammer;
        return grammer;
    }

    public CouplingIter getCouplingIntAssignment() {
        return couplingIter;
    }

    public ArrayList<CouplingStatement> getContainedStatements() {
        return containedStatements;
    }

    @Override
    public void setLateStatement() {
        containedStatements.get(containedStatements.size()-1).setLateStatement();
    }


    @Override
    public void executeStatement() {
        // We want to comment this out once we are done
        //System.out.println("DEBUG ForStatement: Starting for loop from " + couplingIter.getCouplingIntAssignment().getValue() + " to " + couplingIter.getIntValueObject().getValue());

        // So the for loop stars from CouplingIter's current assignment value, to the Value object's value. The second value can technically update so we need to be careful
        for(int i = couplingIter.getCouplingIntAssignment().getValue(); i <= couplingIter.getIntValueObject().getValue(); i++) {
            //System.out.println("DEBUG ForStatement: On loop number " + i);
            // We update our int assignment in the iter, this is a real variable whose value can update, but incrementing its value will not change the i normally
            couplingIter.getCouplingIntAssignment().getIntIdentifierObject().setIntValueObject(new IntValueWrapper(new Value(KeyWord.VariableType.NUMBER,i)));
            for(int x = 0; x < containedStatements.size(); x++) {
                containedStatements.get(x).executeStatement();
            }
        }
        //System.out.println("DEBUG ForStatement: Exiting Loop");

    }
}

