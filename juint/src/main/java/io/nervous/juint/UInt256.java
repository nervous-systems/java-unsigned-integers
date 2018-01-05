package io.nervous.juint;

import java.math.BigInteger;

/**
 * Represents unsigned values less than {@code 2**256}.
 *
 * As indicated by the type signatures, arithmetic operations are not applicable
 * to types of other widths in this package.  Copy constructors can be used to
 * explicitly promote or truncate values for the purposes of interoperability.
 */
public final class UInt256 extends UInt<UInt256> {
  static final int MAX_WIDTH = 8;

  /**
   * Maximum representable value.
   */
  public static UInt256 MAX_VALUE = new UInt256(Arrays.maxValue(MAX_WIDTH));

  public static UInt256 ZERO = new UInt256(Arrays.ZERO);
  public static UInt256 ONE  = new UInt256(Arrays.ONE);
  public static UInt256 TWO  = new UInt256(Arrays.TWO);

  /**
   * Construct from a big-endian {@code int} array.
   *
   * If {@code ints} exceeds {@link MAX_VALUE}, only the maximum prefix
   * will be considered.  Leaves {@code ints} untouched.
   */
  public UInt256(final int[] ints) {
    super(ints, MAX_WIDTH);
  }

  /**
   * Construct from a big-endian {@code byte} array.
   *
   * If {@code bytes} exceeds {@link MAX_VALUE}, only the maximum prefix
   * will be considered.  Leaves {@code bytes} untouched.
   */
  public UInt256(final byte[] bytes) {
    super(bytes, MAX_VALUE);
  }

  /**
   * Construct from a {@link UInt128}.
   */
  public UInt256(final UInt128 other) {
    super(other, MAX_WIDTH);
  }

  /**
   * Construct from a base ten string.
   *
   * Excessively wide numbers will be truncated.
   *
   * @throws NumberFormatException Negative, invalid or zero-length number.
   */
  public UInt256(final String s) {
    this(s, 10);
  }

  /**
   * Construct from a string in the given radix.
   *
   * Excessively wide numbers will be truncated.
   *
   * @throws NumberFormatException Negative, invalid or zero-length number.
   */
  public UInt256(final String s, final int radix) {
    super(s, radix, MAX_WIDTH);
  }

  /**
   * Construct from a {@link BigInteger}.
   *
   * If {@code b} exceeds {@link MAX_VALUE}, it's truncated.
   */
  public UInt256(final BigInteger b) { super(b, MAX_WIDTH); }

  /**
   * Construct from a {@code long}, when considered unsigned.
   *
   * For low values of {@code v}, an array cache may be used.
   */
  public UInt256(final long v) { super(v); }

  public UInt256 not() {
    return new UInt256(Arrays.not(ints, MAX_VALUE.ints));
  }

  public UInt256 and(final UInt256 other) {
    return new UInt256(Arrays.and(ints, other.ints));
  }

  public UInt256 or(final UInt256 other) {
    return new UInt256(Arrays.or(ints, other.ints));
  }

  public UInt256 xor(final UInt256 other) {
    return new UInt256(Arrays.xor(ints, other.ints));
  }

  public UInt256 setBit(final int bit) {
    if(bit < 0)
      throw new ArithmeticException("Negative bit address");
    return ((MAX_WIDTH <= bit >>> 5) ? this :
            new UInt256(Arrays.setBit(ints, bit)));
  }

  public UInt256 clearBit(final int bit) {
    if(bit < 0)
      throw new ArithmeticException("Negative bit address");
    return ((ints.length <= bit >>> 5) ? this :
            new UInt256(Arrays.clearBit(ints, bit)));
  }

  public UInt256 flipBit(final int bit) {
     if(bit < 0)
       throw new ArithmeticException("Negative bit address");
     return ((MAX_WIDTH <= bit >>> 5) ? this :
             new UInt256(Arrays.flipBit(ints, bit)));
  }

  public UInt256 shiftLeft(final int places) {
    return new UInt256(
      0 < places ?
      Arrays.lshift(ints,  places, MAX_WIDTH) :
      Arrays.rshift(ints, -places, MAX_WIDTH));
  }

  public UInt256 shiftRight(final int places) {
    return new UInt256(
      0 < places ?
      Arrays.rshift(ints,  places, MAX_WIDTH) :
      Arrays.lshift(ints, -places, MAX_WIDTH));
  }

  public UInt256 inc() {
    return new UInt256(Arrays.inc(ints, MAX_WIDTH));
  }

  public UInt256 dec() {
    return isZero() ? MAX_VALUE : new UInt256(Arrays.dec(ints));
  }

  public UInt256 add(final UInt256 other) {
    return (isZero() ? other :
            (other.isZero() ? this :
             new UInt256(Arrays.add(ints, other.ints, MAX_WIDTH))));
  }

  public UInt256 addmod(final UInt256 add, final UInt256 mod) {
    if(mod.isZero())
      throw new ArithmeticException("div/mod by zero");
    if(isZero() && add.isZero())
      return ZERO;
    return new UInt256(Arrays.addmod(ints, add.ints, mod.ints));
  }

  public UInt256 subtract(final UInt256 other) {
    if(other.isZero())
      return this;
    final int cmp = compareTo(other);
    return (cmp == 0 ? ZERO :
            new UInt256(
              cmp < 0 ?
              Arrays.subgt(ints, other.ints, MAX_VALUE.ints) :
              Arrays.sub  (ints, other.ints)));
  }

  public UInt256 multiply(final UInt256 other) {
    if(ints.length == 0 || other.ints.length == 0)
      return ZERO;
    return new UInt256(Arrays.multiply(ints, other.ints, MAX_WIDTH));
  }

  public UInt256 mulmod(final UInt256 mul, final UInt256 mod) {
    if(mod.isZero())
      throw new ArithmeticException("div/mod by zero");
    return new UInt256(Arrays.mulmod(ints, mul.ints, mod.ints));
  }

  public UInt256 pow(final int exp) {
    if(exp < 0)
      throw new ArithmeticException("Negative exponent");
    if(exp == 0)
      return ONE;
    if(isZero())
      return this;
    return (exp == 1 ? this :
            new UInt256(Arrays.pow(ints, getLowestSetBit(), exp, MAX_WIDTH)));
  }

  public UInt256 divide(final UInt256 other) {
    if(other.isZero())
      throw new ArithmeticException("div/mod by zero");
    if(isZero())
      return ZERO;
    final int cmp = compareTo(other);
    return (cmp  <  0 ? ZERO :
            (cmp == 0 ? ONE  :
             new UInt256(Arrays.divide(ints, other.ints))));
  }

  public UInt256 mod(final UInt256 other) {
    if(other.isZero())
      throw new ArithmeticException("div/mod by zero");
    if(isZero())
      return ZERO;
    final int cmp = compareTo(other);
    return (cmp  <  0 ? this :
            (cmp == 0 ? ZERO :
             new UInt256(Arrays.mod(ints, other.ints))));
  }

  public UInt256[] divmod(final UInt256 other) {
    if(other.isZero())
      throw new ArithmeticException("div/mod by zero");
    if(isZero())
      return new UInt256[]{ZERO, ZERO};
    final int cmp = compareTo(other);
    if(cmp < 0)
      return new UInt256[]{ZERO, this};
    if(cmp == 0)
      return new UInt256[]{ONE, ZERO};

    final int[][] qr = Arrays.divmod(ints, other.ints);
    return new UInt256[]{new UInt256(qr[0]), new UInt256(qr[1])};
  }

  public boolean equals(final Object other) {
    if(other instanceof BigInteger)
      return Arrays.compare(ints, (BigInteger)other, MAX_WIDTH) == 0;
    return super.equals(other);
  }
}
