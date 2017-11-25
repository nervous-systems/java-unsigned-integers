package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Xor {
  @Benchmark
  public void xor_halfHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].xor(s.half[0]));
  }

  @Benchmark
  public void xor_halfHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].xor(s.half[0]));
  }

  @Benchmark
  public void xor_maxHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.max.xor(s.half[i]));
  }

  @Benchmark
  public void xor_maxHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.max.xor(s.half[i]));
  }

  @Benchmark
  public void xor_maxMax(UIntState s, Blackhole b) {
    b.consume(s.max.xor(s.max));
  }

  @Benchmark
  public void xor_maxMax_ref(BigState s, Blackhole b) {
    b.consume(s.max.xor(s.max));
  }

  @Benchmark
  public void xor_max1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.xor(s.onew[i]));
  }

  @Benchmark
  public void xor_max1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.xor(s.onew[i]));
  }

  @Benchmark
  public void xor_half1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].xor(s.onew[i]));
  }

  @Benchmark
  public void xor_half1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].xor(s.onew[i]));
  }

  @Benchmark
  public void xor_1w1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].xor(s.onew[0]));
  }

  @Benchmark
  public void xor_1w1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].xor(s.onew[0]));
  }
}
