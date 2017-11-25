package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Misc {
  @Benchmark
  public void compareTo_full(UIntState s, Blackhole b) {
    for(int i = 0; i < s.full.length; i++)
      b.consume(s.full[0].compareTo(s.full[i]));
  }

  @Benchmark
  public void compareTo_full_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.full.length; i++)
      b.consume(s.full[0].compareTo(s.full[i]));
  }

  @Benchmark
  public void toString_max10(UIntState s, Blackhole b) {
    b.consume(s.max.toString());
  }

  @Benchmark
  public void toString_max10_ref(BigState s, Blackhole b) {
    b.consume(s.max.toString());
  }

  @Benchmark
  public void toString_max2(UIntState s, Blackhole b) {
    b.consume(s.max.toString(2));
  }

  @Benchmark
  public void toString_max2_ref(BigState s, Blackhole b) {
    b.consume(s.max.toString(2));
  }

  @Benchmark
  public void toString_half16(UIntState s, Blackhole b) {
    b.consume(s.half[0].toString(16));
  }

  @Benchmark
  public void toString_half6_ref(BigState s, Blackhole b) {
    b.consume(s.half[0].toString(16));
  }

  @Benchmark
  public void toString_1w10(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].toString());
  }

  @Benchmark
  public void toString_1w10_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].toString());
  }

  @Benchmark
  public void toString_2w10(UIntState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].toString());
  }

  @Benchmark
  public void toString_2w10_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].toString());
  }
}
