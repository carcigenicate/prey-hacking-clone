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
; TODO: Input should add to velocity instead of setting it directly.
;        Should then be clamped to some max-velocity

(def width 1500)
(def height 1000)

(def ball-speed 1)

(def rand-gen (g/new-rand-gen 99))

(defrecord State [game key-manager])

(defn new-state []
  (->State (ga/new-game [(* width 0.4) (* height 0.8)] [] [0 0])
           (k/new-key-manager)))

(defn apply-keys [state]
  (update state :game
          #(k/reduce-pressed-keys (:key-manager state)
             (fn [acc key] (i/keyboard-input-handler acc key))
             %)))

(defn draw-box [rect]
  (let [{l :left r :right t :top b :bottom} (rP/boundary-map rect)]
    (q/rect l t (- r l) (- b t))))

(defn setup-state []
  (q/frame-rate 60)

  (let [boxes [(bo/->Box 300 700 300 700)
               (bo/->Box 300 700 900 1100)]]
    (-> (new-state)
      (update-in [:game :boxes] #(concat % boxes)))))

(defn update-game-state [state]
  (update state :game
    #(-> %
         (ga/move-player-with-velocity)
         (ga/apply-friction)
         (ga/resolve-collisions))))

(defn update-state [state]
  #_
  (when (zero? (rem (q/frame-count) 30))
    (println (-> game :player :velocity)))

  (let [[mx my] [(q/mouse-x) (q/mouse-y)]]
    (-> state
      (apply-keys)
      (update-game-state))))

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

