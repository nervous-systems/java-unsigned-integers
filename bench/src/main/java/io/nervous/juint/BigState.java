package io.nervous.juint;

import org.openjdk.jmh.annotations.*;

import org.apache.tuweni.units.bigints.UInt256;

@State(Scope.Thread)
public class BigState {
    int bits = 256;

    UInt256   max  =  UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{-1, -1, -1, -1, -1, -1, -1, -1}).toBigInteger());
    UInt256[] full = new UInt256[]{
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{-1, Short.MAX_VALUE, -1, Integer.MAX_VALUE,
                                  Integer.MIN_VALUE, 77,               3, -1}).toBigInteger()),
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{Short.MAX_VALUE,   -1,              -1, Integer.MAX_VALUE,
                                  Short.MIN_VALUE,   13,               2, 0}).toBigInteger()),
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{Short.MIN_VALUE,    0,               0, Integer.MAX_VALUE,
                                  Integer.MAX_VALUE, 11,               1, Short.MIN_VALUE}).toBigInteger())};

    UInt256   one  = UInt256.ONE;
    UInt256   zero = UInt256.ZERO;
    UInt256[] onew = new UInt256[]{
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{-1}).toBigInteger()),
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{Integer.MIN_VALUE}).toBigInteger()),
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{1}).toBigInteger())};

    UInt256[] twow = new UInt256[]{
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{1,                  1}).toBigInteger()),
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{Integer.MAX_VALUE, -1}).toBigInteger()),
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{Short.MIN_VALUE,    9}).toBigInteger())};

    UInt256[] half = new UInt256[]{
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{1,                Short.MAX_VALUE, -1, Integer.MAX_VALUE}).toBigInteger()),
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{Short.MAX_VALUE, -1,               -1, Integer.MAX_VALUE}).toBigInteger()),
            UInt256.valueOf(new io.nervous.juint.UInt256(new int[]{Short.MIN_VALUE,  0,                0, Integer.MAX_VALUE}).toBigInteger())};
}
