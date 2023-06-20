
(ns exercises.class8.main
  (:require [io.pedestal.http :as http]
            [io.pedestal.test :as test-http]
            [io.pedestal.interceptor :as i]
            [io.pedestal.interceptor.chain :as chain]))

; Ex 1
(defn clean-line [line]
  (let [replace-map {\( "" \) "" \' "" \, ""}]
    (clojure.string/escape line replace-map)))

(defn format-line [line]
  (.toLowerCase line))

(defn parse-line [line]
  (let [tokens (.split (format-line (clean-line line)) " ")]
    (map #(vector % 1) tokens)))

(defn combine [mapped]
  (->> (apply concat mapped)
       (group-by first)
       (map (fn [[k v]]
              {k (map second v)}))
       (apply merge-with conj)))

(defn read-lines [file]
  (-> file
      slurp
      clojure.string/split-lines))

(defn sum [[k v]]
  {k (apply + v)})

(defn reduce-parsed-lines [collected-values]
  (apply merge (map sum collected-values)))

(defn word-frequency [filename]
  (->> (read-lines filename)
       (map parse-line)
       (combine)
       (reduce-parsed-lines)))

; Teste
(println (->>
           (word-frequency "canto-chars.txt")
           (sort-by second >)))

; Ex 2
(def impar
  (i/interceptor
    {:name  ::odds
     :enter (fn [context]
              (assoc context :response {:body   "Eu recebo números impares\n"
                                        :status 200}))}))

(def par
  (i/interceptor
    {:name  ::evens
     :enter (fn [context]
              (assoc context :response {:body   "Eu recebo números pares\n"
                                        :status 200}))}))

(def par-ou-impar
  {:name  ::par-ou-impar
   :enter (fn [context]
            (try
              (let [param (get-in context [:request :query-params :n])
                    n     (Integer/parseInt param)
                    nxt   (if (even? n) par impar)]
                (chain/enqueue context [nxt]))
              (catch NumberFormatException e
                (assoc context :response {:body   "Número inválido catch\n"
                                          :status 400}))))})

(def routes
  #{["/par-ou-impar" :get par-ou-impar]})

(defn create-server []
  (http/create-server
    {::http/routes routes
     ::http/type   :jetty
     ::http/port   8890
     ::http/join?  false}))

(defonce server (atom nil))

(defn start []
  (reset! server (http/start (create-server))))

(defn test-request [server verb url]
  (test-http/response-for (::http/service-fn @server) verb url))

; Tests
(comment
  (start)

  (doseq [n (range 101)]
    (->> n
         (str "/par-ou-impar?n=")
         (test-request server :get)
         (:body)
         (str n ": ")
         (println))))