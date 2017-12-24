package io.nervous.juint;

import java.math.BigInteger;

import org.junit.BeforeClass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

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

  @Test
  public void copyCtor() {
    for(int i = 0; i < SAMPLE_SMALL; i++) {
      int[] ints    = randomints(UInt128.MAX_WIDTH);
      UInt128 small = new UInt128(ints);
      assertEquals(new UInt256(small.toBigInteger()), new UInt256(small));
      assertEquals(small, new UInt128(new UInt256(small)));
      assertArrayEquals(ints, new UInt256(small).ints);
    }
  }
}
