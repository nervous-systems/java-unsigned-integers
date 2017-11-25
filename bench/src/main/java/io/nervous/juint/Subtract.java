package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Subtract {
  @Benchmark
  public void subtract_zeroMax(UIntState s, Blackhole b) {
    b.consume(s.zero.subtract(s.max));
  }

  @Benchmark
  public void subtract_zeroMax_ref(BigState s, Blackhole b) {
    b.consume(s.zero.subtract(s.max).and(s.max));
  }

  @Benchmark
  public void subtract_halfHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[0].subtract(s.half[i]));
  }

  @Benchmark
  public void subtract_halfHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[0].subtract(s.half[i]).and(s.max));
  }

  @Benchmark
  public void subtract_maxHalf(UIntState s, Blackhole b) {
    b.consume(s.max.subtract(s.half[0]));
  }

  @Benchmark
  public void subtract_maxHalf_ref(BigState s, Blackhole b) {
    b.consume(s.max.subtract(s.half[0]));
  }

  @Benchmark
  public void subtract_halfMax(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].subtract(s.max));
  }

  @Benchmark
  public void subtract_halfMax_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].subtract(s.max).and(s.max));
  }

  @Benchmark
  public void subtract_maxMax(UIntState s, Blackhole b) {
    b.consume(s.max.subtract(s.max));
  }

  @Benchmark
  public void subtract_maxMax_ref(BigState s, Blackhole b) {
    b.consume(s.max.subtract(s.max));
  }

  @Benchmark
  public void subtract_max1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.subtract(s.onew[i]));
  }

  @Benchmark
  public void subtract_max1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.subtract(s.onew[i]));
  }

  @Benchmark
  public void subtract_1wmax(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].subtract(s.max));
  }

  @Benchmark
  public void subtract_1wmax_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].subtract(s.max).and(s.max));
  }


  @Benchmark
  public void subtract_half1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].subtract(s.onew[i]));
  }

  @Benchmark
  public void subtract_half1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].subtract(s.onew[i]));
  }

  @Benchmark
  public void subtract_1w1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[0].subtract(s.onew[i]));
  }

  @Benchmark
  public void subtract_1w1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[0].subtract(s.onew[i]));
  }
}
