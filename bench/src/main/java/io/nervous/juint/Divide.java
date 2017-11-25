package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Divide {
  @Benchmark
  public void divmod_halfHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[0].divmod(s.half[i]));
  }

  @Benchmark
  public void divmod_halfHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[0].divideAndRemainder(s.half[i]));
  }

  @Benchmark
  public void divmod_maxHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.max.divmod(s.half[i]));
  }

  @Benchmark
  public void divmod_maxHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.max.divideAndRemainder(s.half[i]));
  }

  @Benchmark
  public void divmod_maxMax(UIntState s, Blackhole b) {
    b.consume(s.max.divmod(s.max.subtract(s.one)));
  }

  @Benchmark
  public void divmod_maxMax_ref(BigState s, Blackhole b) {
    b.consume(s.max.divideAndRemainder(s.max.subtract(s.one)));
  }

  @Benchmark
  public void divmod_max1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.divmod(s.onew[i]));
  }

  @Benchmark
  public void divmod_max1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.max.divideAndRemainder(s.onew[i]));
  }

  @Benchmark
  public void divmod_max2w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.max.divmod(s.twow[i]));
  }

  @Benchmark
  public void divmod_max2w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.max.divideAndRemainder(s.twow[i]));
  }

  @Benchmark
  public void divmod_half1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].divmod(s.onew[i]));
  }

  @Benchmark
  public void divmod_half1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].divideAndRemainder(s.onew[i]));
  }

  @Benchmark
  public void divmod_1w1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].divmod(s.onew[i].subtract(s.one)));
  }

  @Benchmark
  public void divmod_1w1_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].divideAndRemainder(s.onew[i].subtract(s.one)));
  }
}
