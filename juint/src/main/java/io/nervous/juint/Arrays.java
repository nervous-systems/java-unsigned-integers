package io.nervous.juint;

import java.math.BigInteger;

import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.copyOf;

/**
 * These methods don't mutate their arguments or return arrays w/ leading zeroes.
 */
final class Arrays {
  static final long    LONG       = 0xffffffffL;
  static final int     MAX_CACHE  = 28;
  static final int[][] CACHE      = new int[MAX_CACHE][1];

  static {
    CACHE[0] = new int[0];
    for(int i = 1; i < MAX_CACHE; i++) {
      CACHE[i] = new int[]{i};
    }
  }

  static final int[] ZERO = CACHE[0];
  static final int[]  ONE = CACHE[1];
  static final int[]  TWO = CACHE[2];

  static int[] valueOf(final long v) {
    if(0 <= v && v < MAX_CACHE)
      return CACHE[(int)v];

    final int hi = (int)(v >>> 32);
    return hi == 0 ? new int[]{(int)v} : new int[]{hi, (int)v};
  }

  static int compare(final int[] ints, final int[] other) {
    final int len = ints.length;
    if(len < other.length)
      return -1;
    if(len > other.length)
      return 1;

    int cmp;
    for(int i = 0; i < len; i++)
      if(ints[i] != other[i])
        return Integer.compareUnsigned(ints[i], other[i]);

    return 0;
  }

  static int compare(final int[] ints, final BigInteger other, final int maxWidth) {
    final int il = bitLength(ints), bl = other.bitLength();
    if(il < bl)
      return -1;
    if(il > bl)
      return 1;

    return compare(ints, from(other, maxWidth));
  }

  static int[] stripLeadingZeroes(final int[] ints, int strip) {
    final int len = ints.length;

    for(; strip < len && ints[strip] == 0; strip++)
      ;
    return strip == 0 ? ints : copyOfRange(ints, strip, len);
  }

  static int[] stripLeadingZeroes(final int[] ints) {
    return stripLeadingZeroes(ints, 0);
  }

  static byte[] stripLeadingZeroes(final byte[] bs) {
    int strip;
    final int len = bs.length;

    for(strip = 0; strip < len && bs[strip] == 0; strip++)
      ;
    return strip == 0 ? bs : copyOfRange(bs, strip, len);
  }

  static int[] not(final int[] ints, final int[] maxValue) {
    int len = ints.length, maxWidth = maxValue.length;
    if(ints.length == 0)
      return maxValue;

    int start = 0;
    if(ints[0] == -1)
      for(start = 1; start < len && ints[start] == -1; start++)
        ;

    if(start == maxWidth)
      return ZERO;

    final int[] out = new int[len < maxWidth ? maxWidth : maxWidth - start];
    int leading     = maxWidth - len;

    java.util.Arrays.fill(out, 0, leading, -1);

    for(int i = out.length - 1; leading <= i; i--)
      out[i] = ~(ints[--len]);

    return out;
  }

  static int[] and(int[] longer, int[] shorter) {
    if(longer.length < shorter.length) {
      int[] tmp = longer; longer = shorter; shorter = tmp;
    }
    int shortlen = shorter.length;
    if(shortlen == 0)
      return ZERO;

    final int[] out = copyOf(shorter, shortlen);
    int longlen     = longer.length;

    while(0 < shortlen)
      out[--shortlen] &= longer[--longlen];

    return out[0] == 0 ? stripLeadingZeroes(out, 1) : out;
  }

  static int[] or(int[] longer, int[] shorter) {
    if(longer.length < shorter.length) {
      int[] tmp = longer; longer = shorter; shorter = tmp;
    }
    int longlen     = longer.length, shortlen = shorter.length;
    final int[] out = copyOf(longer, longlen);

    while(0 < shortlen)
      out[--longlen] |= shorter[--shortlen];

    return out;
  }

  static int[] xor(int[] longer, int[] shorter) {
    if(longer.length < shorter.length) {
      int[] tmp = longer; longer = shorter; shorter = tmp;
    }
    if(longer.length == 0)
      return ZERO;

    int longlen     = longer.length, shortlen = shorter.length;
    final int[] out = copyOf(longer, longlen);

    while(0 < shortlen)
      out[--longlen] ^= shorter[--shortlen];

    return out[0] == 0 ? stripLeadingZeroes(out, 1) : out;
  }

  static int[] setBit(final int[] a, final int bit) {
    final int i = bit >>> 5, alen = a.length;

    if(i <= alen - 1) {
      final int j = alen - i - 1, v = a[j] | 1 << (bit & 31);
      if(v == a[j])
        return a;
      final int[] out = copyOf(a, alen);
      out[j]          = v;
      return out;
    }

    final int[] out = new int[i + 1];
    System.arraycopy(a, 0, out, out.length - alen, alen);

    out[0] = 1 << (bit & 31);
    return out;
  }

  static int[] clearBit(final int[] a, final int bit) {
    final int alen = a.length, i = alen - (bit >>> 5) - 1;
    final int v    = a[i] & ~(1 << (bit & 31));

    if(v == a[i])
      return a;

    if(i != 0 || v != 0) {
      final int[] out = copyOf(a, alen);
      out[i]          = v;
      return out;
    }
    return stripLeadingZeroes(a, 1);
  }

  static int[] flipBit(final int[] a, final int bit) {
    final int i = bit >>> 5, alen = a.length;

    if(i < alen - 1) {
      final int[] out = copyOf(a, alen);
      final int j     = alen - i - 1;
      out[j]         ^= (1 << (bit & 31));
      return out;
    }

    final int[] out = new int[i + 1];
    System.arraycopy(a, 0, out, out.length - alen, alen);

    return (out[0] ^= (1 << (bit & 31))) == 0 ? stripLeadingZeroes(out, 1) : out;
  }

  static int[] lshift(final int[] a, final int n, final int maxWidth) {
    if(n == 0)
      return a;

    final int alen = a.length, ints = n >>> 5;
    if(alen == 0 || maxWidth < ints)
      return ZERO;

    final int bits = n & 0x1f;
    int outlen     = alen + ints;
    int ai         = 0;

    if(maxWidth < outlen) {
      ai     = outlen - maxWidth;
      outlen = maxWidth;

      while(ai < alen && a[ai] == 0) {
        ai++; outlen--;
      }
    }

    if(ai == alen)
      return ZERO;

    final int[] out;
    if(bits == 0) {
      out = new int[outlen];
      System.arraycopy(a, ai, out, 0, alen - ai);
      return out;
    }

    int outi = 0;
    final int invbits = 32 - bits, high = a[ai] >>> invbits;

    if(high != 0 && outlen < maxWidth) {
      out         = new int[outlen + 1];
      out[outi++] = high;
    } else
      out = new int[outlen];

    while (ai < alen - 1)
      out[outi++] = a[ai++] << bits | a[ai] >>> invbits;
    out[outi] = a[ai] << bits;

    return out[0] == 0 ? stripLeadingZeroes(out, 1) : out;
  }

  static int[] rshift(final int[] a, final int n, final int maxWidth) {
    final int alen = a.length, ints = n >>> 5;
    if(alen <= ints)
      return ZERO;

    final int bits = n & 0x1f, outlen = alen - ints;
    if(bits == 0)
      return copyOf(a, outlen);

    final int invbits = 32 - bits, high = a[0] >>> bits, out[];
    int outi = 0, ai = 0;

    if(high != 0) {
      out         = new int[outlen];
      out[outi++] = high;
    } else
      out = new int[outlen - 1];

    while (ai < outlen - 1)
      out[outi++] = (a[ai++] << invbits) | (a[ai] >>> bits);

    return out;
  }

  static int[] inc(final int[] a, final int maxWidth) {
    return inc(a, false, maxWidth);
  }

  static int[] inc(final int[] a, final boolean mutate, final int maxWidth) {
    final int len = a.length;
    if(len == 0)
      return ONE;

    int         last = len - 1;
    final int[] b    = mutate ? a : copyOf(a, len);

    while(0 <= last)
      if(++(b[last--]) != 0)
        return b;
    if(len == maxWidth)
      return stripLeadingZeroes(b);

    final int[] c = new int[len + 1];
    System.arraycopy(b, 0, c, 1, len);
    c[0] = 1;
    return c;
  }

  static int[] dec(final int[] a) {
    final int   len  = a.length;
    int         last = len - 1;
    final int[] b    = copyOf(a, len);

    int v;

    if((v = --(b[last])) != -1)
      return (last == 0 && v == 0) ? stripLeadingZeroes(b, 1) : b;

    while(0 <= --last && (v = --(b[last])) == -1)
      ;
    return (v == 0 && last == 0) ? stripLeadingZeroes(b, 1) : b;
  }

  static int[] add(int[] longer, int[] shorter, final int maxWidth) {
    if(longer.length < shorter.length) {
      int[] tmp = longer;
      longer    = shorter;
      shorter   = tmp;
    }
    int longi       = longer.length;
    int shorti      = shorter.length;
    final int[] out = copyOf(longer, longi);
    long sum        = 0;

    while (0 < shorti) {
      sum        = (out[--longi] & LONG) + (shorter[--shorti] & LONG) + (sum >>> 32);
      out[longi] = (int)sum;
    }

    boolean carry = sum >>> 32 != 0;
    while (carry && 0 < longi)
      carry = ++(out[--longi]) == 0;

    if(carry && (out.length < maxWidth || maxWidth == -1)) {
      int grown[] = new int[out.length + 1];
      grown[0]    = 1;

      System.arraycopy(out, 0, grown, 1, out.length);
      return grown;
    }

    return out[0] == 0 ? stripLeadingZeroes(out, 1) : out;
  }

  static int[] subgt(final int[] a, final int[] b, final int[] maxValue) {
    if(a.length == 0)
      return inc(not(b, maxValue), true, maxValue.length);
    return inc(not(sub(b, a), maxValue), true, maxValue.length);
  }

  static int[] sub(final int[] a, final int[] b) {
    int longi = a.length, shorti = b.length;
    if(shorti == 0)
      return a;
    final int[] out = copyOf(a, longi);
    long diff       = 0;

    while (0 < shorti) {
      diff       = (out[--longi] & LONG) - (b[--shorti] & LONG) + (diff >> 32);
      out[longi] = (int)diff;
    }

    if(diff >> 32 != 0)
      while (0 < longi && --(out[--longi]) == -1)
        ;

    return out[0] == 0 ? stripLeadingZeroes(out, 1) : out;
  }

  static int[] mulmod(int[] a, int[] b, final int[] c) {
    if(a.length < b.length) {
      int[] tmp = a; a = b; b = tmp;
    }
    if(b.length == 0)
      return ZERO;
    final int[] mul = mul(a, a.length, b, b.length);
    final int   cmp = compare(mul, c);
    return (cmp < 0 ? mul : (cmp == 0 ? ZERO : mod(mul, c)));
  }

  static int[] addmod(int[] a, int[] b, final int[] c) {
    if(a.length < b.length) {
      int[] tmp = a; a = b; b = tmp;
    }
    final int[] add = b.length == 0 ? a : add(a, b, -1);
    final int   cmp = compare(add, c);
    return (cmp < 0 ? add : (cmp == 0 ? ZERO : mod(add, c)));
  }

  static int[] multiply(int[] a, int[] b, final int maxWidth) {
    if(a.length < b.length) {
      int[] tmp = a; a = b; b = tmp;
    }
    final int alen = a.length, blen = b.length;

    if(blen == 1)
      return mul(a, alen, b[0], maxWidth);
    if(blen == 2)
      return mul(a, alen, b[0], b[1], maxWidth);

    final int outlen = alen + blen;
    if(maxWidth < outlen)
      return mul(a, alen, b, blen, maxWidth, outlen - maxWidth);
    return mul(a, alen, b, blen);
  }

  static int[] mul(int[] a, final int alen, int b, int maxWidth) {
    if(Integer.bitCount(b) == 1)
      return lshift(a, Integer.numberOfTrailingZeros(b), maxWidth);

    final int[] out = new int[alen == maxWidth ? maxWidth : (alen + 1)];

    long carry     = 0;
    final long bl  = b & LONG;

    for(int ai = alen - 1, outi = out.length - 1; 0 <= ai; ai--, outi--) {
      final long prod  = (a[ai] & LONG) * bl + carry;
      out[outi]        = (int)prod;
      carry            = prod >>> 32;
    }

    return ((alen != maxWidth && (out[0] = (int)carry) != 0) ? out :
            (out[0] == 0 ? stripLeadingZeroes(out, 1) :
             out));
  }

  static int[] mul(
    final int[] a, final int alen, final int hi, final int lo, final int maxWidth) {

    int outlen       = alen + 2;
    final long lhi   = hi & LONG;
    final long llo   = lo & LONG;
    final int[] out  = new int[outlen];
    int outi         = outlen - 1;
    long carry       = 0;

    for(int i = alen - 1; 0 <= i; i--) {
      long prod   = (a[i] & LONG) * llo + carry;
      out[outi--] = (int)prod;
      carry       = prod >>> 32;
    }
    out[outi] = (int)carry;

    carry     = 0;
    outi      = out.length - 2;

    for(int ai = alen - 1; 0 <= ai; ai--) {
      long prod   = (a[ai] & LONG) * lhi + (out[outi] & LONG) + carry;
      out[outi--] = (int)prod;
      carry       = prod >>> 32;
    }
    out[0] = (int)carry;

    if(outlen <= maxWidth)
      return carry == 0L ? stripLeadingZeroes(out, 1) : out;

    return stripLeadingZeroes(out, outlen - maxWidth);
  }

  static int[] mul(
    final int[] a, final int alen, final int[] b, final int blen) {

    final int outlen = alen + blen;
    final int astart = alen - 1, bstart = blen - 1;
    final int[] out  = new int[outlen];
    long carry       = 0;

    for(int bi = bstart, outi = outlen - 1; 0 <= bi; bi--, outi--) {
      final long prod = (b[bi] & LONG) * (a[astart] & LONG) + carry;
      out[outi]       = (int)prod;
      carry           = prod >>> 32;
    }
    out[astart] = (int)carry;

    for(int ai = astart - 1; 0 <= ai; ai--) {
      carry = 0;
      for(int bi = bstart, outi = bstart + ai + 1; 0 <= bi; bi--, outi--) {
        final long prod = (b[bi] & LONG) * (a[ai] & LONG) + (out[outi] & LONG) + carry;
        out[outi]       = (int)prod;
        carry           = prod >>> 32;
      }
      out[ai] = (int)carry;
    }

    return carry == 0L ? stripLeadingZeroes(out, 1) : out;
  }

  static int[] mul(
    final int[] a,    final int alen, final int[] b, final int blen,
    final int outlen, final int trunc) {

    final int astart = alen - 1, bstart = blen - 1;
    final int[] out  = new int[outlen];
    long carry = 0;

    for(int bi = bstart, outi = outlen - 1; 0 <= bi; bi--, outi--) {
      final long prod = (b[bi] & LONG) * (a[astart] & LONG) + carry;
      out[outi]       = (int)prod;
      carry           = prod >>> 32;
    }
    if(trunc <= astart)
      out[astart - trunc] = (int)carry;

    int outend = outlen - 2;
    for(int ai = astart - 1; 0 <= ai; ai--) {
      carry = 0;
      for(int bi = bstart, outi = outend--; 0 <= bi && 0 <= outi; bi--, outi--) {
        final long prod = (b[bi] & LONG) * (a[ai] & LONG) + (out[outi] & LONG) + carry;
        out[outi] = (int)prod;
        carry     = prod >>> 32;
      }
      if(trunc <= ai)
        out[ai - trunc] = (int)carry;
    }
    return stripLeadingZeroes(out);
  }

  static int bitLength(int a[]) {
    return (a.length == 0 ?
            0 :
            ((a.length - 1) * 32) + (32 - Integer.numberOfLeadingZeros(a[0])));
  }

  static int[] square(final int[] a, final int maxWidth) {
    final int alen = a.length, start;

    int outlen = alen << 1;
    if(maxWidth < outlen) {
      start  = (outlen - maxWidth) >>> 1;
      outlen = maxWidth;
    } else
      start  = 0;

    final int[] out  = new int[outlen];

    int last = 0;
    for(int ai = start, i = 0; ai < alen; ai++) {
      final long wl   = (a[ai] & LONG);
      final long prod = wl * wl;
      out[i++]        = (last << 31) | (int)(prod >>> 33);
      out[i++]        = (int)(prod >>> 1);
      last            = (int)prod;
    }

    for(int ai = alen, pos = 1; start < ai && pos < outlen; ai--, pos += 2) {
      long carry   = 0;
      int outi     = outlen - pos - 1;
      final long v = a[ai - 1] & LONG;

      for(int aj = ai - 2; 0 <= aj && 0 <= outi; aj--) {
        long prod   = (a[aj] & LONG) * v + (out[outi] & LONG) + carry;
        out[outi--] = (int)prod;
        carry       = prod >>> 32;
      }

      if(0 <= outi) {
        carry    += (out[outi] & LONG);
        out[outi] = (int)carry;
        if((carry >> 32) != 0)
          for(int tmp = ai - 1; 0 <= tmp  && 0 <= --outi && ++(out[outi]) == 0; tmp--)
            ;
      }
    }

    Division.lshunt(out, 1);
    out[outlen - 1] |= (a[alen - 1] & 1);
    return out[0] == 0 ? stripLeadingZeroes(out, 1) : out;
  }

  static int[] pow(int[] a, final int lo, int exp, final int maxWidth) {
    if(exp == 2)
      return square(a, maxWidth);

    long shift = (long)lo * exp;
    if(Integer.MAX_VALUE < shift)
      throw new ArithmeticException("Overflow");

    if(0 < lo)
      a = rshift(a, lo, maxWidth);

    final int bits = bitLength(a);
    if(bits == 1)
      return 0 < lo ? lshift(ONE, lo * exp, maxWidth) : ONE;

    long scale = (long)bits * exp;

    if(a.length == 1 && scale < 63) {
      long out = 1, base = a[0] & LONG;

      while(exp != 0) {
        if((exp & 1) == 1)
          out *= base;
        if((exp >>>= 1) != 0)
          base *= base;
      }

      if(0 < lo)
        return ((shift + scale) < 63 ?
                valueOf(out << shift) :
                lshift(valueOf(out), (int)shift, maxWidth));
      return valueOf(out);
    }

    final int lplaces = lo * exp;
    int[] out         = ONE;

    while(exp != 0) {
      if((exp & 1) == 1)
        out = multiply(out, a, maxWidth);
      if((exp >>>= 1) != 0)
        a = square(a, maxWidth);
    }

    return 0 < lplaces ? lshift(out, lplaces, maxWidth) : out;
  }

  static int[] divide(final int[] a, final int[] b) {
    final int[] q;

    switch(b.length) {
    case 1:
      q = Division.div(a, b[0])[0];
      break;
    case 2:
      final long divisor = ((b[0] & LONG) << 32) | (b[1] & LONG);
      q                  = Division.div(a, divisor)[0];
      break;
    default:
      q = Division.div(a, b)[0];
    }

    return q[0] == 0 ? stripLeadingZeroes(q) : q;
  }

  static int[] mod(final int[] a, final int[] b) {
    final int[] r;

    switch(b.length) {
    case 1:
      r = Division.div(a, b[0])[1];
      break;
    case 2:
      final long divisor = ((b[0] & LONG) << 32) | (b[1] & LONG);
      r                  = Division.div(a, divisor)[1];
      break;
    default:
      r = Division.div(a, b)[1];
    }

    return r[0] == 0 ? stripLeadingZeroes(r) : r;
  }

  static int[][] divmod(final int[] a, final long b) {
    final int[][] qr = Division.div(a, b);

    if(0 < qr[0].length && qr[0][0] == 0)
      qr[0] = stripLeadingZeroes(qr[0]);
    if(qr[1][0] == 0)
      qr[1] = stripLeadingZeroes(qr[1]);

    return qr;
  }

  static int[][] divmod(final int[] a, final int[] b) {
    final int[][] qr;

    switch(b.length) {
    case 1:
      qr = Division.div(a, b[0]);
      break;
    case 2:
      final long divisor = ((b[0] & LONG) << 32) | (b[1] & LONG);
      qr                 = Division.div(a, divisor);
      break;
    default:
      qr = Division.div(a, b);
    }

    if(0 < qr[0].length && qr[0][0] == 0)
      qr[0] = stripLeadingZeroes(qr[0]);
    if(qr[1][0] == 0)
      qr[1] = stripLeadingZeroes(qr[1]);

    return qr;
  }

  private static BigInteger BIG_INT = BigInteger.valueOf(LONG);

  static int[] from(BigInteger b, final int maxWidth) {
    int n            = Math.min((b.bitLength() >>> 5) + 1, maxWidth);
    final int[] ints = new int[n];
    while(0 < n) {
      ints[--n] = b.and(BIG_INT).intValue();
      b         = b.shiftRight(32);
    }
    return (0 < ints.length && ints[0] == 0) ? stripLeadingZeroes(ints) : ints;
  }

  static int[] from(final byte[] bytes, final int[] maxValue) {
    int len = bytes.length;

    if(len == 0)
      return ZERO;

    int skip;
    for (skip = 0; skip < len && bytes[skip] == 0; skip++)
      ;

    final int ints  = Math.min(maxValue.length, ((len - skip) + 3) >>> 2);
    final int[] out = new int[ints];
    int b = len - 1;
    for(int i = ints - 1; 0 <= i; i--) {
      out[i]   = bytes[b--] & 0xff;
      int copy = Math.min(3, b - skip + 1);
      for(int j = 8; j <= (copy << 3); j += 8)
        out[i] |= ((bytes[b--] & 0xff) << j);
    }
    return out;
  }

  static int[] maxValue(final int maxWidth) {
    final int[] max = new int[maxWidth];
    java.util.Arrays.fill(max, -1);
    return max;
  }
}
