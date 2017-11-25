package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Multiply {
  @Benchmark
  public void multiply_halfSquare(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].multiply(s.half[i]));
  }

  @Benchmark
  public void multiply_halfSquare_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].multiply(s.half[i]));
  }

  @Benchmark
  public void multiply_halfHalf(UIntState s, Blackhole b) {
    for(int i = 1; i < s.half.length; i++)
      b.consume(s.half[0].multiply(s.half[i]));
  }

  @Benchmark
  public void multiply_halfHalf_ref(BigState s, Blackhole b) {
    for(int i = 1; i < s.half.length; i++)
      b.consume(s.half[0].multiply(s.half[i]));
  }

  @Benchmark
  public void multiply_half2w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].multiply(s.twow[i]));
  }

  @Benchmark
  public void multiply_half2w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].multiply(s.twow[i]));
  }

  @Benchmark
  public void multiply_fullFull(UIntState s, Blackhole b) {
    for(int i = 0; i < s.full.length; i++)
      b.consume(s.full[0].multiply(s.full[i]));
  }

  @Benchmark
  public void multiply_fullFull_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.full.length; i++)
      b.consume(s.full[0].multiply(s.full[i]).and(s.max));
  }

  @Benchmark
  public void multiply_half1w(UIntState s, Blackhole b) {
  for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].multiply(s.onew[i]));
  }

  @Benchmark
  public void multiply_half1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].multiply(s.onew[i]));
  }

  @Benchmark
  public void multiply_2w1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].multiply(s.onew[i]));
  }

  @Benchmark
  public void multiply_2w1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].multiply(s.onew[i]));
  }

  @Benchmark
  public void multiply_2w2w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].multiply(s.twow[i]));
  }

  @Benchmark
  public void multiply_2w2w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].multiply(s.twow[i]));
  }
}
