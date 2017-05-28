(ns prey-hacking-clone.ball
  (:require [prey-hacking-clone.protocols.circle :as c]
            [prey-hacking-clone.number-helpers :as nh]

            [helpers.general-helpers :as g]))

(defrecord Ball [position velocity radius statuses]
  Object
  (toString [self] (str (into {} self))))

(defn new-ball [position radius]
  (->Ball position [0 0] radius {}))

(defn move-by [ball x-offset y-offset]
  (update ball :position
          (fn [[x y]] [(+ x x-offset)
                       (+ y y-offset)])))

(defn move-with-velocity [ball]
  (let [[vx vy] (:velocity ball)]
      (move-by ball vx vy)))

(defn set-velocity [ball x-velocity y-velocity]
  (assoc ball :velocity
              [x-velocity y-velocity]))

(defn change-velocity-by [ball x-velocity-offset y-velocity-offset]
  (update ball :velocity
          (fn [[x y]] [(+ x x-velocity-offset)
                       (+ y y-velocity-offset)])))

(defn reduce-velocity-by
  "Reduces the velocity of the ball in each dimension toward 0."
  [ball x-friction y-friction]
  (update ball :velocity
          (fn [[xv yv]]
            [(nh/toward-zero-by xv x-friction)
             (nh/toward-zero-by yv y-friction)])))

; TODO: EWW!
#_
(defn reduce-velocity-by
  "Reduces the velocity of the ball in each dimension toward 0."
  [ball x-friction y-friction]
  (let [sig #(Math/signum ^double %)
        non-z-clamp #(g/clamp % 0 Double/MAX_VALUE)]
    (update ball :velocity
            (fn [[xv yv :as vel]]
              (let [sigs (map sig vel)
                    [i-x-sig i-y-sig] (map - sigs)
                    x-fric (* i-x-sig x-friction)
                    y-fric (* i-y-sig y-friction)]
                [(non-z-clamp (- xv x-fric))
                 (non-z-clamp (- yv y-fric))])))))

(defn bounce-x [ball]
  (update-in ball [:velocity 0] -))

(defn bounce-y [ball]
  (update-in ball [:velocity 1] -))

(defn set-status [ball status-key status-value]
  (assoc-in ball [:statuses status-key] status-value))

(defn status [ball status-key]
  (get-in ball [:statuses status-key]))

(defn update-status
  "Updates a given status of the ball. If f returns nil, the status will be removed from the ball."
  [ball status-key f]
  (let [new-status (f (status ball status-key))]
    (if (nil? new-status)
      (update ball :status #(dissoc % status-key))
      (assoc-in ball [:status status-key] new-status))))

(defn offsets-to-target [position target move-by]
  (let [[x-off y-off] (mapv - target position)
        dist (Math/sqrt (+ (* y-off y-off) (* x-off x-off)))
        x-move (/ (* x-off move-by) dist)
        y-move (/ (* y-off move-by) dist)]

    (if (< dist move-by)
      [0 0]
      [x-move y-move])))

(defn move-towards [ball target by]
  (let [position (:position ball)
        [x-off y-off] (offsets-to-target position target by)]
    (move-by ball x-off y-off)))

(extend Ball
  c/Circle
  {:radius :radius
   :center-position :position})