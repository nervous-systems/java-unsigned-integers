(ns scratch
  (:import [org.openjdk.jol.info ClassLayout]))

(defn layout [inst]
  (println (.toPrintable (ClassLayout/parseClass (class inst)) inst)))
