package io.nervous.juint;

import java.math.BigInteger;

public class TestUInt128 extends Properties<UInt128> {
  UInt128 construct(int[] ints)   { return new UInt128(ints); }
  UInt128 construct(BigInteger b) { return new UInt128(b);    }
  UInt128 construct(long   v)     { return new UInt128(v);    }

  UInt128 construct(String s, int radix) {
    return new UInt128(s, radix);
  }

  int maxWidth() {
    return UInt128.MAX_WIDTH;
  }
}
