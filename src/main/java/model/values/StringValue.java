package model.values;

import model.types.IType;
import model.types.StringType;

public class StringValue implements IValue {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public boolean equals(IValue another) {
        if (!another.getType().equals(this.getType()))
            return false;
        return ((StringValue) another).getValue().equals(this.getValue());
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
