package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Not {
  @Benchmark
  public void not_max(UIntState s, Blackhole b) {
    b.consume(s.max.not());
  }

  @Benchmark
  public void not_max_ref(BigState s, Blackhole b) {
    b.consume(s.max.not());
  }

  @Benchmark
  public void not_zero(UIntState s, Blackhole b) {
    b.consume(s.zero.not());
  }

  @Benchmark
  public void not_zero_ref(BigState s, Blackhole b) {
    b.consume(s.zero.not());
  }

  @Benchmark
  public void not_half(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].not());
  }

  @Benchmark
  public void not_half_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].not());
  }

  @Benchmark
  public void not_1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].not());
  }

  @Benchmark
  public void not_1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].not());
  }

  @Benchmark
  public void not_2w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.twow[i].not());
  }

  @Benchmark
  public void not_2w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].not());
  }
}
