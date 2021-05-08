package org.parker.assembleride.core;

public class Test {

    public static void main(String[] args) {
        int a = 0b00111111101100100001000000000000;
        int b = 0b00111100001010000000000000000000;
        int result = multToIEEE(a,b);

        //System.out.println("0|01111100|01000000000000000000000" + " " + formatIntegerIEEE(a));

        float aFloat = Float.intBitsToFloat(a);
        float bFloat = Float.intBitsToFloat(b);
        float resultFloat = aFloat * bFloat;

        System.out.println("A: " + aFloat + " B: " + bFloat);
        System.out.println("Expected: " + resultFloat + " " + formatIntegerIEEE(Float.floatToRawIntBits(resultFloat)));
        System.out.println("Got: " + Float.intBitsToFloat(result) + " " + formatIntegerIEEE(result));
    }

    public static int multToIEEE(long a, long b){
        int result = 0;
        result |= (int)(((a >> 31) ^ (b >> 31)) << 31);
        result |= (int)(((((( 0x7F800000 & a) >> 23) + (( 0x7F800000 & b) >> 23)) - 127) & 0xFF) << 23);
        result |= (int)(((
                (((0x7FFFFF & a) | 0x00800000L)  << 8) *
                (((0x7FFFFF & b) | 0x00800000L) << 7))
                >> 38) & 0x7FFFFFL);
        return result;
    }

    public static int divToIEEE(long a, long b){
        int result = 0;
        result |= (int)(((a >> 31) ^ (b >> 31)) << 31);
        result |= (int)(((((( 0x7F800000 & a) >> 23) - (( 0x7F800000 & b) >> 23)) - 129) & 0xFF) << 23);
        result |= (int)(((((((0x7FFFFFL & a) << 8) | 0x80000000L) << 15) / ((( 0x7FFFFFL & b)) | 0x00800000L))));
        System.out.println( String.format("%64s", Long.toBinaryString((Long)
                ((((( 0x7FFFFF & a) | 0x00800000L) << 8)) << 31) / (((( 0x7FFFFF & b) | 0x00800000L) << 8))
        )).replaceAll(" ", "0"));
        return result;
    }

    public static String formatIntegerIEEE(int integer){
        String temp = String.format("%32s", Integer.toBinaryString(integer)).replaceAll(" ", "0");
        temp = addChar(temp, '|', 9);
        temp = addChar(temp, '|', 1);
        return temp;
    }

    public static String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }
}