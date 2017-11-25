package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Bits {
  @Benchmark
  public void testBit_max(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.max.testBit(i));
  }

  @Benchmark
  public void testBit_max_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.max.testBit(i));
  }

  @Benchmark
  public void testBit_half(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.half[0].testBit(i));
  }

  @Benchmark
  public void testBit_half_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.half[0].testBit(i));
  }

  @Benchmark
  public void testBit_zero(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.zero.testBit(i));
  }

  @Benchmark
  public void testBit_zero_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.zero.testBit(i));
  }

  @Benchmark
  public void flipBit_max(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.max.flipBit(i));
  }

  @Benchmark
  public void flipBit_max_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.max.flipBit(i));
  }

  @Benchmark
  public void flipBit_half(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.half[0].flipBit(i));
  }

  @Benchmark
  public void flipBit_half_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.half[0].flipBit(i));
  }

  @Benchmark
  public void flipBit_zero(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.zero.flipBit(i));
  }

  @Benchmark
  public void flipBit_zero_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.zero.flipBit(i));
  }

  @Benchmark
  public void setBit_halfAll(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.half[0].setBit(i));
  }

  @Benchmark
  public void setBit_halfAll_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.half[0].setBit(i));
  }

  @Benchmark
  public void setBit_zero(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.zero.setBit(i));
  }

  @Benchmark
  public void setBit_zero_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.zero.setBit(i));
  }

  @Benchmark
  public void clearBit_max(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.max.clearBit(i));
  }

  @Benchmark
  public void clearBit_max_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.max.clearBit(i));
  }

  @Benchmark
  public void clearBit_zero(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.zero.clearBit(i));
  }

  @Benchmark
  public void clearBit_zero_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.zero.clearBit(i));
  }

  @Benchmark
  public void clearBit_half(UIntState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.half[0].clearBit(i));
  }

  @Benchmark
  public void clearBit_half_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.bits; i++)
      b.consume(s.half[0].clearBit(i));
  }
}
