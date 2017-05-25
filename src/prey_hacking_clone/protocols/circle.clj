(ns prey-hacking-clone.protocols.circle)

(defprotocol Circle
  (radius [self] "Returns the radius of the circle.")

  (center-position [self] "Returns a vector of [x y] representing the center position of the circle."))

(defn boundary-map
  "Returns a map with :radius and :center-position keys for easy destructuring."
  [circle]
  {:radius (radius circle)
   :center-position (center-position circle)})
