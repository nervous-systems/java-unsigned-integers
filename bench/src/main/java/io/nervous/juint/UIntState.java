package io.nervous.juint;

import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class UIntState {
  int bits = 256;

  UInt256   max  = new UInt256(new int[]{-1, -1, -1, -1, -1, -1, -1, -1});
  UInt256[] full = new UInt256[]{
    new UInt256(new int[]{-1,                Short.MAX_VALUE, -1, Integer.MAX_VALUE,
                          Integer.MIN_VALUE, 77,               3, -1}),
    new UInt256(new int[]{Short.MAX_VALUE,   -1,              -1, Integer.MAX_VALUE,
                          Short.MIN_VALUE,   13,               2, 0}),
    new UInt256(new int[]{Short.MIN_VALUE,    0,               0, Integer.MAX_VALUE,
                          Integer.MAX_VALUE, 11,               1, Short.MIN_VALUE})};

  UInt256   one  = UInt256.ONE;
  UInt256   zero = UInt256.ZERO;
  UInt256[] onew = new UInt256[]{
    new UInt256(new int[]{-1}),
    new UInt256(new int[]{Integer.MIN_VALUE}),
    new UInt256(new int[]{1})};

  UInt256[] twow = new UInt256[]{
    new UInt256(new int[]{1,                  1}),
    new UInt256(new int[]{Integer.MAX_VALUE, -1}),
    new UInt256(new int[]{Short.MIN_VALUE,    9})};

  UInt256[] half = new UInt256[]{
    new UInt256(new int[]{1,                Short.MAX_VALUE, -1, Integer.MAX_VALUE}),
    new UInt256(new int[]{Short.MAX_VALUE, -1,               -1, Integer.MAX_VALUE}),
    new UInt256(new int[]{Short.MIN_VALUE,  0,                0, Integer.MAX_VALUE})};
}
