package model.values;

import model.types.IType;
import model.types.IntType;

public class IntValue implements IValue {
    private int value;

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public boolean equals(IValue another) {
        if (!another.getType().equals(this.getType()))
            return false;
        return ((IntValue) another).getValue() == this.getValue();
    }

    @Override
    public IValue deepCopy() {
        return new IntValue(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}