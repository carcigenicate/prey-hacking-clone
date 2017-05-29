(ns prey-hacking-clone.number-helpers
  (:require [helpers.general-helpers :as g]))

(defn signum [n]
  (Math/signum ^double n))

(defn abs [n]
  (Math/abs ^double n))

(defn toward-zero-by
  "Returns a value that's been \"moved\" closer to zero; regardless of it's sign. If (> by (abs n)), 0 is returned."
  [n by]
  (let [sign (signum n)
        reduce-by (* sign by)
        adj (- n reduce-by)]
    (if (pos? n)
      (g/clamp adj 0 Double/MAX_VALUE)
      (g/clamp adj (- Double/MAX_VALUE) 0))))

(defn clamp-magnitude [n max-magnitude]
  (if (pos? n)
    (g/clamp n 0 max-magnitude)
    (g/clamp n (- max-magnitude) 0)))