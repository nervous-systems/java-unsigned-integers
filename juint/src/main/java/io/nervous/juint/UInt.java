package io.nervous.juint;

import java.math.BigInteger;
import static io.nervous.juint.Arrays.LONG;

abstract class UInt<T extends UInt>
  extends    java.lang.Number
  implements Comparable<T> {

  final int[] ints;

  /* toString */
  static final int DEFAULT_RADIX = 10;

  UInt(final long l) {
    this.ints = Arrays.valueOf(l);
  }

  UInt(final int[] ints) {
    this.ints = ints;
  }

  UInt(final int[] ints, final int maxWidth) {
    this(Arrays.stripLeadingZeroes(ints, Math.max(0, ints.length - maxWidth)));
  }

  UInt(final UInt other, final int maxWidth) {
    this(other.ints, maxWidth);
  }

  UInt(final String s, final int radix, final int maxWidth) {
    this.ints = StringUtil.fromString(s, radix, maxWidth);
  }

  UInt(final BigInteger b, final int maxWidth) {
    this(Arrays.fromBigInteger(b, maxWidth), maxWidth);
  }

  /**
   * {@code this / other, this % other}
   */
  public abstract T[] divmod(T other);
  /**
   * {@code this / other}
   */
  public abstract T divide(T other);
  /**
   * {@code this % other}
   */
  public abstract T mod(T other);
  /**
   * {@code this * other}
   */
  public abstract T multiply(T other);
  /**
   * {@code this ** exp}
   */
  public abstract T pow(int exp);
  /**
   * {@code ~this}
   */
  public abstract T not();
  /**
   * {@code this & other}
   */
  public abstract T and(T other);
  /**
   * {@code this | other}
   */
  public abstract T or(T other);
  /**
   * {@code this ^ other}
   */
  public abstract T xor(T other);
  /**
   * {@code this + 1}
   */
  public abstract T inc();
  /**
   * {@code this - 1}
   */
  public abstract T dec();
  /**
   * {@code this + other}
   */
  public abstract T add(T other);
  /**
   * {@code this - other}
   */
  public abstract T subtract(T other);
  /**
   * {@code this << places}.
   *
   * Shifts right if {@code places} is negative.
   */
  public abstract T shiftLeft(int places);
  /**
   * {@code this >> places}.
   *
   * Shifts left if {@code places} is negative.
   */
  public abstract T shiftRight(int places);
  /**
   * {@code this | (1 << bit)}
   */
  public abstract T setBit(int bit);
  /**
   * {@code this & ~(1 << bit)}
   */
  public abstract T clearBit(int bit);
  /**
   * {@code this ^ (1 << bit)}
   */
  public abstract T flipBit(int bit);

  /**
   * {@code (this & (1 << bit)) != 0}
   */
  public final boolean testBit(final int bit) {
    if(bit < 0)
      throw new ArithmeticException("Negative bit address");
    final int i = bit >>> 5;
    return i < ints.length && 0 != (ints[ints.length - i - 1] & (1 << (bit & 31)));
  }

  /**
   * Alias for {@link #divmod}.
   */
  public final T[] divideAndRemainder(T other) { return divmod(other); }

  /**
   * Alias for {@link #mod}.
   */
  public final T remainder(T other) { return mod(other); };

  /**
   * Count the number of bits required to represent this number in binary.
   */
  public final int bitLength() {
    return Arrays.bitLength(ints);
  }

  /**
   * {@code this == 0}
   */
  public final boolean isZero() {
    return ints.length == 0;
  }

  /**
   * Return the index of the right-most set bit, or {@code -1}.
   */
  public final int getLowestSetBit() {
    final int start = ints.length - 1;
    for(int i = start; 0 <= i; i--)
      if(ints[i] != 0)
        return (start - i) * 32 + Integer.numberOfTrailingZeros(ints[i]);
    return -1;
  }

  /**
   * Return a hash code identical to the equivalent OpenJDK {@link BigInteger}.
   */
  public int hashCode() {
    int out = 0;

    for(int i = 0; i < ints.length; i++)
      out = (int)(31*out + (ints[i] & LONG));

    return out;
  }

  public boolean equals(final Object other) {
    if(other instanceof BigInteger)
      return Arrays.compare(ints, (BigInteger)other) == 0;
    if(other instanceof UInt)
      return Arrays.compare(ints, ((UInt)other).ints) == 0;
    return false;
  }

  public final int compareTo(final T other) {
    return Arrays.compare(ints, other.ints);
  }

  /**
   * {@code other < this ? this : other}
   */
  @SuppressWarnings("unchecked")
  public final T max(final T other) {
    return 0 < compareTo(other) ? ((T)this) : other;
  }

  /**
   * {@code this < other ? this : other }
   */
  @SuppressWarnings("unchecked")
  public final T min(final T other) {
    return compareTo(other) < 0 ? ((T)this) : other;
  }

  public final int intValue() {
    return ints.length == 0 ? 0 : ints[ints.length - 1];
  }

  public final long longValue() {
    final int len = ints.length;
    if(len == 0)
      return 0;
    final long out = ints[len - 1] & LONG;
    return ints.length == 1 ? out : ((ints[len - 2] & LONG) << 32 | out);
  }

  public final float floatValue() {
    /* Unless somebody desperately wants this to be as efficient as possible,
       I don't think it's worth spending time on - as with doubleValue. */
    return Float.parseFloat(toString());
  }

  public final double doubleValue() {
    return Double.parseDouble(toString());
  }

  public final int intValueExact() {
    if(ints.length <= 1 && bitLength() < 32)
      return intValue();
    throw new ArithmeticException("Out of int range");
  }

  public final long longValueExact() {
    if(ints.length <= 2 && bitLength() < 64)
      return longValue();
    throw new ArithmeticException("Out of long range");
  }

  public final short shortValueExact() {
    if(ints.length <= 1 && bitLength() < 32) {
      final int v = intValue();
      if(Short.MIN_VALUE <= v && v <= Short.MAX_VALUE)
        return shortValue();
    }
    throw new ArithmeticException("Out of short range");
  }

  public final byte byteValueExact() {
    if(ints.length <= 1 && bitLength() < 32) {
      final int v = intValue();
      if(Byte.MIN_VALUE <= v && v <= Byte.MAX_VALUE)
        return byteValue();
    }
    throw new ArithmeticException("Out of byte range");
  }

  public final BigInteger toBigInteger() {
    BigInteger out = BigInteger.ZERO;
    for(int i = 0; i < ints.length; i++) {
      out = out.shiftLeft(32).or(BigInteger.valueOf(ints[i] & LONG));
    }
    return out;
  }

  /**
   * Return a big-endian byte array.
   */
  public final byte[] toByteArray() {
    final int bytes  = (int)Math.ceil(bitLength() / 8.0);
    final byte[] out = new byte[bytes];

    int intsi = ints.length - 1, v = 0;
    for(int outi = bytes - 1, copied = 0; 0 <= outi; outi--, copied++)
      out[outi] = (byte)(v = (copied % 4 == 0) ? ints[intsi--] : v >>> 8);
    return out;
  }

  /**
   * Return a big-endian int array.
   */
  public final int[] toIntArray() {
    return java.util.Arrays.copyOf(ints, ints.length);
  }

 /**
  * Decimal string representation.
  */
  public final String toString() {
    return toString(DEFAULT_RADIX);
  }

  /**
   * String representation in the given radix.
   *
   * {@code radix} values outside {@link Character.MIN_RADIX} and
   * {@link Character.MAX_RADIX} are substituted with {@code 10}.
   */
  public final String toString(int radix) {
    if(isZero())
      return "0";

    if(radix < Character.MIN_RADIX || Character.MAX_RADIX < radix)
      radix = DEFAULT_RADIX;

    if(ints.length == 1)
      return Integer.toUnsignedString(ints[0], radix);
    if(ints.length == 2)
      return Long.toUnsignedString(((ints[0] & LONG) << 32) | (ints[1] & LONG), radix);

    return StringUtil.toString(ints, radix);
  }
}
