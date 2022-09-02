(ns weird-clock.main)


(def hours (shuffle '(1 2 3 4 5 6 7 8 9 10 11 12)))

(def draw-canvas (.getElementById js/document "draw"))

(def draw-width (.-width draw-canvas))
(def draw-height (.-height draw-canvas))
(def draw-ctx (.getContext draw-canvas "2d"))

(defn draw-line [ctx angle len] 
    (.beginPath ctx)
    (.moveTo ctx (/ draw-width 2) (/ draw-height 2))
    (.lineTo ctx
        (+ (/ draw-width 2) (* (/ draw-width 2) (Math/cos angle) len))
        (+ (/ draw-height 2) (* (/ draw-height 2) (Math/sin angle) len)))
    (.stroke draw-ctx))


(defn draw-fn [] 
    (set! (.-fillStyle draw-ctx) "rgb(0,255,255)")
    (.fillRect draw-ctx 0 0 draw-width draw-height)
    (set! (.-font draw-ctx) "18px sans-serif")
    (set! (.-fontStyle draw-ctx) "bold")
    (set! (.-fillStyle draw-ctx) "rgb(0,0,0)")
    (loop 
        [index 11]
        (when (>= index 0)
            (.fillText draw-ctx
                (nth hours index)
                (+ (/ draw-width 2) (* (/ draw-width 2) (Math/cos (/ (* Math/PI index) 6)) 0.9))
                (+ (/ draw-height 2) (* (/ draw-height 2) (Math/sin (/ (* Math/PI index) 6)) 0.9)))
            (recur (- index 1))))
    (set! (.-fillStyle draw-ctx) "rgb(255,0,0)")
    (set! (.-lineWidth draw-ctx) 6)
    (let [
        time (/ (.now js/Date) 1000 60)
        minute (mod time 60)
        hour (- (mod (/ time 60) 12) (/ (.getTimezoneOffset (js/Date.)) 60))
        startHour (.indexOf hours (Math/floor hour))
        endHour (.indexOf hours (Math/floor (+ hour 1)))]
        (draw-line draw-ctx (- (* Math/PI minute .0333333333) (/ Math/PI 2)) 0.9)
        (draw-line draw-ctx (* 2 Math/PI (/ (+ startHour (* (/ minute 60) (mod (- endHour startHour) 12))) 12)) 0.6)))

(js/setInterval draw-fn 1000)