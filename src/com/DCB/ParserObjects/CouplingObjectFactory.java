package com.DCB.ParserObjects;

import com.DCB.LexicalObjects.Identifier;
import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.Couplings.ControlStatements.Conditionals.CouplingForStatement;
import com.DCB.ParserObjects.Couplings.ControlStatements.Conditionals.CouplingIfStatement;
import com.DCB.ParserObjects.Couplings.ControlStatements.Conditionals.CouplingWhileStatement;
import com.DCB.ParserObjects.Couplings.Operations.Boolean.*;
import com.DCB.ParserObjects.Couplings.Operations.Integer.*;
import com.DCB.ParserObjects.Couplings.Operations.Parentheses.CouplingBooleanParetheses;
import com.DCB.ParserObjects.Couplings.Operations.Parentheses.CouplingIntParentheses;
import com.DCB.ParserObjects.Couplings.Operations.Parentheses.CouplingStringParentheses;
import com.DCB.ParserObjects.Couplings.Operations.String.CouplingStringConcat;
import com.DCB.ParserObjects.Couplings.Statements.Assignment.CouplingBooleanAssignment;
import com.DCB.ParserObjects.Couplings.Statements.Assignment.CouplingIntAssignment;
import com.DCB.ParserObjects.Couplings.Statements.Assignment.CouplingStringAssignment;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Couplings.Statements.Print.CouplingIter;
import com.DCB.ParserObjects.Couplings.Statements.Print.CouplingBooleanPrint;
import com.DCB.ParserObjects.Couplings.Statements.Print.CouplingIntPrint;
import com.DCB.ParserObjects.Couplings.Statements.Print.CouplingStringPrint;
import com.DCB.ParserObjects.Value.*;
import com.DCB.ParserObjects.Value.Identifiers.*;
import com.DCB.ParserObjects.Value.Wrapper.BooleanValueWrapper;
import com.DCB.ParserObjects.Value.Wrapper.IntValueWrapper;
import com.DCB.ParserObjects.Value.Wrapper.StringValueWrapper;

import java.util.ArrayList;


/**
 * This is a Object that builds the Coupling required for the parser, it works by working with operations first, working its
 * way up into statements, then branching statements
 */
public class CouplingObjectFactory {
    private final KeyWord[] NUMBER_OPERATIONS = new KeyWord[]{KeyWord.LEFT_PARENTHESIS, KeyWord.EXPONENTIAL,KeyWord.MULTIPLY, KeyWord.DIVIDE,
            KeyWord.INVERT_DIVIDE, KeyWord.ADD, KeyWord.SUBTRACT, KeyWord.NUMBER_IDENTITY, KeyWord.NUMBER_INVERT, KeyWord.LESS_THAN, KeyWord.GREATER_THAN,KeyWord.EQUAL_LESS_THAN, KeyWord.EQUAL_GREATER_THAN,
            KeyWord.AND,KeyWord.EQUAL, KeyWord.NOT_EQUAL, KeyWord.ASSIGN, KeyWord.COLLEN
    };

    private KeyWord[] STATEMENTS = new KeyWord[]{KeyWord.PRINT
    };

    private KeyWord[] CONTROL_STATEMENTS = new KeyWord[]{KeyWord.IF, KeyWord.WHILE, //KeyWord.DO, KeyWord.REPEAT,
            KeyWord.FOR
    };

    private final ArrayList<Object> parsedScript;
    private final ArrayList<Integer> lineNumbers;
    private final int currentLineNumber;

    private final ArrayList<CouplingStatement> parsedStatements;
    private final ArrayList<IdentifierCoupling> identifiers;



    public CouplingObjectFactory(ArrayList<Object> parsedScript, ArrayList<Integer> lineNumbers, int currentLineNumber) {
        this.parsedScript = parsedScript;
        this.lineNumbers = lineNumbers;
        this.currentLineNumber = currentLineNumber;

        this.parsedStatements = new ArrayList<>();
        this.identifiers = new ArrayList<>();
    }



    // Do until you can't create any more value or identifier couplings
    // Do all Value Couplings ( Prioritize () * / + -
    // Do all Identifier Assignment/Reassignment Couplings   <= The reason we wait is some might be dependant like
    // <= The reason we wait is some might be dependant like     int a = 7 + 4     int b = 7 + a, if a isn't a value yet uh oh,
    // <= We also do all avalible to prevent premature value encapsulation from assignment    int a = 7 + 4 + 7  ===>>> (int a = (7+4) + 7 ( Oh no! )
    // If any work was done, go up to Do all Value Couplings

    // Do all other Statements except for   IF THEN    WHILE    FOR   REPEAT Ect

    // Then do all the Statement containers


    // So what if we looked ahead, and marked anything intertwine parenthesis as "Urgent Complete first",

    public void createAllCouplings() {
        // Check and removal all raw values and replace with other couplings
        for(int i = 0; i < parsedScript.size(); i++) {
            Object object = getObject(i);
            if (object instanceof Value) {
                Value value = (Value) object;
                switch (value.getVariableType()) {
                    case NUMBER:
                        replace(i,new IntValueWrapper(value));
                        break;
                    case STRING:
                        replace(i,new StringValueWrapper(value));
                        break;
                    case BOOLEAN:
                        replace(i,new BooleanValueWrapper(value));
                        break;
                }
            }
            // Identifiers should actually be filled in once their assignment statement has initialized, but for now this will cheat.
            // The values of these identifiers will be what they where when initialized, mind you that will be incorrect
            // As the assignment a = 5*4 will initalize a = 5, but this is only to maintain typing
            if (object instanceof Identifier) {
                Identifier identifier = ((Identifier) object);
                replace(i,new UnidentifiedIdentifierObject(identifier));
            }
        }



        // Builds all Operational couplings, in PDMOS order, and restart each time an operation is found, to preserve order
        boolean completedAllOperationalCouplings = false;
        while(!completedAllOperationalCouplings) {
            int numberOfOperationsCompleted = 0;

            for (KeyWord currentKeyword : NUMBER_OPERATIONS) {
                boolean completedKeywordCouplings = false;
                ArrayList<Integer> failedCouplings = new ArrayList<>();
                while(!completedKeywordCouplings) {
                    int nextPotentialCouplingLocation = -1;

                    for(int i = 0; i < parsedScript.size(); i++) {
                        Object object = getObject(i);
                        if(object instanceof KeyWord && object == currentKeyword && !failedCouplings.contains(i)) {
                            nextPotentialCouplingLocation = i;
                            break;
                        }
                    }
                    if(nextPotentialCouplingLocation != -1) {
                        KeyWord keyword = (KeyWord) getObject(nextPotentialCouplingLocation);
                        if(canCreateCoupling(keyword,nextPotentialCouplingLocation)) {
                            createCoupling(keyword,nextPotentialCouplingLocation);
                            numberOfOperationsCompleted++;
                            completedKeywordCouplings = true;
                        } else {
                            failedCouplings.add(nextPotentialCouplingLocation);
                        }
                    } else {
                        completedKeywordCouplings = true;
                    }
                }
            }
            if(numberOfOperationsCompleted == 0) {
                completedAllOperationalCouplings = true;
            }

        }
        for(int i = 0; i < parsedScript.size(); i++) {
            for(KeyWord keyWord: NUMBER_OPERATIONS) {
                if(getObject(i) == keyWord) {
                    System.out.println("ERROR!");
                    System.out.println("Line #" + (getObjectLineNumber(i)+1));
                    System.out.println("Token number " + (i+1));
                    switch(keyWord) {
                        case LEFT_PARENTHESIS:
                            if(!(getObject(i+1) instanceof IntValueObject) || !(getObject(i+1) instanceof StringValueObject) || !(getObject(i+1) instanceof BooleanValueObject)) {
                                System.out.println("Sample Example of the Token | ( id ) | ( literal_int ) | ( literal_boolean ) | ( literal_string )");
                                System.out.println("Expression causing the error: " + "( " + " <NonValueObject> " + " )");
                            }
                            if(!(getObject(i+2) instanceof KeyWord) || getObject(i+2) instanceof KeyWord && getObject(i+2) != KeyWord.RIGHT_PARENTHESIS) {
                                System.out.println("Sample Example of the Token | ( id ) | ( literal_int ) | ( literal_boolean ) | ( literal_string )");
                                System.out.println("Expression causing the error: " + "( " + " <Value> " + " <No Right Side Parentheses>");
                            }
                            break;
                        case ASSIGN:
                            if(!(getObject(i-1) instanceof Identifier)) {
                                System.out.println("Sample Example of the Token | <assignment_statement> -> id assignment_operator <arithmetic_expression>");
                                System.out.println("Expression causing the error: <assignment_statement> -> <No ID> assignment_operator <arithmetic_expression>");
                            }
                            if(!(getObject(i+1) instanceof IntValueObject) || !(getObject(i+1) instanceof StringValueObject) || !(getObject(i+1) instanceof BooleanValueObject)) {
                                System.out.println("Sample Example of the Token | <assignment_statement> -> id assignment_operator <arithmetic_expression>");
                                System.out.println("Expression causing the error: <assignment_statement> -> id assignment_operator <no arithmetic expression>");
                            }
                            break;
                        case COLLEN:
                            if(!(getObject(i-1) instanceof CouplingIntAssignment)) {
                                System.out.println("Sample Example of the Token | <iter> -> <arithmetic_expression> : <arithmetic_expression>");
                                System.out.println("Expression causing the error: <iter> -> <No Arithmetic expression> : <arithmetic_expression>");
                            }
                            if(!(getObject(i+1) instanceof IntValueObject) || !(getObject(i+1) instanceof StringValueObject) || !(getObject(i+1) instanceof BooleanValueObject)) {
                                System.out.println("Sample Example of the Token | <iter> -> <arithmetic_expression> : <arithmetic_expression>");
                                System.out.println("Expression causing the error: <iter> -> <arithmetic_expression> : <No Arithmetic expression> ");
                            }
                            break;
                        case EQUAL:
                        case NOT_EQUAL:
                            if(!(getObject(i+1) instanceof IntValueObject) || !(getObject(i+1) instanceof StringValueObject) || !(getObject(i+1) instanceof BooleanValueObject)) {
                                System.out.println("Sample Example of the Token | <binary_expression> -> <arithmetic_op> <arithmetic_expression> <arithmetic_expression>");
                                System.out.println("Expression causing the error: <binary_expression> -> <arithmetic_op> <non arithmetic_expression> <arithmetic_expression>");
                            }
                            if(!(getObject(i+2) instanceof IntValueObject) || !(getObject(i+2) instanceof StringValueObject) || !(getObject(i+2) instanceof BooleanValueObject)) {
                                System.out.println("Sample Example of the Token | <boolean_expression> -> <relative_op> <arithmetic_expression> <arithmetic_expression>");
                                System.out.println("Expression causing the error: <boolean_expression> -> <relative_op> <arithmetic_expression> <non arithmetic_expression>");
                            }
                            break;
                        case EXPONENTIAL:
                        case MULTIPLY:
                        case DIVIDE:
                        case INVERT_DIVIDE:
                        case ADD:
                        case SUBTRACT:
                            if(!(getObject(i+1) instanceof IntValueObject)) {
                                System.out.println("Sample Example of the Token | <boolean_expression> -> <relative_op> <arithmetic_expression> <arithmetic_expression>");
                                System.out.println("Expression causing the error: <boolean_expression> -> <relative_op> <non arithmetic_expression> <arithmetic_expression> ");
                            }
                            if(!(getObject(i+2) instanceof IntValueObject)) {
                                System.out.println("Sample Example of the Token | <boolean_expression> -> <relative_op> <arithmetic_expression> <arithmetic_expression>");
                                System.out.println("Expression causing the error: <boolean_expression> -> <relative_op> <arithmetic_expression> <non arithmetic_expression>");
                            }
                            break;
                        case LESS_THAN:
                        case GREATER_THAN:
                        case EQUAL_LESS_THAN:
                        case EQUAL_GREATER_THAN:
                        case AND:
                            if(!(getObject(i+1) instanceof BooleanValueObject)) {
                                System.out.println("Sample Example of the Token | <boolean_expression> -> <relative_op> <arithmetic_expression> <arithmetic_expression>\n");
                                System.out.println("Expression causing the error: <boolean_expression> -> <relative_op> <non arithmetic_expression> <arithmetic_expression>\n ");
                            }
                            if(!(getObject(i+2) instanceof BooleanValueObject)) {
                                System.out.println("Sample Example of the Token | <boolean_expression> -> <relative_op> <arithmetic_expression> <arithmetic_expression>\n");
                                System.out.println("Expression causing the error: <boolean_expression> -> <relative_op> <arithmetic_expression> <non arithmetic_expression>\n");
                            }
                            break;
                    }
                    System.exit(0);
                }
                if(getObject(i)  instanceof UnidentifiedIdentifierObject) {
                    System.out.println("ERROR!");
                    System.out.println("Line #" + (getObjectLineNumber(i)+1));
                    System.out.println("Token number " + (i+1));
                    System.out.println("Sample Example of the Token | <assignment_statement> -> id assignment_operator <arithmetic_expression>");
                    System.out.println("Expression causing the error: null - No Assignment_Statement found for this ID");
                    System.exit(0);
                }
            }
        }

        // Complete all statements top to bottom
        boolean completedAllStatementCouplings = false;
        while(!completedAllStatementCouplings) {
            int statementCompleteCount = 0;
            for(KeyWord currentKeyword: STATEMENTS) {
                int nextStatementCouplingLocation = -1;

                for(int i = 0; i < parsedScript.size(); i++) {
                    Object object = getObject(i);
                    if(object instanceof KeyWord && object == currentKeyword) {
                        nextStatementCouplingLocation = i;
                        break;
                    }
                }
                if(nextStatementCouplingLocation != -1) {
                    KeyWord keyword = (KeyWord) getObject(nextStatementCouplingLocation);
                    if(canCreateCoupling(keyword,nextStatementCouplingLocation)) {
                        createCoupling(keyword,nextStatementCouplingLocation);
                        statementCompleteCount++;
                    }
                }
            }
            if(statementCompleteCount == 0) {
                completedAllStatementCouplings = true;
            }
        }
        for(int i = 0; i < parsedScript.size(); i++) {
            for(KeyWord keyWord: STATEMENTS) {
                if(getObject(i) == keyWord) {
                    System.out.println("ERROR!");
                    System.out.println("Line #" + (getObjectLineNumber(i)+1));
                    System.out.println("Token number " + (i+1));
                    switch(keyWord) {
                        case PRINT:
                            System.out.println("Sample Example of the Token | print ( <arithmetic_expression> )");
                            System.out.println("Expression causing the error: print ( <non arithmetic_expression> )");
                            break;
                    }

                    System.exit(0);
                }
            }
        }

        // Complete all control statements
        boolean completedAllControlStatementCouplings = false;
        while(!completedAllControlStatementCouplings) {
            int numberOfOperationsCompleted = 0;

            for (KeyWord currentKeyword : CONTROL_STATEMENTS) {
                boolean completedControlStatementCoupling = false;
                ArrayList<Integer> failedCouplings = new ArrayList<>();
                while(!completedControlStatementCoupling) {
                    int nextPotentialCouplingLocation = -1;

                    for(int i = 0; i < parsedScript.size(); i++) {
                        Object object = getObject(i);
                        if(object instanceof KeyWord && object == currentKeyword && !failedCouplings.contains(i)) {
                            nextPotentialCouplingLocation = i;
                            break;
                        }
                    }
                    if(nextPotentialCouplingLocation != -1) {
                        KeyWord keyword = (KeyWord) getObject(nextPotentialCouplingLocation);
                        if(canCreateCoupling(keyword,nextPotentialCouplingLocation)) {
                            createCoupling(keyword,nextPotentialCouplingLocation);
                            numberOfOperationsCompleted++;
                            completedControlStatementCoupling = true;
                        } else {
                            failedCouplings.add(nextPotentialCouplingLocation);
                        }
                    } else {
                        completedControlStatementCoupling = true;
                    }
                }
            }
            if(numberOfOperationsCompleted == 0) {
                completedAllControlStatementCouplings = true;
            }

        }
        for(int i = 0; i < parsedScript.size(); i++) {
            for(KeyWord keyWord: CONTROL_STATEMENTS) {
                if(getObject(i) == keyWord) {
                    System.out.println("ERROR!");
                    System.out.println("Line #" + (getObjectLineNumber(i)+1));
                    System.out.println("Token number " + (i+1));
                    switch(keyWord) {
                        case IF:
                            System.out.println("Sample Example of the Token | <if_statement> -> if <boolean_expression> <block> else <block> end");
                            System.out.println("Expression causing the error: <if_statement> -> if <bo boolean_expression> <no block> <no else> <no block> <no end>");
                            break;
                        case WHILE:
                            System.out.println("Sample Example of the Token | <while_statement> -> while <boolean_expression> <block> end\n");
                            System.out.println("Expression causing the error: <while_statement> -> while <no boolean_expression> <no block> <no en>\n");
                            break;
                        case FOR:
                            System.out.println("Sample Example of the Token | <for_statement> -> for id = <iter> <block> end");
                            System.out.println("Expression causing the error: <for_statement> -> for <No Id> <No => <No Iter> <No Block> <No End>>");
                            break;
                    }
                    System.exit(0);
                }
            }
        }


        for(int i = 0; i < parsedScript.size(); i++) {
            if(!(getObject(i) instanceof CouplingStatement)) {
                System.out.println("ERROR NONPARSED STATEMENT AT LINE " + (i+1));
                System.exit(0);
            }
        }

        for(int i = 0; i < parsedScript.size(); i++) {
            parsedStatements.add((CouplingStatement) parsedScript.get(i));
        }


    }


    /**
     * Checks if a coupling can be created at the current location
     * @param keyword
     * @param couplingPosition
     * @return
     */
    public boolean canCreateCoupling(KeyWord keyword,int couplingPosition) {
        switch(keyword) {

            case IF:
                if(getObject(couplingPosition+1) instanceof BooleanValueObject) {
                    int foundElseLine = -1;
                    int nonStatementFoundLine = -1;
                    for(int i = couplingPosition+2; i < parsedScript.size(); i++) {
                        if(!(getObject(i) instanceof CouplingStatement)) {
                            if(getObject(i) instanceof KeyWord && getObject(i) == KeyWord.ELSE) {
                                foundElseLine = i;
                                break;
                            } else {
                                nonStatementFoundLine = getObjectLineNumber(i);
                            }
                        }
                    }

                    // Everything found needs to be a statement
                    if(nonStatementFoundLine == -1) {
                        // They actually did find the Else line
                        if(foundElseLine != -1) {

                            int foundEndLine = -1;
                            nonStatementFoundLine = -1;
                            for (int i = foundElseLine + 1; i < parsedScript.size(); i++) {
                                if (!(getObject(i) instanceof CouplingStatement)) {
                                    if (getObject(i) instanceof KeyWord && getObject(i) == KeyWord.END) {
                                        foundEndLine = i;
                                        break;
                                    } else {
                                        nonStatementFoundLine = getObjectLineNumber(i);
                                        break;
                                    }
                                }
                            }


                            if (nonStatementFoundLine == -1) {
                                if (foundEndLine != -1) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                break;
            case WHILE:
                if(getObject(couplingPosition+1) instanceof BooleanValueObject) {
                    int foundEndLine = -1;
                    int nonStatementFoundLine = -1;
                    for(int i = couplingPosition+2; i < parsedScript.size(); i++) {
                        if(!(getObject(i) instanceof CouplingStatement)) {
                            if(getObject(i) instanceof KeyWord && getObject(i) == KeyWord.END) {
                                foundEndLine = i;
                                break;
                            } else {
                                nonStatementFoundLine = getObjectLineNumber(i);
                                break;
                            }
                        }
                    }
                    if (nonStatementFoundLine == -1) {
                        if (foundEndLine != -1) {
                            return true;
                        }

                    }
                }
                break;
            case FOR:
                if(getObject(couplingPosition+1) instanceof CouplingIter) {
                    int foundEndLine = -1;
                    int nonStatementFoundLine = -1;
                    for (int i = couplingPosition + 2; i < parsedScript.size(); i++) {
                        if (!(getObject(i) instanceof CouplingStatement)) {
                            if (getObject(i) instanceof KeyWord && getObject(i) == KeyWord.END) {
                                foundEndLine = i;
                                break;
                            } else {
                                nonStatementFoundLine = getObjectLineNumber(i);
                                break;
                            }
                        }
                    }
                    if (nonStatementFoundLine == -1) {
                        if (foundEndLine != -1) {
                            return true;
                        }

                    }

                }
                break;
            case PRINT:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    return true;
                }
                if(getObject(couplingPosition+1) instanceof BooleanValueObject) {
                    return true;
                }
                if(getObject(couplingPosition+1) instanceof StringValueObject) {
                    return true;
                }
                break;
            case COLLEN:
                if(getObject(couplingPosition-1) instanceof CouplingIntAssignment) {
                    if(getObject(couplingPosition+1) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case ASSIGN:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition-1) instanceof IntIdentifierObject || getObject(couplingPosition-1) instanceof UnidentifiedIdentifierObject) {
                        return true;
                    }
                }
                if(getObject(couplingPosition+1) instanceof BooleanValueObject) {
                    if(getObject(couplingPosition-1) instanceof BooleanIdentifierObject || getObject(couplingPosition-1) instanceof UnidentifiedIdentifierObject) {
                        return true;
                    }
                }
                if(getObject(couplingPosition+1) instanceof StringValueObject) {
                    if(getObject(couplingPosition-1) instanceof StringIdentifierObject || getObject(couplingPosition-1) instanceof UnidentifiedIdentifierObject) {
                        return true;
                    }
                }

                break;
            case LEFT_PARENTHESIS:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof KeyWord && getObject(couplingPosition+2) == KeyWord.RIGHT_PARENTHESIS) {
                        return true;
                    }
                }
                if(getObject(couplingPosition+1) instanceof StringValueObject) {
                    if(getObject(couplingPosition+2) instanceof KeyWord && getObject(couplingPosition+2) == KeyWord.RIGHT_PARENTHESIS) {
                        return true;
                    }
                }
                if(getObject(couplingPosition+1) instanceof BooleanValueObject) {
                    if(getObject(couplingPosition+2) instanceof KeyWord && getObject(couplingPosition+2) == KeyWord.RIGHT_PARENTHESIS) {
                        return true;
                    }
                }

                break;
            case LESS_THAN:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case GREATER_THAN:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case EQUAL_LESS_THAN:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case EQUAL_GREATER_THAN:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case AND:
                if(getObject(couplingPosition+1) instanceof BooleanValueObject) {
                    if(getObject(couplingPosition+2) instanceof BooleanValueObject) {
                        return true;
                    }
                }
                break;
            case EQUAL:
                if(getObject(couplingPosition+1) instanceof IntValueObject | getObject(couplingPosition+1) instanceof StringValueObject | getObject(couplingPosition+1) instanceof BooleanValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject | getObject(couplingPosition+2) instanceof StringValueObject | getObject(couplingPosition+2) instanceof BooleanValueObject) {
                        return true;
                    }
                }
                break;
            case NOT_EQUAL:
                if(getObject(couplingPosition+1) instanceof IntValueObject | getObject(couplingPosition+1) instanceof StringValueObject | getObject(couplingPosition+1) instanceof BooleanValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject | getObject(couplingPosition+2) instanceof StringValueObject | getObject(couplingPosition+2) instanceof BooleanValueObject) {
                        return true;
                    }
                }
                break;
            case ADD:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        return true;
                    }
                }
                if(getObject(couplingPosition+1) instanceof StringValueObject) {
                    if(getObject(couplingPosition+2) instanceof StringValueObject) {
                        return true;
                    }
                }
                break;
            case SUBTRACT:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case MULTIPLY:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if (getObject(couplingPosition + 2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case DIVIDE:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if (getObject(couplingPosition + 2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case INVERT_DIVIDE:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if (getObject(couplingPosition + 2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case EXPONENTIAL:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if (getObject(couplingPosition + 2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
            case MOD:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if (getObject(couplingPosition + 2) instanceof IntValueObject) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    /**
     * Creates the coupling that has been verified to exist, just needs to replace the keywords 
     * @param keyword
     * @param couplingPosition
     */
    public void createCoupling(KeyWord keyword,int couplingPosition) {
        switch(keyword) {
            case IF:
                ArrayList<Integer> encapsulatedStatements = new ArrayList<>();
                BooleanValueObject booleanValueObject = (BooleanValueObject) getObject(couplingPosition+1);
                encapsulatedStatements.add(couplingPosition+1);
                ArrayList<CouplingStatement> ifStatements = new ArrayList<>();
                ArrayList<CouplingStatement> elseStatements = new ArrayList<>();
                boolean foundEnd = false;
                int cursor = couplingPosition+2;
                boolean ifMode = true;
                while(!foundEnd) {
                    if(getObject(cursor) instanceof CouplingStatement) {
                        if(ifMode) {
                            ifStatements.add((CouplingStatement) getObject(cursor));
                        } else {
                            elseStatements.add((CouplingStatement) getObject(cursor));
                        }
                    } else {
                        if(getObject(cursor) instanceof KeyWord) {
                            switch(((KeyWord)getObject(cursor))) {
                                case ELSE:
                                    ifMode = false;
                                    break;
                                case END:
                                    foundEnd = true;
                                    break;
                            }
                        }
                    }
                    encapsulatedStatements.add(cursor);
                    if(!foundEnd) {
                        cursor++;
                    }
                }
                CouplingIfStatement couplingIfStatement = new CouplingIfStatement(booleanValueObject,ifStatements,elseStatements);
                replace(couplingPosition,couplingIfStatement);
                for(int i = cursor; i > couplingPosition; i--) {
                    remove(i);
                }
                break;
            case FOR:
                encapsulatedStatements = new ArrayList<>();
                CouplingIter couplingIter = (CouplingIter) getObject(couplingPosition+1);
                encapsulatedStatements.add(couplingPosition+1); // (


                ArrayList<CouplingStatement> containedStatements = new ArrayList<>();
                foundEnd = false;
                cursor = couplingPosition+2;
                while(!foundEnd) {
                    if(getObject(cursor) instanceof CouplingStatement) {
                        containedStatements.add((CouplingStatement) getObject(cursor));
                    } else {
                        if(getObject(cursor) instanceof KeyWord && getObject(cursor) == KeyWord.END) {
                            foundEnd = true;
                        }
                    }
                    encapsulatedStatements.add(cursor);
                    if(!foundEnd) {
                        cursor++;
                    }
                }
                CouplingForStatement couplingForStatement = new CouplingForStatement(couplingIter,containedStatements);
                replace(couplingPosition,couplingForStatement);
                for(int i = cursor; i > couplingPosition; i--) {
                    remove(i);
                }
                break;
            case WHILE:
                encapsulatedStatements = new ArrayList<>();
                booleanValueObject = (BooleanValueObject) getObject(couplingPosition+1);
                encapsulatedStatements.add(couplingPosition+1);
                containedStatements = new ArrayList<>();
                foundEnd = false;
                cursor = couplingPosition+2;
                while(!foundEnd) {
                    if(getObject(cursor) instanceof CouplingStatement) {
                        containedStatements.add((CouplingStatement) getObject(cursor));
                    } else {
                        if(getObject(cursor) instanceof KeyWord && getObject(cursor) == KeyWord.END) {
                            foundEnd = true;
                        }
                    }
                    encapsulatedStatements.add(cursor);
                    if(!foundEnd) {
                        cursor++;
                    }
                }
                CouplingWhileStatement couplingWhileStatement = new CouplingWhileStatement(booleanValueObject,containedStatements);
                replace(couplingPosition,couplingWhileStatement);
                for(int i = cursor; i > couplingPosition; i--) {
                    remove(i);
                }
                break;
            case PRINT:
                if(getObject(couplingPosition+1) instanceof StringValueObject) {
                    CouplingStringPrint couplingStringPrint = new CouplingStringPrint((StringValueObject) getObject(couplingPosition+1));
                    replace(couplingPosition,couplingStringPrint);
                    remove(couplingPosition+1);
                } else {
                    if (getObject(couplingPosition + 1) instanceof IntValueObject) {
                        CouplingIntPrint couplingIntPrint = new CouplingIntPrint((IntValueObject) getObject(couplingPosition + 1));
                        replace(couplingPosition, couplingIntPrint);
                        remove(couplingPosition + 1);
                    } else {
                        if (getObject(couplingPosition + 1) instanceof BooleanValueObject) {
                            CouplingBooleanPrint couplingBooleanPrint = new CouplingBooleanPrint((BooleanValueObject) getObject(couplingPosition + 1));
                            replace(couplingPosition, couplingBooleanPrint);
                            remove(couplingPosition + 1);
                        }
                    }
                }
                break;
            case COLLEN:
                if(getObject(couplingPosition-1) instanceof CouplingIntAssignment) {
                    if(getObject(couplingPosition+1) instanceof IntValueObject) {
                        CouplingIter returnIter = new CouplingIter(((CouplingIntAssignment)getObject(couplingPosition-1)),((IntValueObject)getObject(couplingPosition+1)));
                        replace(couplingPosition,returnIter);
                        remove(couplingPosition+1);
                        remove(couplingPosition-1);
                    }
                }
                break;
            case ASSIGN:
                if(getObject(couplingPosition+1) instanceof StringValueObject) {
                    StringIdentifierObject stringIdentifierObject = null;
                    if(getObject(couplingPosition-1) instanceof UnidentifiedIdentifierObject) {
                        stringIdentifierObject = wrapStringIdentifier(((UnidentifiedIdentifierObject)getObject(couplingPosition-1)),((StringValueObject)getObject(couplingPosition+1)));
                        identifiers.add(stringIdentifierObject);
                    } else {
                        if(getObject(couplingPosition-1) instanceof StringIdentifierObject) {
                            stringIdentifierObject = (StringIdentifierObject) getObject(couplingPosition-1);
                            stringIdentifierObject.setStringValueObject((StringValueObject) getObject(couplingPosition+1));
                        }
                    }

                    Identifier identifier = stringIdentifierObject.getIdentifier();
                    for(int i = currentLineNumber; i < parsedScript.size(); i++) {
                        if(getObject(i) instanceof UnidentifiedIdentifierObject) {
                            if(((UnidentifiedIdentifierObject) getObject(i)).getIdentifier().getIdentifier().equals(identifier.getIdentifier())) {
                                replace(i,stringIdentifierObject);
                            }
                        }
                        if(getObject(i) instanceof StringIdentifierObject) {
                            if(((StringIdentifierObject) getObject(i)).getIdentifier().equals(identifier)) {
                                replace(i,stringIdentifierObject);
                            }
                        }
                    }


                    CouplingStringAssignment couplingStringAssignment = new CouplingStringAssignment(stringIdentifierObject,((StringValueObject)getObject(couplingPosition+1)));
                    replace(couplingPosition, couplingStringAssignment);
                    remove(couplingPosition+1);
                    remove(couplingPosition-1);


                } else {

                    if (getObject(couplingPosition + 1) instanceof IntValueObject) {
                        IntIdentifierObject intIdentifierObject = null;
                        if (getObject(couplingPosition - 1) instanceof UnidentifiedIdentifierObject) {
                            intIdentifierObject = wrapIntegerIdentifier(((UnidentifiedIdentifierObject) getObject(couplingPosition - 1)), ((IntValueObject) getObject(couplingPosition + 1)));
                            identifiers.add(intIdentifierObject);
                        } else {
                            if (getObject(couplingPosition - 1) instanceof IntIdentifierObject) {
                                intIdentifierObject = (IntIdentifierObject) getObject(couplingPosition - 1);
                                intIdentifierObject.setIntValueObject((IntValueObject) getObject(couplingPosition + 1));
                            }
                        }

                        Identifier identifier = intIdentifierObject.getIdentifier();
                        for (int i = couplingPosition - 1; i < parsedScript.size(); i++) {
                            if (getObject(i) instanceof UnidentifiedIdentifierObject) {
                                if (((UnidentifiedIdentifierObject) getObject(i)).getIdentifier().getIdentifier().equals(identifier.getIdentifier())) {
                                    replace(i, intIdentifierObject);
                                }
                            }
                            if (getObject(i) instanceof IntIdentifierObject) {
                                if (((IntIdentifierObject) getObject(i)).getIdentifier().equals(identifier)) {
                                    replace(i, intIdentifierObject);
                                }
                            }
                        }


                        CouplingIntAssignment couplingIntAssignment = new CouplingIntAssignment(intIdentifierObject, ((IntValueObject) getObject(couplingPosition + 1)));
                        replace(couplingPosition, couplingIntAssignment);
                        remove(couplingPosition + 1);
                        remove(couplingPosition - 1);
                    } else {
                        if (getObject(couplingPosition + 1) instanceof BooleanValueObject) {
                            BooleanIdentifierObject booleanIdentifierObject = null;
                            if (getObject(couplingPosition - 1) instanceof UnidentifiedIdentifierObject) {
                                booleanIdentifierObject = wrapBooleanIdentifier(((UnidentifiedIdentifierObject) getObject(couplingPosition - 1)), ((BooleanValueObject) getObject(couplingPosition + 1)));
                                identifiers.add(booleanIdentifierObject);
                            } else {
                                if (getObject(couplingPosition - 1) instanceof BooleanIdentifierObject) {
                                    booleanIdentifierObject = (BooleanIdentifierObject) getObject(couplingPosition - 1);
                                    booleanIdentifierObject.setBooleanValueObject((BooleanValueObject) getObject(couplingPosition + 1));
                                }
                            }

                            Identifier identifier = booleanIdentifierObject.getIdentifier();
                            for (int i = currentLineNumber; i < parsedScript.size(); i++) {
                                if (getObject(i) instanceof UnidentifiedIdentifierObject) {
                                    if (((UnidentifiedIdentifierObject) getObject(i)).getIdentifier().equals(identifier)) {
                                        replace(i, booleanIdentifierObject);
                                    }
                                }
                                if (getObject(i) instanceof StringIdentifierObject) {
                                    if (((StringIdentifierObject) getObject(i)).getIdentifier().getIdentifier().equals(identifier.getIdentifier())) {
                                        replace(i, booleanIdentifierObject);
                                    }
                                }
                            }


                            CouplingBooleanAssignment couplingBooleanAssignment = new CouplingBooleanAssignment(booleanIdentifierObject, ((BooleanValueObject) getObject(couplingPosition + 1)));
                            replace(couplingPosition, couplingBooleanAssignment);
                            remove(couplingPosition + 1);
                            remove(couplingPosition - 1);
                        }
                    }
                }
                break;


            case LEFT_PARENTHESIS:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof KeyWord && getObject(couplingPosition+2) == KeyWord.RIGHT_PARENTHESIS) {
                        CouplingIntParentheses couplingIntParentheses = new CouplingIntParentheses(((IntValueObject)getObject(couplingPosition+1)));
                        replace(couplingPosition, couplingIntParentheses);
                        remove(couplingPosition+(2));
                        remove(couplingPosition+(1));
                    }
                } else {
                    if (getObject(couplingPosition + 1) instanceof StringValueObject) {
                        if(getObject(couplingPosition+2) instanceof KeyWord && getObject(couplingPosition+2) == KeyWord.RIGHT_PARENTHESIS) {
                            CouplingStringParentheses couplingStringParentheses = new CouplingStringParentheses(((StringValueObject) getObject(couplingPosition + 1)));
                            parsedScript.set(couplingPosition, couplingStringParentheses);
                            parsedScript.remove(couplingPosition + (2));
                            parsedScript.remove(couplingPosition + (1));
                        }
                    } else {
                        if (getObject(couplingPosition + 1) instanceof BooleanValueObject) {
                            if(getObject(couplingPosition+2) instanceof KeyWord && getObject(couplingPosition+2) == KeyWord.RIGHT_PARENTHESIS) {
                                CouplingBooleanParetheses couplingBooleanParetheses = new CouplingBooleanParetheses(((BooleanValueObject) getObject(couplingPosition + 1)));
                                parsedScript.set(couplingPosition, couplingBooleanParetheses);
                                parsedScript.remove(couplingPosition + (2));
                                parsedScript.remove(couplingPosition + (1));
                            }
                        }
                    }
                }


                break;
            case LESS_THAN:
                CouplingBooleanLessThan couplingBooleanLessThan = new CouplingBooleanLessThan(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                remove(couplingPosition+(2));
                replace(couplingPosition,couplingBooleanLessThan);
                remove(couplingPosition+(1));
                break;
            case GREATER_THAN:
                CouplingBooleanGreaterThan couplingBooleanGreaterThan = new CouplingBooleanGreaterThan(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                remove(couplingPosition+(2));
                replace(couplingPosition,couplingBooleanGreaterThan);
                remove(couplingPosition+(1));
                break;
            case EQUAL_LESS_THAN:
                CouplingBooleanEqualLessThan couplingBooleanEqualLessThan = new CouplingBooleanEqualLessThan(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                remove(couplingPosition+(2));
                replace(couplingPosition,couplingBooleanEqualLessThan);
                remove(couplingPosition+(1));
                break;
            case EQUAL_GREATER_THAN:
                CouplingBooleanEqualGreaterThan couplingBooleanEqualGreaterThan = new CouplingBooleanEqualGreaterThan(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                remove(couplingPosition+(2));
                replace(couplingPosition,couplingBooleanEqualGreaterThan);
                remove(couplingPosition+(1));
                break;
            case AND:
                CouplingBooleanAnd couplingBooleanAnd = new CouplingBooleanAnd(((BooleanValueObject)getObject(couplingPosition+1)),((BooleanValueObject)getObject(couplingPosition+2)));
                remove(couplingPosition+(2));
                replace(couplingPosition,couplingBooleanAnd);
                remove(couplingPosition+(1));
                break;
            case EQUAL:
                CouplingBooleanEquals couplingBooleanEquals = new CouplingBooleanEquals(getObject(couplingPosition+1),getObject(couplingPosition+2));
                remove(couplingPosition+(2));
                replace(couplingPosition,couplingBooleanEquals);
                remove(couplingPosition+(1));
                break;
            case NOT_EQUAL:
                CouplingBooleanNotEquals couplingBooleanNotEquals = new CouplingBooleanNotEquals(getObject(couplingPosition+1),getObject(couplingPosition+2));
                remove(couplingPosition+(2));
                replace(couplingPosition,couplingBooleanNotEquals);
                remove(couplingPosition+(1));
                break;
            case ADD:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        CouplingIntAdd couplingIntAdd = new CouplingIntAdd(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                        remove(couplingPosition+(2));
                        replace(couplingPosition,couplingIntAdd);
                        remove(couplingPosition+(1));
                    }
                } else {
                    if (getObject(couplingPosition + 1) instanceof StringValueObject) {
                        if (getObject(couplingPosition + 2) instanceof StringValueObject) {
                            CouplingStringConcat couplingStringConcat = new CouplingStringConcat(((StringValueObject) getObject(couplingPosition + 1)), ((StringValueObject) getObject(couplingPosition + 2)));
                            remove(couplingPosition + (2));
                            replace(couplingPosition, couplingStringConcat);
                            remove(couplingPosition + (1));
                        }
                    }
                }
                break;
            case SUBTRACT:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        CouplingIntSubtract couplingIntSubtract = new CouplingIntSubtract(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                        remove(couplingPosition+(2));
                        replace(couplingPosition,couplingIntSubtract);
                        remove(couplingPosition+(1));
                    }
                }
                break;
            case MULTIPLY:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        CouplingIntMultiply couplingIntMultiply = new CouplingIntMultiply(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                        remove(couplingPosition+(2));
                        replace(couplingPosition,couplingIntMultiply);
                        remove(couplingPosition+(1));
                    }
                }
                break;
            case DIVIDE:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        CouplingIntDivide couplingIntDivide = new CouplingIntDivide(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                        remove(couplingPosition+(2));
                        replace(couplingPosition,couplingIntDivide);
                        remove(couplingPosition+(1));
                    }
                }
                break;
            case INVERT_DIVIDE:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        CouplingIntInvertDivide couplingIntInvertDivide = new CouplingIntInvertDivide(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                        remove(couplingPosition+(2));
                        replace(couplingPosition,couplingIntInvertDivide);
                        remove(couplingPosition+(1));
                    }
                }
                break;
            case EXPONENTIAL:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        CouplingIntExponential couplingIntExponential = new CouplingIntExponential(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                        remove(couplingPosition+(2));
                        replace(couplingPosition,couplingIntExponential);
                        remove(couplingPosition+(1));
                    }
                }
                break;
            case MOD:
                if(getObject(couplingPosition+1) instanceof IntValueObject) {
                    if(getObject(couplingPosition+2) instanceof IntValueObject) {
                        CouplingIntMod couplingIntMod = new CouplingIntMod(((IntValueObject)getObject(couplingPosition+1)),((IntValueObject)getObject(couplingPosition+2)));
                        remove(couplingPosition+(2));
                        replace(couplingPosition,couplingIntMod);
                        remove(couplingPosition+(1));
                    }
                }
                break;
        }
    }


    public StringIdentifierObject wrapStringIdentifier(UnidentifiedIdentifierObject unidentifiedIdentifierObject, StringValueObject stringValueObject) {
        Identifier identifier = unidentifiedIdentifierObject.getIdentifier();
        StringIdentifierObject stringIdentifierObject = new StringIdentifierObject(identifier, stringValueObject);
        return stringIdentifierObject;
    }

    public IntIdentifierObject wrapIntegerIdentifier(UnidentifiedIdentifierObject unidentifiedIdentifierObject, IntValueObject intValueObject) {
        Identifier identifier = unidentifiedIdentifierObject.getIdentifier();
        IntIdentifierObject intIdentifierObject = new IntIdentifierObject(identifier, intValueObject);
        return intIdentifierObject;
    }

    public BooleanIdentifierObject wrapBooleanIdentifier(UnidentifiedIdentifierObject unidentifiedIdentifierObject, BooleanValueObject booleanValueObject) {
        Identifier identifier = unidentifiedIdentifierObject.getIdentifier();
        BooleanIdentifierObject booleanIdentifierObject = new BooleanIdentifierObject(identifier, booleanValueObject);
        return booleanIdentifierObject;
    }



    public void replace(int position, Object object) {
        parsedScript.set(position,object);
        lineNumbers.set(position,-1);
    }

    public void remove(int position) {
        parsedScript.remove(position);
        lineNumbers.remove(position);
    }

    private boolean isKeyword(Object object) {
        return (object instanceof KeyWord);
    }

    private boolean isIdentifier(Object object) {
        return (object instanceof Identifier);
    }

    private boolean isValue(Object object) {
        return (object instanceof Value);
    }

    private boolean isBooleanValueObject(Object object) {
        return (object instanceof BooleanValueObject);
    }

    private boolean isStringValueObject(Object object) {
        return (object instanceof StringValueObject);
    }

    private boolean isIntValueObject(Object object) {
        return (object instanceof IntValueObject);
    }

    private boolean isBooleanIdentifierObject(Object object) {
        return (object instanceof BooleanIdentifierObject);
    }

    private boolean isStringIdentifierObject(Object object) {
        return (object instanceof StringIdentifierObject);
    }

    private boolean isIntIdentifierObject(Object object) {
        return (object instanceof IntIdentifierObject);
    }

    private Object getObject(int i) {
        return parsedScript.get(i);
    }

    private int getObjectLineNumber(int i) {
        return lineNumbers.get(i);
    }

    private Object getObjectOffset(int i, int offset) {
        return parsedScript.get(i+offset);
    }


    public ArrayList<CouplingStatement> getParsedStatements() {
        return parsedStatements;
    }

    public ArrayList<IdentifierCoupling> getIdentifiers() {
        return identifiers;
    }
}
