(ns prey-hacking-clone.protocols.rectangle)

(defprotocol Rectangle
  (left [self])
  (right [self])

  (top [self])
  (bottom [self]))

(defn boundary-map
  "Returns a map with :left, :right, :top, and :bottom keys for easy destructuring."
  [rectangle]
  {:left (left rectangle)
   :right (right rectangle)
   :top (top rectangle)
   :bottom (bottom rectangle)})