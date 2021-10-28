package com.DCB.ParserObjects.Couplings.Operations.Boolean;

//import org.graalvm.compiler.phases.common.NonNullParametersPhase;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.BooleanValueObject;
import com.DCB.ParserObjects.Value.IntValueObject;
import com.DCB.ParserObjects.Value.StringValueObject;


/**
 * Coupling that supports the Boolean NotEquals operation
 */
public class CouplingBooleanNotEquals extends CouplingObject implements BooleanValueObject {
    private final Object object1;
    private final Object object2;

    public CouplingBooleanNotEquals(Object object1, Object object2) {
        super(CoupleObjectType.BOOLEAN_NOT_EQUAl);
        this.object1 = object1;
        this.object2 = object2;
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
        String identifier = "[" + coupleObjectType + " | ";
        if(object1 instanceof CouplingObject) {
            identifier += ((CouplingObject) object1).getStringIdentifier();
        } else {
            identifier += object1;
        }
        if(object2 instanceof CouplingObject) {
            identifier += ((CouplingObject) object2).getStringIdentifier();
        } else {
            identifier += object2;
        }
        identifier += " ]";
        return identifier;
    }

  //See CouplingForStatement.java for explanation.
    @Override
    public String getParsedGrammar() {
    	
    	//object1 and 2 have the chance of not being CoupledObjects, this will error check. 
    	CouplingObject number1 = (object1 instanceof CouplingObject)? (CouplingObject)object1 : null;
        CouplingObject number2 = (object2 instanceof CouplingObject)? (CouplingObject)object2 : null;
        String n1 = (number1 == null) ? "" : number1.getParsedGrammar();
        String n2 = (number2 == null) ? "" : number2.getParsedGrammar();
    	
    	return "<boolean_expression> -> <relative_op> <arithmetic_expression> <arithmetic_expression>  \n"
        		+ "<relative_op> -> ne_operator\n"
        		+ n1
        		+ n2;
    }


    @Override
    public boolean getValue() {

        if(object1 instanceof IntValueObject && object2 instanceof IntValueObject) {
            return ((IntValueObject) object1).getValue() != ((IntValueObject) object2).getValue();
        } else {
            if (object1 instanceof BooleanValueObject && object2 instanceof BooleanValueObject) {
                return ((BooleanValueObject) object1).getValue() != ((BooleanValueObject) object2).getValue();
            } else {
                if (object1 instanceof StringValueObject && object2 instanceof StringValueObject) {
                    return !((StringValueObject) object1).getValue().equals(((StringValueObject) object2).getValue());
                } else {
                    return false;
                }
            }
        }
    }


}