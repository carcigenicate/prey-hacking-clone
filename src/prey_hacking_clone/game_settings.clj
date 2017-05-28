(ns prey-hacking-clone.game-settings
  (:require [prey-hacking-clone.number-helpers :as nh]))

(defrecord Settings [input-velocity friction max-velocity player-radius])

(def default-settings
  (map->Settings
    {:input-velocity 5
     :friction 1
     :max-velocity 5
     :player-radius 30}))

(defn clamp-velocity [settings velocity]
  (let [max-v (:max-velocity settings)]
    (mapv #(nh/clamp-magnitude % max-v) velocity)))
