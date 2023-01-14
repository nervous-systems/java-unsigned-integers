package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Divide {
  @Benchmark
  public void divide_halfHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[0].divide(s.half[i]));
  }

  @Benchmark
  public void divide_halfHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[0].divide(s.half[i]));
  }

  @Benchmark
  public void divide_maxHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.max.divide(s.half[i]));
  }

  @Benchmark
  public void divide_maxHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.max.divide(s.half[i]));
  }

  @Benchmark
  public void divide_maxMax(UIntState s, Blackhole b) {
    b.consume(s.max.divide(s.max.subtract(s.one)));
  }

  @Benchmark
  public void divide_maxMax_ref(BigState s, Blackhole b) {
    b.consume(s.max.divide(s.max.subtract(s.one)));
  }

  @Benchmark
  public void divide_max1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.divide(s.onew[i]));
  }

  @Benchmark
  public void divide_max1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.divide(s.onew[i]));
  }

  @Benchmark
  public void divide_max2w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.max.divide(s.twow[i]));
  }

  @Benchmark
  public void divide_max2w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.max.divide(s.twow[i]));
  }

  @Benchmark
  public void divide_half1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].divide(s.onew[i]));
  }

  @Benchmark
  public void divide_half1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].divide(s.onew[i]));
  }

  @Benchmark
  public void divide_1w1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].divide(s.onew[i].subtract(s.one)));
  }

  @Benchmark
  public void divide_1w1_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].divide(s.onew[i].subtract(s.one)));
  }
}
