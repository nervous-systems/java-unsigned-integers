package io.nervous.juint;

import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public abstract class Properties<T extends UInt<T>> {
  abstract T   construct(int[]      ints);
  abstract T   construct(BigInteger b);
  abstract T   construct(long       v);
  abstract T   construct(String     s, int radix);
  abstract int maxWidth ();

  T zero = construct(new int[0]);
  T max  = construct(Arrays.maxValue(maxWidth()));
  T one  = fromInt(1), two = fromInt(2), x, y, rand1w, xcopy, ycopy, rand1wcopy;
  BigInteger xb, yb, rand1wb;

  static int SAMPLE_BIG;
  static int SAMPLE_MED;
  static int SAMPLE_SMALL;

  @BeforeClass
  public static void init() {
    String v     = System.getenv("JUINT_SAMPLE_FACTOR");
    int i        = v == null ? 1 : Integer.parseInt(v);
    SAMPLE_BIG   = 4096 * i;
    SAMPLE_MED   = 2048 * i;
    SAMPLE_SMALL = 1024 * i;
    System.out.println("Max interations: " + SAMPLE_BIG);
  }

  Random rnd = new SecureRandom();

  @Before
  public void cycle() {
    x       = random();
    y       = random();
    rand1w  = random(1);

    xb      = x     .toBigInteger();
    yb      = y     .toBigInteger();
    rand1wb = rand1w.toBigInteger();

    xcopy      = construct(x.toIntArray());
    ycopy      = construct(y.toIntArray());
    rand1wcopy = construct(rand1w.toIntArray());
  }

  @After
  public void immutable() {
    assertEquals(x,      xcopy);
    assertEquals(y,      ycopy);
    assertEquals(rand1w, rand1wcopy);
  }

  T fromInt(int i) { return i == 0 ? zero : construct(new int[]{i}); }
  T random()       { return random(Math.abs(rnd.nextInt() % (maxWidth()+1))); }

  private static final int SMALL_MAX = Short.MAX_VALUE;

  T random(int n) {
    if(n == 0)
      return zero;
    if(rnd.nextFloat() < 0.025)
      return construct(Arrays.maxValue(n));

    int[] ints          = new int[n];
    final boolean small = rnd.nextFloat() < 0.25;
    do {
      ints[0] = small ? rnd.nextInt(SMALL_MAX) : rnd.nextInt();
    } while(ints[0] == 0);
    for(int i = 1; i < n; i++)
      ints[i] = small ? rnd.nextInt(SMALL_MAX) : rnd.nextInt();
    return construct(ints);
  }

  T randomNonZero() {
    T x;
    do {
      x = random();
    } while(x.isZero());
    return x;
  }

  void eq(BigInteger a, UInt b) {
    assertTrue(
      ("UInt<" + java.util.Arrays.toString(b.ints) +
       "> != Big<" + java.util.Arrays.toString(construct(a).ints) + ">"),
      b.equals(a));
  }

  BigInteger big(T o)    { return o.toBigInteger();      }
  BigInteger big(long i) { return BigInteger.valueOf(i); }

  BigInteger trunc(BigInteger u) {
    BigInteger mask = BigInteger.ONE.shiftLeft(32 * maxWidth())
      .subtract(BigInteger.ONE);
    return u.and(mask);
  }

  @Test
  public void longCtorInvariant() {
    assertEquals(construct(new int[]{-1, -1}), construct(-1L));
    assertEquals(zero,                         construct(0L));
    assertEquals(one,                          construct(1L));
  }

  @Test
  public void isZeroInvariant() {
    assertTrue  (zero.isZero());
    assertFalse (one .isZero());
    assertFalse (two .isZero());
    assertTrue  (one .subtract(one).isZero());
    assertFalse (max .isZero());
  }

  @Test
  public void notInvariant() {
    assertEquals(zero.not(),       max);
    assertEquals(zero.not().not(), zero);

    assertEquals(max.not(),        zero);
    assertEquals(max.not().not(),  max);

    assertEquals(one.not(),        max.subtract(one));
    assertEquals(one.not().not(),  one);

    int[] exp = Arrays.maxValue(maxWidth());
    exp[exp.length - 1] = ~1;
    exp[exp.length - 2] = ~-1;
    assertEquals(construct(exp), construct(new int[]{-1, 1}).not());
  }

  @Test
  public void bitLength() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      assertEquals(xb.bitLength(), x.bitLength());
  }

  @Test
  public void not() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      eq(trunc(xb.not()), x.not());
  }

  @Test
  public void getLowestSetBit() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      assertEquals(xb.getLowestSetBit(), x.getLowestSetBit());
  }

  @Test
  public void shiftLeftInvariant() {
    assertEquals(zero,                       zero.shiftLeft(0));
    assertEquals(one,                        one .shiftLeft(0));
    assertEquals(construct(new int[]{1, 0}), one .shiftLeft(32));
  }

  @Test
  public void shiftRightInvariant() {
    assertEquals(zero, zero.shiftRight(0));
    assertEquals(one,  one .shiftRight(0));
    assertEquals(zero, one .shiftRight(32));
    assertEquals(one,  construct(new int[]{1, 0}).shiftRight(32));
  }

  @Test
  public void shiftLeftLimit() {
    int[] ints = new int[maxWidth()];
    ints[0]    = -1;

    T a = construct(ints);
    for(int places = 0; places <= 32; places++)
      eq(trunc(big(a).shiftLeft(places)), a.shiftLeft(places));
  }

  @Test
  public void shiftLeftWord() {
    for(int i = 0; i < SAMPLE_SMALL; i++, cycle())
      for(int places = 0; places <= x.ints.length * 32; places += 32)
        eq(trunc(xb.shiftLeft(places)), x.shiftLeft(places));
  }

  @Test
  public void shiftRightWord() {
    for(int i = 0; i < SAMPLE_SMALL; i++, cycle())
      for(int places = 0; places <= x.ints.length * 32; places += 32)
        eq(xb.shiftRight(places), x.shiftRight(places));
  }

  @Test
  public void shiftLeft() {
    for(int i = 0; i < SAMPLE_SMALL; i++, cycle()) {
      for(int places = 0; places <= x.ints.length * 32; places++)
        eq(trunc(xb.shiftLeft(places)), x.shiftLeft(places));
    }
  }

  @Test
  public void shiftRight() {
    for(int i = 0; i < SAMPLE_SMALL; i++, cycle())
      for(int places = 0; places <= x.ints.length * 32; places++)
        eq(xb.shiftRight(places), x.shiftRight(places));
  }

  @Test
  public void shift() {
    for(int i = 0; i < SAMPLE_SMALL / 2; i++, cycle()) {
      int w = x.ints.length * 32;
      for(int places = -w; places <= w; places++) {
        eq(trunc(xb.shiftLeft (places)), x.shiftLeft(places));
        eq(trunc(xb.shiftRight(places)), x.shiftRight(places));;
      }
    }
  }

  @Test
  public void multiplyInvariant() {
    assertEquals(zero, one .multiply(zero));
    assertEquals(zero, zero.multiply(one));
    assertEquals(zero, zero.multiply(zero));
    assertEquals(one,  one .multiply(one));

    assertEquals(zero, max .multiply(zero));
    assertEquals(zero, zero.multiply(max));
    assertEquals(max,  max .multiply(one));
    assertEquals(max,  one .multiply(max));

    assertEquals(two, two.multiply(one));
    assertEquals(two, one.multiply(two));

    assertEquals(max.subtract(one), max.multiply(two));
    assertEquals(max.subtract(one), two.multiply(max));

    T half = max.divide(fromInt(2));
    assertEquals(max.subtract(one), half.multiply(two));
    assertEquals(max.subtract(one), two .multiply(half));
    assertEquals(one,               half.multiply(half));
  }

  @Test
  public void multiply1w() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      eq(trunc(xb.multiply(rand1wb)), x.multiply(rand1w));
  }

  @Test
  public void multiply() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      eq(trunc(xb.multiply(yb)), x.multiply(y));
    }
  }

  @Test
  public void multiplySquare() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      eq(trunc(xb.multiply(xb)), x.multiply(x));
    }
  }

  @Test(expected=ArithmeticException.class)
  public void powNegative() {
    one.pow(-1);
  }

  @Test
  public void powInvariant() {
    assertEquals(one,        one .pow(0));
    assertEquals(zero,       zero.pow(1));
    assertEquals(two,        two .pow(1));
    assertEquals(one,        two .pow(0));
    assertEquals(fromInt(4), two .pow(2));
  }

  @Test
  public void powSmall() {
    for(int i = 0; i < maxWidth() * 32; i++)
      eq(big(two).pow(i), two.pow(i));
    eq(trunc(big(two).pow(256)), two.pow(256));

    T six = fromInt(6);
    for(int i = 0; i <= maxWidth() * 32; i++)
      eq(trunc(big(six).pow(i)), six.pow(i));
  }

  @Test
  public void powConstrained() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      int exp = rnd.nextInt(300);
      eq(trunc(xb.pow(exp)), x.pow(exp));
    }
  }

  @Test(expected=NumberFormatException.class)
  public void fromStringNeg() { construct("-1", 10); }
  @Test(expected=NumberFormatException.class)
  public void fromStringEmpty() { construct("", 10); }
  @Test(expected=NumberFormatException.class)
  public void fromStringEmpty2() { construct("+", 10); }
  @Test(expected=NumberFormatException.class)
  public void fromStringInvalid() { construct("_", 10); }

  @Test
  public void fromStringInvariant() {
    assertEquals(zero,          construct("0",  2));
    assertEquals(one,           construct("1",  2));
    assertEquals(two,           construct("10", 2));
    assertEquals(fromInt(1024), construct("+001024", 10));
  }

  @Test
  public void fromString10() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle()) {
      assertEquals(x, construct(x.toString(), 10));
    }
  }

  @Test
  public void fromString() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      for(int radix = Character.MIN_RADIX; radix < Character.MAX_RADIX; radix++)
        assertEquals(x, construct(x.toString(radix), radix));
  }

  @Test
  public void fromStringTruncate() {
    for(int i = 0; i < SAMPLE_MED; i++) {
      BigInteger b = new BigInteger(32 * maxWidth() * 2, rnd);
      for(int radix = Character.MIN_RADIX; radix < Character.MAX_RADIX; radix++)
        eq(trunc(b), construct(b.toString(radix), radix));
    }
  }

  @Test
  public void toStringDefaultsRadix() {
    for(int i = -5; i < Character.MIN_RADIX; i++)
      assertEquals(x.toString(), x.toString(i));
    for(int i = 1; i < 10; i++)
      assertEquals(x.toString(), x.toString(Character.MAX_RADIX + i));
  }

  @Test
  public void toStringDefaultRadix() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      assertEquals(xb.toString(), x.toString());
  }

  @Test
  public void toStringRadix() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      for(int radix = Character.MIN_RADIX; radix < Character.MAX_RADIX; radix++)
        assertEquals(xb.toString(radix), x.toString(radix));
  }

  @Test(expected=ArithmeticException.class)
  public void divmodZero() {
    one.divmod(zero);
  }

  @Test(expected=ArithmeticException.class)
  public void divideZero() {
    one.divide(zero);
  }

  @Test(expected=ArithmeticException.class)
  public void modZero() {
    one.mod(zero);
  }

  public void divmodInvariant() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle()) {
        T[] qr = x.divmod(x);
        assertEquals(one,  qr[0]);
        assertEquals(zero, qr[1]);
    }
  }

  @Test
  public void divmod1w() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      BigInteger[] bqr = xb.divideAndRemainder(rand1wb);
      T[]          uqr = x.divmod(rand1w);
      eq(bqr[0], uqr[0]);
      eq(bqr[1], uqr[1]);

      eq(bqr[0], x.divide(rand1w));
    }
  }

  @Test
  public void divmod2w() {
    if(2 <= maxWidth())
      for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
        T              b = random(2);
        BigInteger[] bqr = xb.divideAndRemainder(big(b));
        T[]          uqr = x.divmod(b);
        eq(bqr[0], uqr[0]);
        eq(bqr[1], uqr[1]);

        eq(bqr[0], x.divide(b));
      }
  }

  @Test
  public void divmod() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      T              b = randomNonZero();
      BigInteger[] bqr = xb.divideAndRemainder(big(b));
      T[]          uqr = x.divmod(b);

      eq(bqr[0], uqr[0]);
      eq(bqr[1], uqr[1]);

      assertEquals(uqr[0], x.divide(b));
      assertEquals(uqr[1], x.mod(b));

      if(!x.equals(zero)) {
        bqr = big(b).divideAndRemainder(xb);
        uqr = b.divmod(x);

        eq(bqr[0], uqr[0]);
        eq(bqr[1], uqr[1]);

        assertEquals(uqr[0], b.divide(x));
        assertEquals(uqr[1], b.mod(x));
      }
    }
  }

  @Test
  public void divmodPowersOfTwo() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      for(int exp = 1; exp < maxWidth() * 32; exp++) {
        T              b = two.pow(exp);
        BigInteger[] bqr = xb.divideAndRemainder(big(b));
        T[]          uqr = x.divmod(b);

        eq(bqr[0], uqr[0]);
        eq(bqr[1], uqr[1]);
      }
  }

  @Test
  public void divmodTiny() {
    for(int a = 0; a < SAMPLE_SMALL; a++) {
      T av = fromInt(a);
      for(int b = 1; b < 25; b++) {
        T             bv = fromInt(b);
        BigInteger[] bqr = big(av).divideAndRemainder(big(bv));
        T[]          uqr = av.divmod(bv);

        eq(bqr[0], uqr[0]);
        eq(bqr[1], uqr[1]);
      }
    }
  }

  @Test
  public void divmodClose() {
    for(int i = 0; i < SAMPLE_SMALL; i++, cycle()) {
      T xc = x;

      for(int j = 0; j < 10 && !xc.isZero(); j++) {
        BigInteger[] bqr = xb.divideAndRemainder(big(xc));
        T[]          uqr = x.divmod(xc);

        eq(bqr[0], uqr[0]);
        eq(bqr[1], uqr[1]);

        xc = xc.dec();
      }
    }
  }

  @Test
  public void toFromBigInteger() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle()) {
      BigInteger b = new BigInteger(32 * maxWidth(), rnd);
      assertEquals(b, construct(b).toBigInteger());
    }
  }

  @Test
  public void compareTo() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle()) {
      assertEquals(x.compareTo(y), xb.compareTo(yb));
      assertEquals(y.compareTo(x), yb.compareTo(xb));
    }
  }

  @Test
  public void equalsInvariant() {
    assertEquals   (one,  one);
    assertEquals   (zero, zero);
    assertNotEquals(zero, one);
    assertNotEquals(one,  zero);
    assertEquals   (max,  max);
  }

  @Test
  public void equalsBigInteger() {
    assertTrue (one .equals(big(one)));
    assertFalse(zero.equals(big(one)));
    assertTrue (max .equals(big(max)));
    assertTrue (zero.equals(big(max.inc())));
    assertFalse(max .equals(big(one)));
  }

  @Test
  public void and() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      eq(xb.and(yb), x.and(y));
  }

  @Test
  public void or() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      eq(xb.or(yb), x.or(y));
  }

  @Test
  public void xor() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      eq(xb.xor(yb), x.xor(y));
  }

  @Test(expected=ArithmeticException.class)
  public void setBitNegative() {
    zero.setBit(-1);
  }

  @Test
  public void setBitInvariant() {
    assertEquals(one, zero.setBit(0));
    assertEquals(construct(new int[]{1, 0}), zero.setBit(32));
    assertEquals(zero, zero.setBit(maxWidth() * 32));
  }

  @Test
  public void setBit() {
    final int maxWidth = maxWidth();
    for(int i = 0; i < SAMPLE_SMALL; i++, cycle()) {
      for(int bit = 0; bit < maxWidth * 32; bit++)
        eq(xb.setBit(bit), x.setBit(bit));
      eq(trunc(xb.setBit(maxWidth * 32)), x.setBit(maxWidth * 32));
      eq(trunc(xb.setBit(maxWidth * 64)), x.setBit(maxWidth * 64));
    }
  }

  @Test(expected=ArithmeticException.class)
  public void clearBitNegative() {
    zero.clearBit(-1);
  }

  @Test
  public void clearBitInvariant() {
    assertEquals(zero, one .clearBit(0));
    assertEquals(zero, zero.clearBit(0));
    assertEquals(zero, two .clearBit(1));
    assertEquals(zero, construct(new int[]{1, 0}).clearBit(32));
    int[] a = new int[maxWidth()];
    a[0] = 1 << 31;
    assertEquals(zero, construct(a).clearBit((maxWidth() * 32) - 1));
  }

  @Test
  public void clearBit() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle()) {
      for(int bit = 0; bit < x.ints.length * 32; bit++)
        eq(xb.clearBit(bit), x.clearBit(bit));
      eq(xb.clearBit(x.ints.length * 32), x.clearBit(x.ints.length * 32));
      eq(xb.clearBit(x.ints.length * 64), x.clearBit(x.ints.length * 64));
    }
  }

  @Test(expected=ArithmeticException.class)
  public void flipBitNegative() {
    zero.flipBit(-1);
  }

  @Test
  public void flipBitInvariant() {
    assertEquals(one,  zero.flipBit(0));
    assertEquals(zero, one .flipBit(0));
    assertEquals(zero, two .flipBit(1));
    assertEquals(two,  zero.flipBit(1));
  }

  @Test
  public void flipBit() {
    int maxWidth = maxWidth();
    for(int i = 0; i < SAMPLE_SMALL; i++, cycle()) {
      for(int bit = 0; bit < maxWidth * 32; bit++)
        eq(xb.flipBit(bit), x.flipBit(bit));
      eq(trunc(xb.flipBit(maxWidth * 32)), x.flipBit(maxWidth * 32));
      eq(trunc(xb.flipBit(maxWidth * 64)), x.flipBit(maxWidth * 64));
    }
  }

  @Test(expected=ArithmeticException.class)
  public void tesBtitNegative() {
    zero.testBit(-1);
  }

  @Test
  public void testBit() {
    int maxWidth = maxWidth();
    for(int i = 0; i < SAMPLE_SMALL; i++, cycle()) {
      for(int bit = 0; bit < maxWidth * 32; bit++)
        assertEquals(xb.testBit(bit), x.testBit(bit));
      assertEquals(xb.testBit(maxWidth * 32), x.testBit(maxWidth * 32));
      assertEquals(xb.testBit(maxWidth * 64), x.testBit(maxWidth * 64));
    }
  }

  @Test
  public void add() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      eq(trunc(xb.add(yb)), x.add(y));
  }

  @Test
  public void addSubtractInvariant() {
    assertEquals(zero,  zero.subtract(zero));
    assertEquals(max,   zero.subtract(one));
    assertEquals(one,   one .subtract(zero));
    assertEquals(zero,  zero.add(zero));
    assertEquals(one,   zero.add(one));
    assertEquals(zero,  one .subtract(one));
    assertEquals(max,   one .subtract(one).subtract(one));
    assertEquals(max,   one .subtract(two));
    assertEquals(
      max.subtract(one),
      one.subtract(two).subtract(one));
    assertEquals(
      max.subtract(one),
      one.subtract(fromInt(3)));
      assertEquals(zero,  max .add(one));
  }

  @Test
  public void subtract() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      eq(trunc(xb.subtract(yb)), x.subtract(y));
      eq(trunc(yb.subtract(xb)), y.subtract(x));
    }
  }

  @Test
  public void incDecInvariant() {
    assertEquals(zero, max.inc());
    assertEquals(max,  zero.dec());
    assertEquals(construct(new int[]{1,   0}), construct(new int[]{-1}).inc());
    assertEquals(construct(new int[]{-1}),     construct(new int[]{1, 0}).dec());
    assertEquals(construct(new int[]{-2, -1}), construct(new int[]{-1, 0}).dec());
  }

  @Test
  public void inc() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      eq(trunc(xb.add(BigInteger.ONE)),        x.inc());
      eq(trunc(xb.add(BigInteger.valueOf(2))), x.inc().inc());
    }
  }

  @Test
  public void dec() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      eq(trunc(xb.subtract(BigInteger.ONE)),        x.dec());
      eq(trunc(xb.subtract(BigInteger.valueOf(2))), x.dec().dec());
    }
  }

  @Test
  public void toByteArray() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      byte[] exp = xb.toByteArray();
      assertArrayEquals(
        Arrays.stripLeadingZeroes(exp),
        x.toByteArray());
    }
  }

  @Test
  public void intValue() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      assertEquals(xb.intValue(), x.intValue());
  }

  @Test(expected=ArithmeticException.class)
  public void intValueExactThrows() {
    construct(-1).intValueExact();
  }

  @Test
  public void longValue() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      assertEquals(xb.longValue(), x.longValue());
  }

  @Test
  public void shortValue() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      assertEquals(xb.shortValue(), x.shortValue());
  }

  @Test
  public void byteValue() {
    for(int i = 0; i < SAMPLE_MED; i++, cycle())
      assertEquals(xb.byteValue(), x.byteValue());
  }

  @Test(expected=ArithmeticException.class)
  public void longValueExactThrows() {
    construct(new int[]{-1, -1, -1}).longValueExact();
  }

  @Test(expected=ArithmeticException.class)
  public void shortValueExactThrows() {
    construct(new int[]{Short.MAX_VALUE + 1}).shortValueExact();
  }

  @Test(expected=ArithmeticException.class)
  public void byteValueExactThrows() {
    construct(new int[]{Byte.MAX_VALUE + 1}).byteValueExact();
  }

  @Test
  public void doubleValue() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      assertEquals(xb.doubleValue(), x.doubleValue(), 0);
  }

  @Test
  public void floatValue() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      assertEquals(xb.floatValue(), x.floatValue(), 0);
  }

  @Test
  public void testHashCode() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle())
      assertEquals(xb.hashCode(), x.hashCode());
  }

  @Test
  public void minMax() {
    for(int i = 0; i < SAMPLE_BIG; i++, cycle()) {
      eq(xb.max(yb), x.max(y));
      eq(xb.min(yb), x.min(y));
    }
  }
}
