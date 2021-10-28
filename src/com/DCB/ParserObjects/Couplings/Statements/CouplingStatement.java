package com.DCB.ParserObjects.Couplings.Statements;

import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;

import java.util.ArrayList;


/**
 * All statements extend this class making them identifiable
 */
public abstract class CouplingStatement extends CouplingObject {
    private boolean isLateStatement = false;
    public CouplingStatement(CoupleObjectType coupleObjectType) {
        super(coupleObjectType);
    }

    public void setLateStatement() {
        isLateStatement = true;
    }

    public boolean isLateStatement() {
        return isLateStatement;
    }

    public abstract void executeStatement();

}
