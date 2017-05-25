(ns prey-hacking-clone.main
  (:require [prey-hacking-clone.game :as ga]

            [quil.core :as q]
            [quil.middleware :as qm])
  (:gen-class))

(def width 1000)
(def height 1000)

(def ball-speed 5)

(defn setup-state [])

(defn update-state [state]
  (let [[mx my] [(q/mouse-x) (q/mouse-y)]]))

(defn draw-state [state])

(defn -main
  (q/defsketch Prey-Hacking
    :size [width height]

    :setup setup-state
    :update update-state
    :draw draw-state))

