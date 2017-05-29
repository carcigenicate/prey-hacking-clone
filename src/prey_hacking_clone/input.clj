(ns prey-hacking-clone.input
  (:require [prey-hacking-clone.game :as ga]))

(defn keyboard-input-handler [state key]
  (let [move-by #(ga/set-velocity-direction state % %2)]
    (case key
      \w (move-by 0 -1)
      \s (move-by 0 1)

      \a (move-by -1 0)
      \d (move-by 1 0)

      state)))

(defn mouse-input-handler [state mouse-x mouse-y ball-speed]
  (ga/move-player-to state mouse-x mouse-y ball-speed))