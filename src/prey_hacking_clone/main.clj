(ns prey-hacking-clone.main
  (:require [prey-hacking-clone.game :as ga]
            [prey-hacking-clone.protocols.circle :as cP]
            [prey-hacking-clone.protocols.rectangle :as rP]
            [prey-hacking-clone.box :as bo]
            [prey-hacking-clone.input :as i]

            [quil.core :as q]
            [quil.middleware :as qm]

            [helpers.general-helpers :as g]
            [helpers.key-manager :as k]
            [prey-hacking-clone.ball :as ba])

  (:gen-class))

; TODO: Enforce the ball being moved via velocity
; TODO: Ball should be disabled on collision
; TODO: Input should add to velcoity instead of setting it directly.
;        Should then be clamped to some max-velocity

(def width 1000)
(def height 1000)

(def ball-speed 1)

(def rand-gen (g/new-rand-gen 99))

(defrecord State [game key-manager])

(defn new-state []
  (->State (ga/new-game [(/ width 2) (/ height 2)] [] [0 0])
           (k/new-key-manager)))

(defn apply-keys [state]
  (update state :game
          #(k/reduce-pressed-keys (:key-manager state)
             (fn [acc key] (i/keyboard-input-handler acc key ball-speed))
             %)))

(defn draw-box [rect]
  (let [{l :left r :right t :top b :bottom} (rP/boundary-map rect)]
    (q/rect l t (- r l) (- b t))))

(defn setup-state []
  (let [box (bo/->Box 300 500 300 500)]
    (-> (new-state)
      (update-in [:game :boxes] #(conj % box)))))

(defn update-state [state]
  #_
  (when (zero? (rem (q/frame-count) 30))
    (println (-> game :player :velocity)))

  (let [[mx my] [(q/mouse-x) (q/mouse-y)]
        fric (* ball-speed 0.5)]
    (-> state
      (apply-keys)
      (update :game ga/move-player-with-velocity)
      (update-in [:game :player] #(ba/reduce-velocity-by % fric fric))
      (update :game ga/resolve-collisions))))

(defn draw-state [state]
  (q/background 200 200 200)

  (let [game (:game state)
        {{[bx by] :position r :radius} :player boxes :boxes} game
        diam (* r 2)]
    (q/ellipse bx by diam diam)

    (doseq [box boxes]
      (draw-box box))))

(defn key-press-handler [state event]
  (update state :key-manager #(k/press-key % (:raw-key event))))

(defn key-release-handler [state]
  (update state :key-manager #(k/release-key % (q/raw-key))))

(defn -main []
  (q/defsketch Prey-Hacking
    :size [width height]

    :setup setup-state
    :update update-state
    :draw draw-state

    :key-pressed key-press-handler
    :key-released key-release-handler

    :middleware [qm/fun-mode]))

