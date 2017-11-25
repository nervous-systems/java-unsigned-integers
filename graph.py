#!/usr/bin/env python
import csv, sys
from decimal import Decimal

def avg(d):
  return {k: sum(v)/len(v) for (k, v) in d.iteritems()}

def calculate(path):
  scores = {}
  refs   = {}

  with open(path, 'rb') as f:
    reader = csv.reader(f)
    next(reader)
    for row in reader:
      segments = tuple(row[0].split('.')[-1].split('_'))
      d        = refs if segments[-1] == 'ref' else scores
      d.setdefault(segments[0], []).append(Decimal(row[-3]))

  scores = avg(scores)
  refs   = avg(refs)

  for (meth, mean) in scores.iteritems():
    change = (mean - refs[meth]) / refs[meth] * 100
    yield (meth, change)

if __name__ == '__main__':
  import numpy as np
  import matplotlib.pyplot as plt
  from matplotlib import ticker, rc
  import pprint, itertools

  data = {sys.argv[i]: sorted(calculate(sys.argv[i+1]), key=lambda t: t[0])
          for i in xrange(1, len(sys.argv), 2)}

  mmax = float(max(zip(*itertools.chain(*data.itervalues()))[1]))
  mmin = float(min(zip(*itertools.chain(*data.itervalues()))[1]))

  pprint.pprint(data)

  N = len(next(data.itervalues()))

  ind    = np.arange(N)
  width  = 0.35
  colors = itertools.cycle(('#9edeff', '#ff9ede'))
  FONT   = 'Droid Sans Mono'

  rc('font', family=FONT)

  (fig, (axhi, axlo)) = plt.subplots(2, 1, sharex=True)

  for ax in (axhi, axlo):
    ax.spines['bottom'].set_visible(False)
    ax.spines['top']   .set_visible(False)

  axhi.tick_params(labeltop='off', top='off', bottom='off')

  axlo.set_ylim((mmin, 190))
  axhi.set_ylim((350,  mmax))

  rects = []
  for (i, (tag, xs)) in enumerate(sorted(data.iteritems(), key=lambda t: t[0])):
    color = next(colors)
    r     = axlo.bar(ind + (i * width), zip(*xs)[1], width, color=color)
    axhi.bar(ind + (i * width), zip(*xs)[1], width, color=color)
    rects.append((r, tag))

  axhi.set_title("UInt256 Throughput Increase\nvs. OpenJDK's BigInteger")
  axlo.set_xticks(ind + width / 2)
  axlo.set_xticklabels(zip(*next(data.itervalues()))[0], rotation=90)

  def yformat(v, pos):
    return '%s%d%% ' % ('+' if 0 < v else '', v)

  for ax in (axlo, axhi):
    ax.yaxis.set_major_formatter(ticker.FuncFormatter(yformat))
    ax.tick_params(axis='y', left='off')

  axlo.tick_params(axis='x', bottom='off')
  axhi.legend(
    *zip(*tuple((r[0], l) for (r, l) in rects)),
    frameon=False)

  axlo.axhline(0, color='k')
  axlo.axhline(190, linestyle=':', color='silver')
  axhi.axhline(350, linestyle=':', color='silver')

  d  = .015
  kw = dict(transform=axhi.transAxes, color='k', clip_on=False)
  axhi.plot((-d, +d),       (-d, +d), **kw)
  axhi.plot((1 - d, 1 + d), (-d, +d), **kw)

  kw.update(transform=axlo.transAxes)
  axlo.plot((-d, +d),       (1 - d, 1 + d), **kw)
  axlo.plot((1 - d, 1 + d), (1 - d, 1 + d), **kw)

  plt.tight_layout()
  plt.savefig('out.png', transparent=True)
