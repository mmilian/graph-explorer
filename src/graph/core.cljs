(ns graph.core
  (:require [graph.components.graph :refer [graph]]
            [reagent.core :as r]))



(defn app
  []
  [:div.container
   [graph]])


(defn ^:export main
  []
  (r/render
   [app]
   (.getElementById js/document "app")))