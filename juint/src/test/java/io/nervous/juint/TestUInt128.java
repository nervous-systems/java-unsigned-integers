package io.nervous.juint;

import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

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

  @Test
  public void copyCtor() {
    for(int i = 0; i < SAMPLE_SMALL; i++) {
      int[] ints    = randomints(UInt256.MAX_WIDTH);
      UInt256 large = new UInt256(ints);
      assertEquals(new UInt128(large.toBigInteger()), new UInt128(large));
      assertArrayEquals(
        Arrays.stripLeadingZeroes(ints, UInt256.MAX_WIDTH - UInt128.MAX_WIDTH),
        new UInt128(large).ints);
    }
  }
}
