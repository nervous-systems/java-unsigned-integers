package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class And {
  @Benchmark
  public void and_halfHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[0].and(s.half[i]));
  }

  @Benchmark
  public void and_halfHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[0].and(s.half[i]));
  }

  @Benchmark
  public void and_maxHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].and(s.max));
  }

  @Benchmark
  public void and_maxHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].and(s.max));
  }

  @Benchmark
  public void and_maxMax(UIntState s, Blackhole b) {
    b.consume(s.max.and(s.max));
  }

  @Benchmark
  public void and_maxMax_ref(BigState s, Blackhole b) {
    b.consume(s.max.and(s.max));
  }

  @Benchmark
  public void and_max1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.and(s.onew[i]));
  }

  @Benchmark
  public void and_max1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.and(s.onew[i]));
  }

  @Benchmark
  public void and_half1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].and(s.onew[i]));
  }

  @Benchmark
  public void and_half1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].and(s.onew[i]));
  }

  @Benchmark
  public void and_1w1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[0].and(s.onew[i]));
  }

  @Benchmark
  public void and_1w1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[0].and(s.onew[i]));
  }
}
