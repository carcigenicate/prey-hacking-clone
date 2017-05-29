(ns prey-hacking-clone.game
  (:require [prey-hacking-clone.ball :as ba]
            [prey-hacking-clone.box :as bo]
            [prey-hacking-clone.collision :as c]
            [prey-hacking-clone.game-settings :as gs]
            [prey-hacking-clone.protocols.circle :as cP]
            [prey-hacking-clone.number-helpers :as nh]))

; TODO: Should be able to be set externally. Make part of Game?
(def end-point-radius 30)
(def player-radius 50)

(defrecord Game [player boxes end-point settings])

(defn new-game [starting-position boxes end-point-position]
  (->Game (ba/new-ball starting-position player-radius)
          boxes
          end-point-position
          gs/default-settings))

(defn set-velocity-direction [game x-dir y-dir]
  (let [x-dir' (nh/signum x-dir)
        y-dir' (nh/signum y-dir)
        vel (-> game :settings :player-velocity)
        x-offset (* x-dir' vel)
        y-offset (* y-dir' vel)]

    (update game :player
            #(-> %
                 (ba/change-velocity-by x-offset y-offset)
                 (update :velocity (partial gs/clamp-velocity (:settings game)))))))

(defn move-player-to [game x-target y-target by]
  (update game :player
          #(ba/move-towards % [x-target y-target] by)))

(defn move-player-with-velocity [game]
  (update game :player ba/move-with-velocity))

(defn apply-friction [game]
  (let [fric (-> game :settings :friction)]
    (update game :player #(ba/reduce-velocity-by % fric fric))))

(defn collide-with [ball box]
  (let [side? (c/colliding-side ball box)
        av #(assoc ball :velocity %)
        c #(Math/copySign ^double % ^double %2)
        [xv yv] (:velocity ball)]
    (case side?
      nil ball
      :left (av [(c xv -1) yv])
      :right (av [(c xv 1) yv])
      :top (av [xv (c yv -1)])
      :bottom (av [xv (c yv 1)]))))

(defn resolve-collisions [game]
  (update game :player
    #(reduce
       (fn [ball box]
         (if (c/circle-rect-collision? ball box)
           (reduced (collide-with ball box))
           ball))
       %
       (:boxes game))))