package com.DCB.ParserObjects.Value.Identifiers;

import com.DCB.LexicalObjects.Identifier;
import com.DCB.LexicalObjects.KeyWord;
import com.DCB.ParserObjects.CouplingObject;
import com.DCB.ParserObjects.CouplingObjectFactory;

public abstract class IdentifierCoupling extends CouplingObject {
    protected final Identifier identifier;
    public IdentifierCoupling(CoupleObjectType coupleObjectType, Identifier identifier) {
        super(coupleObjectType);
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
