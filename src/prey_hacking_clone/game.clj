(ns prey-hacking-clone.game
  (:require [prey-hacking-clone.ball :as ba]
            [prey-hacking-clone.collision :as c]))

; TODO: Should be able to be set externally. Make part of Game?
(def end-point-radius 20)
(def player-radius)

(defrecord Game [player boxes end-point])

(defn new-game [starting-position boxes end-point-position]
  (->Game (ba/->Ball starting-position)))

(defn move-player-by [game x-offset y-offset]
  (update game :player #(ba/move-by % x-offset y-offset)))

(defn collide-with [ball box]
  (if-let [side? (c/colliding-side ball box)]
    (cond
      (#{:left :right} side?) (ba/bounce-x ball)
      (#{:top :bottom} side?) (ba/bounce-y ball))

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