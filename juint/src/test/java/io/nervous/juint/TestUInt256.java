package io.nervous.juint;

import java.math.BigInteger;

import org.junit.BeforeClass;

public class TestUInt256 extends Properties<UInt256> {
  UInt256 construct(int[] ints)   { return new UInt256(ints); }
  UInt256 construct(BigInteger b) { return new UInt256(b);    }
  UInt256 construct(long   v)     { return new UInt256(v);    }

  UInt256 construct(String s, int radix) {
    return new UInt256(s, radix);
  }

  int maxWidth() {
    return UInt256.MAX_WIDTH;
  }
}
