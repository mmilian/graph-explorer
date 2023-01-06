(ns clj.main
  (:require [clojure.edn :as edn]))


(def raw-graph {:a 1})

(def content "(ns graph.data)\n (def graph ")

(spit "./src/graph/data.clj" (str content (pr-str raw-graph) ")"))


