(ns prey-hacking-clone.main
  (:require [prey-hacking-clone.game :as ga]
            [prey-hacking-clone.protocols.circle :as cP]
            [prey-hacking-clone.protocols.rectangle :as rP]
            [prey-hacking-clone.box :as bo]

            [quil.core :as q]
            [quil.middleware :as qm]

            [helpers.general-helpers :as g])

  (:gen-class))

; TODO: Enforce the ball being moved via velocity

(def width 1000)
(def height 1000)

(def ball-speed 10)

(def rand-gen (g/new-rand-gen 99))
#_
(defn random-boxes [n-boxes min-length max-length min-height max-height rand-gen]
  (let []
    (for [n (range n-boxes)]
      (bo/->Box (g/random-double 0 width)))))

(defn draw-box [rect]
  (let [{l :left r :right t :top b :bottom} (rP/boundary-map rect)]
    (q/rect l t (- r l) (- b t))))

(defn setup-state []
  (let [box (bo/->Box 300 500 300 500)]
    (-> (ga/new-game [(/ width 2) (/ height 2)] [box] [0 0])
        (assoc-in [:player :velocity] [2 5]))))

(defn update-state [game]
  (when (zero? (rem (q/frame-count) 30))
    (println (-> game :player :velocity)))

  (let [[mx my] [(q/mouse-x) (q/mouse-y)]]
    (-> game
      (ga/move-player-to mx my ball-speed)
      (ga/resolve-collisions))))

(defn draw-state [game]
  (q/background 200 200 200)

  (let [{{[bx by] :position r :radius} :player boxes :boxes} game]
    (q/ellipse bx by r r)

    (doseq [box boxes]
      (draw-box box))))

(defn -main []
  (q/defsketch Prey-Hacking
    :size [width height]

    :setup setup-state
    :update update-state
    :draw draw-state

    :middleware [qm/fun-mode]))

