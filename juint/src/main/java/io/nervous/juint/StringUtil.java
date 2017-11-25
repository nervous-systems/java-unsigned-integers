package io.nervous.juint;

import static io.nervous.juint.Arrays.LONG;

/**
 * These constants are basically ripped from OpenJDK.
 */
final class StringUtil {
  private static int[] BITS_PER_DIGIT = {
    1024, 1624, 2048, 2378, 2648, 2875, 3072, 3247, 3402, 3543, 3672,
    3790, 3899, 4001, 4096, 4186, 4271, 4350, 4426, 4498, 4567, 4633,
    4696, 4756, 4814, 4870, 4923, 4975, 5025, 5074, 5120, 5166, 5210,
    5253, 5295};

  private static int[] DIGITS_PER_INT = {
    30, 19, 15, 13, 11, 11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7,
    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5};

  private static int[] RADIX_LENGTH_LONG = {
    62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15,
    14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12};

  private static int[] RADIX_INT = {
    0x40000000, 0x4546b3db, 0x40000000, 0x48c27395, 0x159fd800, 0x75db9c97,
    0x40000000, 0x17179149, 0x3b9aca00, 0xcc6db61,  0x19a10000, 0x309f1021,
    0x57f6c100, 0xa2f1b6f,  0x10000000, 0x18754571, 0x247dbc80, 0x3547667b,
    0x4c4b4000, 0x6b5a6e1d, 0x6c20a40,  0x8d2d931,  0xb640000,  0xe8d4a51,
    0x1269ae40, 0x17179149, 0x1cb91000, 0x23744899, 0x2b73a840, 0x34e63b41,
    0x40000000, 0x4cfa3cc1, 0x5c13d840, 0x6d91b519, 0x39aa400};

  private static long[] RADIX_LONG = {
    0x4000000000000000L, 0x383d9170b85ff80bL, 0x4000000000000000L, 0x6765c793fa10079dL,
    0x41c21cb8e1000000L, 0x3642798750226111L, 0x1000000000000000L, 0x12bf307ae81ffd59L,
    0xde0b6b3a7640000L,  0x4d28cb56c33fa539L, 0x1eca170c00000000L, 0x780c7372621bd74dL,
    0x1e39a5057d810000L, 0x5b27ac993df97701L, 0x1000000000000000L, 0x27b95e997e21d9f1L,
    0x5da0e1e53c5c8000L, 0xb16a458ef403f19L,  0x16bcc41e90000000L, 0x2d04b7fdd9c0ef49L,
    0x5658597bcaa24000L, 0x6feb266931a75b7L,  0xc29e98000000000L,  0x14adf4b7320334b9L,
    0x226ed36478bfa000L, 0x383d9170b85ff80bL, 0x5a3c23e39c000000L, 0x4e900abb53e6b71L,
    0x7600ec618141000L,  0xaee5720ee830681L,  0x1000000000000000L, 0x172588ad4f5f0981L,
    0x211e44f7d02c1000L, 0x2ee56725f06e5c71L, 0x41c21cb8e1000000L};

  static String ZEROES = "000000000000000000000000000000000000000000000000000000000000000";

  static int[] fromString(final String s, final int radix, final int maxWidth) {
    if(radix < Character.MIN_RADIX || Character.MAX_RADIX < radix)
      throw new NumberFormatException("Radix out of range");

    if(-1 < s.lastIndexOf('-'))
      throw new NumberFormatException("Invalid sign");

    int pos         = 0;
    final int len   = s.length();
    final int signi = s.lastIndexOf('+');

    if(-1 < signi) {
      if(0 < signi)
        throw new NumberFormatException("Illegal embedded sign character");
      pos++;
    }

    if(len == pos)
      throw new NumberFormatException("Zero-length");

    while(pos < len && Character.digit(s.charAt(pos), radix) == 0)
      pos++;

    if(pos == len)
      return Arrays.ZERO;

    final int digits = len - pos, perint = DIGITS_PER_INT[radix - 2];
    final long bits  = ((digits * BITS_PER_DIGIT[radix - 2]) >>> 10) + 1;
    final int words  = Math.min((int)(bits + 31) >>> 5, maxWidth);
    final int[] ints = new int[words];

    int firstlen = digits % perint;
    if(firstlen == 0)
      firstlen = perint;

    String group = s.substring(pos, pos += firstlen);
    if((ints[words - 1] = Integer.parseInt(group, radix)) < 0)
      throw new NumberFormatException("Illegal digit");

    final int superradix = RADIX_INT[radix - 2];
    int groupv           = 0;
    while(pos < len) {
      group = s.substring(pos, pos += perint);
      if((groupv = Integer.parseInt(group, radix)) < 0)
        throw new NumberFormatException("Illegal digit");
      muladd(ints, superradix, groupv);
    }

    return Arrays.stripLeadingZeroes(ints);
  }

  private static void muladd(final int[] out, final int mul, final int add) {
    final long lmul = mul & LONG, ladd = add & LONG;
    final int len   = out.length;

    long carry = 0;
    for(int outi = len - 1; 0 <= outi; outi--) {
      final long prod = lmul * (out[outi] & LONG) + carry;
      out[outi]       = (int)prod;
      carry           = prod >>> 32;
    }

    long sum     = (out[len - 1] & LONG) + ladd;
    out[len - 1] = (int)sum;
    carry        = sum >>> 32;
    for(int outi = len - 2; carry != 0L && 0 <= outi; outi--) {
      sum        = (out[outi] & LONG) + carry;
      out[outi]  = (int)sum;
      carry      = sum >>> 32;
    }
  }

  static String toString(final int[] ints, final int radix) {
    final String[] groups = new String[(4 * ints.length + 6) / 7];
    final long divisor    = RADIX_LONG[radix - 2];

    int group = 0;
    int[] q   = ints, r = null;
    int[][] tmp;

    do {
      tmp = Arrays.divmod(q, divisor);
      q   = tmp[0];
      r   = tmp[1];
      if(r.length == 0)
        groups[group++] = "0";
      else {
        final long rl = r.length == 1 ?
          (r[0] & LONG) : ((r[0] & LONG) << 32) | (r[1] & LONG);
        groups[group++] = Long.toString(rl, radix);
      }
    } while(0 < q.length);

    final int rlen         = RADIX_LENGTH_LONG[radix - 2];
    final StringBuilder sb = new StringBuilder(group * rlen);
    sb.append(groups[group - 1]);

    int zeroes;
    for(int i = group - 2; 0 <= i; i--) {
      if((zeroes = rlen - groups[i].length()) != 0)
        sb.append(ZEROES.substring(0, zeroes));
      sb.append(groups[i]);
    }
    return sb.toString();
  }
}
