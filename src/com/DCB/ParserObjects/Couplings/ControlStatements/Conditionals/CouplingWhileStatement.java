package com.DCB.ParserObjects.Couplings.ControlStatements.Conditionals;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Couplings.ControlStatements.CouplingControlStatement;
import com.DCB.ParserObjects.Value.BooleanValueObject;
import com.DCB.ParserObjects.Value.Wrapper.IntValueWrapper;

import java.util.ArrayList;

/**
 * Control Statement encapsulation coupling for the While Control Statement
 */
public class CouplingWhileStatement extends CouplingControlStatement {
    private final BooleanValueObject booleanValueObject;
    private final ArrayList<CouplingStatement> containedStatements;

    public CouplingWhileStatement(BooleanValueObject booleanValueObject, ArrayList<CouplingStatement> containedStatements) {
        super(CoupleObjectType.WHILE);
        this.booleanValueObject = booleanValueObject;
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
        identifier += " Boolean Value: " + booleanValueObject.getStringIdentifier() + " \n";
        identifier += " Contained Statements\n";
        for(int i = 0; i < containedStatements.size(); i++) {
            identifier += "~    " + containedStatements.get(i).getStringIdentifier() + "\n";
        }
        identifier += "]\n";
        return identifier;
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
    	
    	//This will loop through the while block and pull the parsed grammar to be placed below.
    	String containGrammer = "\n";
    	for (int i = 0; i<containedStatements.size();i++)
    	{
    		containGrammer += containedStatements.get(i).getParsedGrammar();
    	}
    	
    	String grammer = "";
        if(!isLateStatement()) {
            grammer += "\n<block> -> <statement> <block>\n";
        } else {
            grammer += "\n<block> -> <statement>\n";
        }

        grammer += "<statement> -> <while_statement>\r\n" + 
        		"<while_statement> -> while <boolean_expression> <block> end\r\n"
                + booleanValueObject.getParsedGrammar()
                + containGrammer;
        return grammer;
    }

    public BooleanValueObject getBooleanValueObject() {
        return booleanValueObject;
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
    	// giving an i here so we can count while (ha get it) we move though the loop.
        int i = 0;
        // So the for loop stars from CouplingIter's current assignment value, to the Value object's value. The second value can tecnically update so we need to be careful
        while(booleanValueObject.getValue()) {
        	//System.out.println("DEBUG WhileStatement: On loop number " + i);
        	
        	for(int x = 0; x < containedStatements.size(); x++) {
                containedStatements.get(x).executeStatement();
            }
        	i++;
        }
        //System.out.println("DEBUG WhileStatement: Exiting Loop");
        // TLDR: Do an actual While Loop, with the boolean inside being the booleanValueObject.getValue, and have the inside of the loop run all the statements inside the While aka containedStatements
    }
}


