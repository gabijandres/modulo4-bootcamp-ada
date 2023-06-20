; Ex 1

(def coll (range 10))
(def max-range 10)

(defn get-multiples
  ([number coll]
  (take-nth number coll))
  ([number]
  (get-multiples number (range max-range))))

; Tests
(comment
  (get-multiples 5 coll)
  (get-multiples 5))

; Ex 2

(def entries [{:month 1 :val 12}
              {:month 2 :val 3}
              {:month 3 :val 32}
              {:month 4 :val 18}
              {:month 5 :val 32}
              {:month 6 :val 62}
              {:month 7 :val 12}
              {:month 8 :val 142}
              {:month 9 :val 52}
              {:month 10 :val 18}
              {:month 11 :val 23}
              {:month 12 :val 56}])

(defn get-result
  [coll m]
  (take-while
    #(>= (:val %) m) coll))

(defn ignore-results
  [coll m]
  (drop-while
    #(>= (:val %) m) coll))

; Tests
(comment
  (get-result entries 7)
  (ignore-results entries 11))

(defn get-result2
  [coll m k]
  (take-while
    #(<= (k %) m) coll))

(defn ignore-results2
  [coll m k]
  (drop-while
    #(<= (k %) m) coll))

; Tests
(comment
  (get-result2 entries 7 :month)
  (ignore-results2 entries 11 :month))

; Ex 3

(def phone-numbers ["221 610-5007" "221 433-4185"
                    "661 471-3948" "661 653-4480"
                    "661 773-8656" "555 515-0158"])

(defn unique-area-codes [numbers]
  (->> numbers
       (map #(clojure.string/split % #" "))
       (map second)
       (map #(clojure.string/split % #"-"))
       (map second)
       distinct))

; Tests
(comment
  (unique-area-codes phone-numbers))

; Ex 4

(defn all-positives? [coll]
  (cond
    (empty? coll) true
    (pos? (first coll)) (recur (rest coll))
    :else false))

; Tests
(comment
  (all-positives? (list 1 2 3 5 6 7))
  (all-positives? (list 0 1 2 3 4 5 6 -1)))