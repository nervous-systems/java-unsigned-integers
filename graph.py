#!/usr/bin/env python3
import csv, sys
from decimal import Decimal

def avg(d):
  return {k: sum(v)/len(v) for (k, v) in d.items()}

def calculate(path):
  scores = {}
  refs   = {}

  with open(path, 'r') as f:
    reader = csv.reader(f)
    next(reader)
    for row in reader:
      segments = tuple(row[0].split('.')[-1].split('_'))
      d        = refs if segments[-1] == 'ref' else scores
      d.setdefault(segments[0], []).append(Decimal(row[-3]))

  scores = avg(scores)
  refs   = avg(refs)

  for (meth, mean) in scores.items():
    change = (mean - refs[meth]) / refs[meth] * 100
    yield (meth, change)

if __name__ == '__main__':
  import numpy as np
  import matplotlib.pyplot as plt
  from matplotlib import ticker, rc
  import pprint, itertools

  data = {sys.argv[i]: sorted(calculate(sys.argv[i+1]), key=lambda t: t[0])
          for i in range(1, len(sys.argv), 2)}

  mmax = float(max(list(zip(*itertools.chain(*data.values())))[1]))
  mmin = float(min(list(zip(*itertools.chain(*data.values())))[1]))

  pprint.pprint(data)

  N = len(list(data.values())[0])

  ind    = np.arange(N)
  width  = 0.35
  colors = itertools.cycle(('#9edeff', '#ff9ede'))
  FONT   = 'Droid Sans Mono'

  rc('font', family=FONT)

  (fig, ax) = plt.subplots()

  ax.tick_params(labeltop='off', top='off', bottom='on')

  rects = []
  for (i, (tag, xs)) in enumerate(sorted(data.items(), key=lambda t: t[0])):
    color = next(colors)
    r     = ax.bar(ind + (i * width), list(zip(*xs))[1], width, color=color)
    rects.append((r, tag))

  ax.set_title("UInt256 Throughput Increase\nvs. Tuweni's UInt256")
  labels = list(zip(*list(data.values())[0]))[0]
  ax.set_xticks(np.arange(len(labels)), top=False)
  ax.set_xticklabels(labels, rotation=90)
  ax.tick_params(axis='x', top=False, labeltop=False)

  def yformat(v, pos):
    return '%s%d%% ' % ('+' if 0 < v else '', v)

  ax.yaxis.set_major_formatter(ticker.FuncFormatter(yformat))
  ax.spines[['top']].set_visible(False)

  #ax.legend(
  #  *zip(*tuple((r[0], l) for (r, l) in rects)),
  #  frameon=False)

  ax.axhline(0, color='k')

  plt.tight_layout()
  plt.savefig('out.png', transparent=True)
