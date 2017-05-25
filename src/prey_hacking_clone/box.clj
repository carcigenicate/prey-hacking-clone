(ns prey-hacking-clone.box)

(defrecord Box [top bottom left right])

(defn new-box-tl [top-left-coord box-dimensions]
  (let [[w h] box-dimensions
        [tx ty] top-left-coord]
    (->Box ty (+ ty h)
           tx (+ tx w))))