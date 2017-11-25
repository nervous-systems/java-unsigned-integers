package io.nervous.juint;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Shift {
  @Benchmark
  public void shiftLeft_1wBy1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].shiftLeft(32));
  }

  @Benchmark
  public void shiftLeft_1wBy1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].shiftLeft(32));
  }

  @Benchmark
  public void shiftRight_1wBy1w(UIntState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].shiftRight(32));
  }

  @Benchmark
  public void shiftRight_1wBy1w_ref(BigState s, Blackhole b) {
    for(int i = 0; i < s.onew.length; i++)
      b.consume(s.onew[i].shiftRight(32));
  }

  @Benchmark
  public void shiftLeft_maxByHalf(UIntState s, Blackhole b) {
    b.consume(s.max.shiftLeft(128));
  }

  @Benchmark
  public void shiftLeft_maxByHalf_ref(BigState s, Blackhole b) {
    b.consume(s.max.shiftLeft(128));
  }

  @Benchmark
  public void shiftRight_maxByHalf(UIntState s, Blackhole b) {
    b.consume(s.max.shiftRight(128));
  }

  @Benchmark
  public void shiftRight_maxByHalf_ref(BigState s, Blackhole b) {
    b.consume(s.max.shiftRight(128));
  }

  @Benchmark
  public void shiftLeft_maxByAlmostHalf(UIntState s, Blackhole b) {
    b.consume(s.max.shiftLeft(97));
  }

  @Benchmark
  public void shiftLeft_maxByAlmostHalf_ref(BigState s, Blackhole b) {
    b.consume(s.max.shiftLeft(97));
  }

  @Benchmark
  public void shiftRight_maxByAlmostHalf(UIntState s, Blackhole b) {
    b.consume(s.max.shiftRight(97));
  }

  @Benchmark
  public void shiftRight_maxByAlmostHalf_ref(BigState s, Blackhole b) {
    b.consume(s.max.shiftRight(97));
  }
}
