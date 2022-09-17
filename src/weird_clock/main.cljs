(ns weird-clock.main)


(def hours (shuffle '(1 2 3 4 5 6 7 8 9 10 11 12)))

(def draw-canvas (.getElementById js/document "draw"))

(def draw-ctx (.getContext draw-canvas "2d"))

(def params (js/URLSearchParams. (.-search js/location)))

(defn draw-line [ctx draw-width draw-height angle len]
    (.beginPath ctx)
    (.moveTo ctx (/ draw-width 2) (/ draw-height 2))
    (.lineTo ctx
        (+ (/ draw-width 2) (* (/ draw-width 2) (Math/cos angle) len))
        (+ (/ draw-height 2) (* (/ draw-height 2) (Math/sin angle) len)))
    (.stroke draw-ctx))

(set! (.-textBaseline draw-ctx) "middle")
(set! (.-textAlign draw-ctx) "center")

; challenge time
(def challenge-hour (.floor js/Math (* 12 (.random js/Math))))
(def challenge-minute (.floor js/Math (* 60 (.random js/Math))))
(.log js/console "Murder Myster :)")

(defn draw-fn []
    (let [
        draw-width (.-offsetWidth draw-canvas)
        draw-height (.-offsetHeight draw-canvas)]
        (set! (.-width draw-canvas) draw-width)
        (set! (.-height draw-canvas) draw-height)
        (set! (.-fillStyle draw-ctx) "rgb(0,255,255)")
        (.fillRect draw-ctx 0 0 draw-width draw-height)
        (set! (.-font draw-ctx) "1em sans-serif")
        (set! (.-fontStyle draw-ctx) "bold")
        (set! (.-fillStyle draw-ctx) "rgb(0,0,0)")
        (loop
            [index 11]
            (when (>= index 0)
                (.fillText draw-ctx
                    (nth hours index)
                    (+ (/ draw-width 2) (* (/ draw-width 2) (Math/cos (/ (* Math/PI index) 6)) 0.8))
                    (+ (/ draw-height 2) (* (/ draw-height 2) (Math/sin (/ (* Math/PI index) 6)) 0.8)))
                (recur (- index 1))))
        (set! (.-fillStyle draw-ctx) "rgb(255,0,0)")
        (set! (.-lineWidth draw-ctx) 6)
        (let [
            time (/ (.now js/Date) 1000 60)
            minute challenge-minute ; (mod time 60)
            hour (+ challenge-hour (/ minute 60)) ; (mod (+ (/ time 60) (- (/ (.getTimezoneOffset (js/Date.)) 60))) 12)
            startHour (.indexOf hours (Math/floor hour))
            endHour (.indexOf hours (Math/floor (+ hour 1)))
            startMinute (.indexOf hours (Math/floor (/ minute 5)))
            endMinute (.indexOf hours (Math/floor (+ (/ minute 5) 1)))]
            (draw-line draw-ctx draw-width draw-height (* 2 Math/PI (/ (+ startMinute (* (mod (- endMinute startMinute) 12) (/ (mod minute 5) 5))) 12)) 0.7)
            (draw-line draw-ctx draw-width draw-height (* 2 Math/PI (/ (+ startHour (* (/ minute 60) (mod (- endHour startHour) 12))) 12)) 0.5))))

; (js/setInterval draw-fn 1000)
(draw-fn)

(def guess-input (.getElementById js/document "guess-text"))

(defn guess-fn []
    (let [
        value (.-value guess-input)
        hour (js/parseInt (.substring value 0 (.indexOf value ":")))
        minute (js/parseInt (.substring value (+ (.indexOf value ":") 1)))]
        (when-not (or (js/isNaN hour) (js/isNaN minute))
            (if (and (= challenge-hour hour) (= challenge-minute minute))
                (set! (.-innerHTML (.getElementById js/document "body")) "<h1>Clue 9: 5</h1>")
                (set! (.-value guess-input) "")))))

(.addEventListener (.getElementById js/document "guess-button") "click" guess-fn)

(defn print-answer [] (.log js/console (.concat "" challenge-hour ":" challenge-minute)))