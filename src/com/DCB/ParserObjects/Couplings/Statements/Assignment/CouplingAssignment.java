package com.DCB.ParserObjects.Couplings.Statements.Assignment;

import com.DCB.ParserObjects.Couplings.Statements.CouplingStatement;
import com.DCB.ParserObjects.Value.Identifiers.IdentifierCoupling;

import java.util.ArrayList;

public abstract class CouplingAssignment extends CouplingStatement {

    public CouplingAssignment(CoupleObjectType coupleObjectType) {
        super(coupleObjectType);
    }

    public void doAssignment(ArrayList<IdentifierCoupling> identifiers) {

    }
}
