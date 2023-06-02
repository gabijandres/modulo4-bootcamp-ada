(ns exercises.class3.main)

(def clock (atom 0))
(def time-by-unit {:s 1000 :ms 1})
(def keep-counting (atom true))

(defn tic!
  "Increment the value of clock atom every second"
  [clock]
  (Thread/sleep 1000)
  (swap! clock inc)
  (println (str @clock "s have passed"))
  (recur clock))

; Tests
(comment
  (future (tic! clock)) ; another thread
  (tic! clock)) ; main thread

(defn tic-config!
  "Increment the value of clock atom every second or milisecond, depending on the value of unit"
  [clock unit]
  (let [time (unit time-by-unit)]
    (if (not (nil? time))
      (Thread/sleep time)
      (throw (Exception. "Invalid unit"))))
  (swap! clock inc)
  (println (str @clock (name unit) " have passed"))
  (recur clock unit))

; Tests
(comment
  (tic-config! clock :s)
  (tic-config! clock :ms)
  (tic-config! clock :batata))

(defn tic-time!
  "Increment the value of clock atom when 'miliseconds' ms have passed"
  [clock miliseconds]
  (Thread/sleep miliseconds)
  (swap! clock inc)
  (println (str "(" @clock " x " miliseconds ") ms have passed"))
  (recur clock miliseconds))

; Tests
(comment
  (tic-time! clock 5000))

(defn tic-on-off!
  "Increment the value of clock atom every second, based on the value of keep-couting"
  [clock keep-counting]
  (Thread/sleep 1000)
  (swap! clock inc)
  (println (str @clock "s have passed"))
  (if (true? @keep-counting)
    (recur clock keep-counting)
    (println "The clock was turned off")))

; Tests
(comment
  (future (tic-on-off! clock keep-counting)) ; another thread
  (Thread/sleep 5000) ; main thread will sleep for 5s
  (swap! keep-counting not)) ; main thread will turn off the clock
