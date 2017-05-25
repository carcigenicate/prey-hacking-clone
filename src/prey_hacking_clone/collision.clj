(ns prey-hacking-clone.collision
  (:require [helpers.general-helpers :as g]

            [prey-hacking-clone.protocols.circle :as cP]
            [prey-hacking-clone.protocols.rectangle :as rP]))

(defn circle-rect-collision? [circle rectangle]
  (let [{top :top left :left bottom :bottom right :right} (rP/boundary-map rectangle)
        {[bx by] :center-position radius :radius} (cP/boundary-map circle)

        closest-x (g/clamp bx left right)
        closest-y (g/clamp by top right)

        x-dist (- bx closest-x)
        y-dist (- by closest-y)]

    (< (+ (* x-dist x-dist) (* y-dist y-dist))
       (* radius radius))))

(defn colliding-side [circle rectangle]
  (let [{t :top b :bottom l :left r :right} (rP/boundary-map rectangle)
        {[bx by] :center-position rad :radius} (cP/boundary-map circle)
        abs #(Math/abs ^double %)
        col? #(< (abs (- % %2)) rad)]

    (cond
      (col? t by) :top
      (col? by b) :bottom
      (col? l bx) :left
      (col? bx r) :right)))


(def test-circle
  (reify
    cP/Circle
    (radius [self] 2)
    (center-position [self] [0 0])))

(def test-rectangle
  (reify
    rP/Rectangle
    (left [_] 1) (right [_] 3)
    (top [_] -2) (bottom [_] -2)))