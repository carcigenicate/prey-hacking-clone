(ns prey-hacking-clone.ball
  (:require [prey-hacking-clone.protocols.circle :as c]))

(defrecord Ball [position velocity radius statuses])

(defn move-by [ball x-offset y-offset]
  (update ball :position
          (fn [[x y]] [(+ x x-offset)
                       (+ y y-offset)])))

(defn set-velocity [ball x-velocity y-velocity]
  (assoc ball :velocity
              [x-velocity y-velocity]))

(defn change-velocity-by [ball x-velocity-offset y-velocity-offset]
  (update ball :velocity
          (fn [[x y]] [(+ x x-velocity-offset)
                       (+ y y-velocity-offset)])))

(defn bounce-x [ball]
  (update-in ball [:velocity 0] -))

(defn bounce-y [ball]
  (update-in ball [:velocity 1] -))

(defn set-status [ball status-key status-value]
  (assoc-in ball [:statuses status-key] status-value))

(defn update-status [ball status-key f]
  (update-in ball [:statuses status-key] f))

(extend Ball
  c/Circle
  {:radius :radius
   :center-position :position})