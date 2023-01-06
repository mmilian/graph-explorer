(ns graph.components.graph
  (:require 
   [reagent.core :as r]
   ["react-force-graph" :as react-graph]
   [graph.state :as state]
    [cljs.reader :as reader]
   ))


(def shapes {:micro {:color "blue"}
             :mssql {:color "green"}
             :elastic {:color "gray"}
             :kafka {:color "red"}})


(defn random-node []
  (nth (keys shapes) (js/Math.round (* (js/Math.random) (dec (count shapes))))))


(defn- gen-random-tree [nodes]
  {:nodes (map (fn [id] {:id id :name "Whatever" :type (random-node)}) (range nodes))
   :links (map (fn [id] {:source id :target (js/Math.round (* (js/Math.random ()) (- id 1)))}) (range 1 nodes))})


(def default-text {:font "2px Sans-Serif"
                   :text-allign "center"
                   :text-baseline "middle"})


(defn nodes-2-map [nodes] 
  (reduce (fn [acc node] (assoc acc (:id node) node)) {} nodes))


(defn calc-color [nodes id]
    (let [node-type (get-in nodes [id :type])]
      (get-in shapes [node-type :color])))

(comment
  (calc-color (nodes-2-map (:nodes (gen-random-tree 12))) 1)
  ;;
  )

(defn nodePaint [node ctx nodes & [opt]]
  (let [x (.-x node)
        y (.-y node)
        id (.-id node)
        color (calc-color nodes id)
        text (get-in nodes [id :type])
        {:keys [font text-allign text-baseline]} (merge default-text opt)]
    (set! (.-fillStyle ctx) color)
    (set! (.-font ctx) font)
    (set! (.-textAlign ctx) text-allign)
    (set! (.-textBaseline ctx) text-baseline)
    (set! (.-globalAlpha ctx) 0.3) 
    (.fillRect ctx (- x 6) (- y 4)  12 8)
    (set! (.-globalAlpha ctx) 1)
    (.fillText ctx text x y)
    ))

(def g (gen-random-tree 50))

(defn load-graph [nr]
  (reset! state/graph (gen-random-tree nr)))


(defn graph
  []
  [:main [(r/adapt-react-class react-graph/ForceGraph2D)
          {:graphData  @state/graph
           :nodeLabel "id"
           :nodeCanvasObject (fn [node ctx] (nodePaint node ctx (nodes-2-map (:nodes @state/graph))))}]])


(comment
  @state/graph
  (load-graph 22)
  )