package io.nervous.juint;

final class Division {
  private static long LONG = 0xffffffffL;

  static int[][] div(final int[] a, final int b) {
    final long bl = b & LONG;

    if(a.length == 1) {
      final long al = a[0] & LONG;
      return new int[][]{new int[]{(int)(al / bl)}, new int[]{(int)(al % bl)}};
    }

    final int[] quo  = java.util.Arrays.copyOf(a, a.length);
    final int places = Integer.numberOfLeadingZeros(b);
    int  rem         = a[0], alen = a.length;
    final long reml  = rem & LONG;

    if(reml < bl)
      quo[0] = 0;
    else {
      quo[0] = (int)(reml / bl);
      rem    = (int)(reml % bl);
    }

    while(0 < --alen) {
      final long est = ((rem & LONG) << 32) | (a[a.length - alen] & LONG);
      final int q;
      if(0 <= est) {
        q   = (int)(est / bl);
        rem = (int)(est % bl);
      } else {
        long tmp = divone(est, b & LONG);
        q   = (int)(tmp & LONG);
        rem = (int)(tmp >>> 32);
      }
      quo[a.length - alen] = q;
    }

    return new int[][]{quo, new int[]{0 < places ? rem % b : rem}};
  }

  static int[][] div(final int[] a, long b) {
    final int  alen = a.length;
    final int[] quo = new int[alen - 1], rem = new int[alen + 1];

    System.arraycopy(a, 0, rem, 1, alen);

    final int places = Long.numberOfLeadingZeros(b);
    if(0 < places) {
      lshunt(rem, places);
      b <<= places;
    }

    final int   dh = (int)(b >>> 32);
    final long dhl = dh & LONG;
    final int   dl = (int)(b & LONG);

    int qhat;
    for(int i = 0; i < alen - 1; i++)
      if((qhat = D3(i, rem, dh, dhl, dl)) != 0)
        quo[i] = D4_D5(i, rem, dh, dl, qhat);

    if(0 < places)
      rshift(rem, places);

    return new int[][]{quo, rem};
  }

  static int[][] div(final int[] a, final int[] b) {
    final int places = Integer.numberOfLeadingZeros(b[0]);
    final int[] div, rem;

    if(0 < places) {
      div = new int[b.length];
      copyshift(b, 0, div, 0, places);

      if(places <= Integer.numberOfLeadingZeros(a[0])) {
        rem = new int[a.length + 1];
        copyshift(a, 0, rem, 1, places);
      } else {
        rem            = new int[a.length + 2];
        final int invp = 32 - places;
        int c          = 0;
        for(int i = 0; i < a.length; i++)
          rem[i + 1] = (c << places) | ((c = a[i]) >>> invp);
        rem[a.length + 1] = c << places;
      }
    } else {
      div = b;
      rem = new int[a.length + 1];
      System.arraycopy(a, 0, rem, 1, a.length);
    }

    final int   qints = rem.length - b.length;
    final int[] quo   = new int[qints];

    final int  dh  = div[0];
    final int  dl  = div[1];
    final long dhl = dh & LONG;

    int qhat;
    for(int i = 0; i < qints; i++)
      if((qhat = D3(i, rem, dh, dhl, dl)) != 0)
        quo[i] = D4_D5(i, rem, div, qhat);

    if(0 < places)
      rshift(rem, places);

    return new int[][]{quo, rem};
  }

  private static int D3(final int j, final int[] rem, final int dh, final long dhl, final int dl) {
    int qhat, qrem;
    boolean correct = true;
    final int nh    = rem[j];
    final int nm    = rem[j + 1];

    if(nh == dh) {
      qhat    = ~0;
      qrem    = nh + nm;
      correct = nh + 0x80000000 <= qrem + 0x80000000;
    } else {
      final long chunk = (((long)nh) << 32) | (nm & LONG);
      if(0 <= chunk) {
        qhat = (int)(chunk / dhl);
        qrem = (int)(chunk - (qhat * dhl));
      } else {
        final long tmp = divone(chunk, dh & LONG);
        qhat = (int)(tmp & LONG);
        qrem = (int)(tmp >>> 32);
      }
    }

    if(qhat != 0 && correct) {
      final long nl = rem[j + 2] & LONG;
      long rs       = ((qrem & LONG) << 32) | nl;
      long est      =  (dl   & LONG) * (qhat & LONG);

      if(0 < Long.compareUnsigned(est, rs)) {
        qhat--;
        qrem = (int)((qrem & LONG) + dhl);
        if(dhl <= (qrem & LONG)) {
          est -= (dl    & LONG);
          rs   = ((qrem & LONG) << 32) | nl;
          if(0 < Long.compareUnsigned(est, rs))
            qhat--;
        }
      }
    }
    return qhat;
  }

  private static int D4_D5(
    final int i, final int[] rem, final int[] divisor, final int qhat) {

    final int tmp     = rem[i];
    rem[i]            = 0;
    final int borrow  = mulsub(rem, divisor, qhat & LONG, divisor.length, i);

    if(tmp + 0x80000000 < borrow + 0x80000000) {
      divadd(divisor, rem, i + 1);
      return qhat - 1;
    }
    return qhat;
  }

  private static int D4_D5(
    final int i, final int[] rem, final int dh, final int dl, final int qhat) {

    final int tmp    = rem[i];
    rem[i]           = 0;
    final int borrow = mulsub(rem, dh, dl, qhat & LONG, i);

    if(tmp + 0x80000000 < borrow + 0x80000000) {
      divadd(dh, dl, rem, i + 1);
      return qhat - 1;
    }
    return qhat;
  }

  private static int mulsub(
    final int[] q, final int[] a, final long x, final int len, int off) {

    long carry = 0;
    off       += len;

    for(int ai = len - 1; 0 <= ai; ai--) {
      long prod  = (a[ai] & LONG) * x + carry;
      long diff  = q[off] - prod;
      q[off--]   = (int)diff;
      carry      = (prod >>> 32) + (((((~(int)prod) & LONG)) < (diff & LONG)) ? 1 : 0);
    }
    return (int)carry;
  }

  private static int mulsub(
    final int[] q, final int dh, final int dl, final long x, final int off) {
    long prod   = (dl & LONG) * x;
    long diff   = q[off + 2] - prod;
    q[off + 2]  = (int)diff;
    long carry  = (prod >>> 32) + (((~(int)prod) & LONG) < (diff & LONG) ? 1 : 0);
    prod        = (dh & LONG) * x + carry;
    diff        = q[off + 1] - prod;
    q[off + 1]  = (int)diff;
    return (int)(prod >>> 32) + (((~(int)prod) & LONG) < (diff & LONG) ? 1 : 0);
  }

  private static int divadd(final int[] a, final int[] result, final int offset) {
    long carry = 0;

    for(int ai = a.length - 1; 0 <= ai; ai--) {
      long sum            = (a[ai] & LONG) + (result[ai + offset] & LONG) + carry;
      result[ai + offset] = (int)sum;
      carry               = sum >>> 32;
    }
    return (int)carry;
  }

  private static int divadd(final long dh, final long dl, final int[] result, final int off) {
    result[off + 1]  = (int)(dl + (result[off + 1] & LONG));
    final long sum   = dh + (result[off] & LONG);
    result[off]      = (int)sum;
    return (int)(sum >>> 32);
  }

  private static long divone(final long a, final long b) {
    if(b == 1)
      return (int)a;

    long quo = (a >>> 1) / (b >>> 1);
    long rem = a - quo * b;

    while(rem < 0) {
      rem += b;
      quo--;
    }

    while(b <= rem) {
      rem -= b;
      quo++;
    }

    return (rem << 32) | (quo & LONG);
  }

  static void lshunt(final int[] a, final int places) {
    final int invplaces = 32 - places;
    for(int ai = 0, lim = ai + a.length- 1; ai < lim; ai++)
      a[ai]  = (a[ai] << places) | (a[ai + 1] >>> invplaces);
    a[a.length - 1] <<= places;
  }

  private static void rshunt(final int[] a, final int places) {
    int invplaces = 32 - places;
    for(int ai = a.length - 1; 0 < ai; ai--)
      a[ai] = (a[ai - 1] << invplaces) | (a[ai] >>> places);
    a[0] >>>= places;
  }

  private static void rshift(final int[] a, final int places) {
    if(a.length == 0)
      return;
    final int bits = places & 0x1F;
    if(bits == 0)
      return;
    if(Integer.bitCount(a[0]) <= bits) {
      lshunt(a, 32 - bits);
      for(int ai = a.length - 1; 0 < ai; ai--)
        a[ai] = a[ai - 1];
      a[0] = 0;
    } else
      rshunt(a, bits);
  }

  private static void copyshift(
    final int[] src, int srci, final int[] dst, final int dsti, final int places) {
    final int invplaces = 32 - places;

    int carry = src[srci];
    for(int i = 0; i < src.length - 1; i++)
      dst[dsti + i] = (carry << places) | ((carry = src[++srci]) >>> invplaces);
    dst[dsti + src.length - 1] = carry << places;
  }
}
