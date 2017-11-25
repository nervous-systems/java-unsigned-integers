package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Pow {
  @Benchmark
  public void pow_1w256(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].pow(256));
  }

  @Benchmark
  public void pow_1w256_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].pow(256).and(s.max));
  }

  @Benchmark
  public void pow_1w16(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].pow(16));
  }

  @Benchmark
  public void pow_1w16_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].pow(16));
  }

  @Benchmark
  public void pow_2w8(UIntState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].pow(8));
  }

  @Benchmark
  public void pow_2w8_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].pow(8));
  }

  @Benchmark
  public void pow_2w17(UIntState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].pow(17));
  }

  @Benchmark
  public void pow_2w17_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.twow.length; i++)
      b.consume(s.twow[i].pow(17));
  }

  @Benchmark
  public void pow_half2(UIntState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].pow(2));
  }

  @Benchmark
  public void pow_half2_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.half.length; i++)
      b.consume(s.half[i].pow(2));
  }
}
