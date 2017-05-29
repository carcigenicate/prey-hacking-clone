(ns prey-hacking-clone.game-settings
  (:require [prey-hacking-clone.number-helpers :as nh]))

(defrecord Settings [player-velocity friction max-velocity player-radius])

(def default-settings
  (map->Settings
    {:player-velocity 3
     :friction 0
     :max-velocity 5
     :player-radius 30}))

(defn clamp-velocity [settings velocity]
  (let [max-v (:max-velocity settings)]
    (mapv #(nh/clamp-magnitude % max-v) velocity)))
