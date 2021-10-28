package com.DCB.ParserObjects.Couplings.ControlStatements;

import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;

import java.util.ArrayList;

/**
 * All control statements extend this class making them identifiable
 */
public abstract class CouplingControlStatement extends CouplingStatement {

    public CouplingControlStatement(CoupleObjectType coupleObjectType) {
        super(coupleObjectType);
    }

    // Have execution control the control statement
}
