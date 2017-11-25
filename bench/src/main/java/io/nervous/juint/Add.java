package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Add {
  @Benchmark
  public void add_halfHalf(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].add(s.half[0]));
  }

  @Benchmark
  public void add_halfHalf_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].add(s.half[0]));
  }

  @Benchmark
  public void add_maxMax(UIntState s, Blackhole b) {
    b.consume(s.max.add(s.max));
  }

  @Benchmark
  public void add_maxMax_ref(BigState s, Blackhole b) {
    b.consume(s.max.add(s.max).and(s.max));
  }

  @Benchmark
  public void add_max1w(UIntState s, Blackhole b) {
    b.consume(s.max.add(s.onew[0]));
  }

  @Benchmark
  public void add_max1w_ref(BigState s, Blackhole b) {
    b.consume(s.max.add(s.onew[0]).and(s.max));
  }

  @Benchmark
  public void add_half1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.half[i].add(s.onew[i]));
  }

  @Benchmark
  public void add_half1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.half[i].add(s.onew[i]));
  }

  @Benchmark
  public void add_1w1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].add(s.onew[0]));
  }

  @Benchmark
  public void add_1w1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].add(s.onew[0]));
  }
}
