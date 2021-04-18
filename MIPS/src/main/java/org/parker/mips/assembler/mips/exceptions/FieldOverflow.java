package org.parker.mips.assembler.mips.exceptions;

public class FieldOverflow extends RuntimeException{
    public final int field;
    public final long value;
    public final long max;
    public final long min;

    public FieldOverflow(String message, int field, long value, long max, long min){
        super(message);
        this.field = field;
        this.value = value;
        this.max = max;
        this.min = min;
    }

    public FieldOverflow(int field, long value, long max, long min){
        this("Field: " + field + " is too large/small to fit into its allocated space found: " + value + " max: " + max + " min: " + min, field, value, max, min);
    }
}
