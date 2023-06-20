; Ex 1
(let [p (promise)]
  (let [google {:url "https://google.com"
                :site-name :google}
        github {:url "https://github.com"
                :site-name :github}
        #_(americanas {:url "https://americanas.com.br"
                       :site-name :americanas}
                      amazon {:url "https://amazon.com"
                              :site-name :amazon})]
    (doseq [site [google github #_(americanas amazon)]]
      (future (deliver p (assoc site :response (slurp (:url site)))))))
  #_(println (str "Atom: " @p))
  (:site-name @p))