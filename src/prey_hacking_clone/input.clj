(ns prey-hacking-clone.input
  (:require [prey-hacking-clone.game :as ga]))

(defn keyboard-input-handler [state key ball-speed]
  (let [move-by #(ga/move-player-by state % %2)]
    (case key
      \w (move-by 0 (- ball-speed))
      \s (move-by 0 ball-speed)

      \a (move-by (- ball-speed) 0)
      \d (move-by ball-speed 0))))

(defn mouse-input-handler [state mouse-x mouse-y ball-speed]
  (ga/move-player-to state mouse-x mouse-y ball-speed))