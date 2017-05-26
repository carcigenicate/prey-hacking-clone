(ns prey-hacking-clone.ball-effects
  (:require [prey-hacking-clone.ball :as ba]))

(def statuses #{::disabled-for})

; ----- Ball Disabling -----

(defn disabled? [ball]
  (ba/status ball ::disabled-for))

(defn disable-ball-for
  "Sets the ball as disabled for duration milliseconds."
  [ball duration]
  (ba/set-status ball ::disabled-for duration))

(defn dec-disabled-time [ball duration]
  (ba/update-status ball ::disabled-for
    #(let [new-dur (- % duration)]
       (if (<= new-dur 0)
         nil
         new-dur))))
