package com.DCB.Interpreter;

import com.DCB.LexicalObjects.KeyWord;
import com.DCB.LexicalObjects.Value;
import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Value.BooleanValueObject;
import com.DCB.ParserObjects.Value.Identifiers.BooleanIdentifierObject;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;
import com.DCB.ParserObjects.Value.Identifiers.IntIdentifierObject;
import com.DCB.ParserObjects.Value.Identifiers.StringIdentifierObject;
import com.DCB.ParserObjects.Value.Wrapper.BooleanValueWrapper;
import com.DCB.ParserObjects.Value.Wrapper.IntValueWrapper;
import com.DCB.ParserObjects.Value.Wrapper.StringValueWrapper;

import java.util.ArrayList;

public class InterpreterManager {
    private final ArrayList<CouplingStatement> statements;
    private final ArrayList<IdentifierCoupling> identifiers;
    public InterpreterManager(ArrayList<CouplingStatement> statements, ArrayList<IdentifierCoupling> identifiers) {
        this.statements = statements;
        this.identifiers = identifiers;
    }

    public void executeAllStatements() {
        for(int i = 0; i < identifiers.size(); i++) {
            IdentifierCoupling identifierCoupling = identifiers.get(i);
            if(identifierCoupling instanceof IntIdentifierObject) {
                ((IntIdentifierObject) identifierCoupling).setIntValueObject(new IntValueWrapper(new Value(KeyWord.VariableType.NUMBER,0)));
            }
            if(identifierCoupling instanceof BooleanIdentifierObject) {
                ((BooleanIdentifierObject) identifierCoupling).setBooleanValueObject(new BooleanValueWrapper(new Value(KeyWord.VariableType.BOOLEAN, false)) {
                });
            }
            if(identifierCoupling instanceof StringIdentifierObject) {
                ((StringIdentifierObject) identifierCoupling).setStringValueObject(new StringValueWrapper(new Value(KeyWord.VariableType.STRING,"")));
            }
        }
        for(int i = 0; i < statements.size(); i++) {
            statements.get(i).executeStatement();
        }
    }
}
