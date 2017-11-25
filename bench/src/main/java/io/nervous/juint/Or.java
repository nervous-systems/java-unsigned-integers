package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Or {
  @Benchmark
  public void or_halfHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].or(s.half[0]));
  }

  @Benchmark
  public void or_halfHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].or(s.half[0]));
  }

  @Benchmark
  public void or_maxHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.max.or(s.half[i]));
  }

  @Benchmark
  public void or_maxHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.max.or(s.half[i]));
  }

  @Benchmark
  public void or_maxMax(UIntState s, Blackhole b) {
    b.consume(s.max.or(s.max));
  }

  @Benchmark
  public void or_maxMax_ref(BigState s, Blackhole b) {
    b.consume(s.max.or(s.max));
  }

  @Benchmark
  public void or_max1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.or(s.onew[i]));
  }

  @Benchmark
  public void or_max1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.or(s.onew[i]));
  }

  @Benchmark
  public void or_half1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].or(s.onew[i]));
  }

  @Benchmark
  public void or_half1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].or(s.onew[i]));
  }

  @Benchmark
  public void or_1w1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].or(s.onew[0]));
  }

  @Benchmark
  public void or_1w1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].or(s.onew[0]));
  }
}
