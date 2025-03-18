package model.types;

import model.values.IValue;
import model.values.RefValue;

public class RefType implements IType {
    private IType innerType;

    public RefType(IType innerType) {
        this.innerType = innerType;
    }

    public IType getInnerType() {
        return innerType;
    }

    @Override
    public boolean equals(IType another) {
        if (another instanceof RefType) {
            RefType refType = (RefType) another;
            return innerType.equals(refType.getInnerType());
        }
        return false;
    }

    @Override
    public String toString() {
        return "ref(" + innerType.toString() + ")";
    }

    @Override
    public IValue getDefaultValue() {
        return new RefValue(0, innerType);
    }
}
