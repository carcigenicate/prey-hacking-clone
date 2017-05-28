(ns prey-hacking-clone.game
  (:require [prey-hacking-clone.ball :as ba]
            [prey-hacking-clone.collision :as c]
            [prey-hacking-clone.game-settings :as gs]
            [prey-hacking-clone.protocols.circle :as cP]))

; TODO: Should be able to be set externally. Make part of Game?
(def end-point-radius 30)
(def player-radius 50)

(defrecord Game [player boxes end-point settings])

(defn new-game [starting-position boxes end-point-position]
  (->Game (ba/new-ball starting-position player-radius)
          boxes
          end-point-position
          gs/default-settings))

; TODO: Should use "input velocity"? Getting messy?
(defn set-player-velocity [game x-offset y-offset]
  (update game :player
          #(-> %
               (ba/change-velocity-by x-offset y-offset)
               (update :velocity (partial gs/clamp-velocity (:settings game))))))

(defn move-player-to [game x-target y-target by]
  (update game :player
          #(ba/move-towards % [x-target y-target] by)))

(defn move-player-with-velocity [game]
  (update game :player ba/move-with-velocity))

(defn collide-with [ball box]
  (print "-") (flush)
  ball
  #_
  (if-let [side? (c/colliding-side ball box)]
    (cond
      (#{:left :right} side?) (ba/bounce-x ball)
      (#{:top :bottom} side?) (ba/bounce-y ball))

    (do
      (println (str "Triggered: " ball "\n" box))
      ball) ; TODO: Wut?
    #_
    (throw (IllegalStateException.
             (str "Ball isn't colliding with anything!\n" ball "\n" box)))))

(defn resolve-collisions [game]
  (update game :player
    #(reduce
       (fn [ball box]
         (if (c/circle-rect-collision? ball box)
           (reduced (collide-with ball box))
           ball))
       %
       (:boxes game))))