package io.nervous.juint;

import java.math.BigInteger;

import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class BigState {
  int bits = 256;

  static BigInteger b(int[] vs) {
    return new UInt256(vs).toBigInteger();
  }

  BigInteger   max  = b(new int[]{-1, -1, -1, -1, -1, -1, -1, -1});
  BigInteger[] full = new BigInteger[]{
    b(new int[]{-1,                Short.MAX_VALUE, -1, Integer.MAX_VALUE,
                Integer.MIN_VALUE, 77,               3, -1}),
    b(new int[]{Short.MAX_VALUE,   -1,              -1, Integer.MAX_VALUE,
                Short.MIN_VALUE,   13,               2, 0}),
    b(new int[]{Short.MIN_VALUE,    0,               0, Integer.MAX_VALUE,
                Integer.MAX_VALUE, 11,               1, Short.MIN_VALUE})};

  BigInteger   one  = BigInteger.ONE;
  BigInteger   zero = b(new int[0]);
  BigInteger[] onew = new BigInteger[]{
    b(new int[]{-1}),
    b(new int[]{Integer.MIN_VALUE}),
    b(new int[]{1})};

  BigInteger[] twow = new BigInteger[]{
    b(new int[]{1,                  1}),
    b(new int[]{Integer.MAX_VALUE, -1}),
    b(new int[]{Short.MIN_VALUE,    9})};

  BigInteger[] half = new BigInteger[]{
    b(new int[]{-1,               Short.MAX_VALUE, -1, Integer.MAX_VALUE}),
    b(new int[]{Short.MAX_VALUE, -1,               -1, Integer.MAX_VALUE}),
    b(new int[]{Short.MIN_VALUE,  0,                0, Integer.MAX_VALUE})};
}
