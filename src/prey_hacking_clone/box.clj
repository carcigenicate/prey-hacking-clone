(ns prey-hacking-clone.box
  (:require [prey-hacking-clone.protocols.rectangle :as rP]))

(defrecord Box [top bottom left right]
  Object
  (toString [self] (str (into {} self))))

(defn new-box-tl [top-left-coord box-dimensions]
  (let [[w h] box-dimensions
        [tx ty] top-left-coord]
    (->Box ty (+ ty h)
           tx (+ tx w))))

(extend Box
  rP/Rectangle
  {:left :left
   :right :right
   :top :top
   :bottom :bottom})
