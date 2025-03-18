package model.values;

import model.types.IType;
import model.types.BoolType;

public class BoolValue implements IValue {
    private boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(IValue another) {
        if (!another.getType().equals(this.getType()))
            return false;
        return ((BoolValue) another).getValue() == this.getValue();
    }

    @Override
    public IValue deepCopy() {
        return new BoolValue(value);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}