(ns meeting-burn.core
  (:require [clojure.string :as string]
            [clojure.walk :refer [keywordize-keys]]
            [reagent.core :as reagent]
            [reagent.format :as format]))

(def decode js/decodeURIComponent)

;; ganked from https://github.com/gf3/secretary/blob/master/src/secretary/core.cljs
(defn decode-query-params
  "Extract a map of query parameters from a query string."
  [query-string]
  (let [parts (string/split query-string #"&")
        params (reduce
                (fn [m part]
                  ;; We only want two parts since the part on the right hand side
                  ;; could potentially contain an =.
                  (let [[k v] (string/split part #"=" 2)]
                    (assoc m k (decode v))))
                {}
                parts)
        params (keywordize-keys params)]
    params))

(defonce app-state
  (let [params (-> js/location .-search  (subs 1) decode-query-params)]
    (reagent/atom
     {:cents-per-second (-> params :oneseccost int) 
      })))

(defn burn-counter-component [money-per-second interval-ms]
  (let [seconds-elapsed (reagent/atom 0)
        seconds-per-interval (/ interval-ms 1000 )
        ]
    (.log js/console interval-ms seconds-per-interval)
    (fn []
      (js/setTimeout #(swap! seconds-elapsed (partial + seconds-per-interval)) interval-ms)
      [:div
       [:h2 "Money Spent" ]
       [:hr]
       [:div.money
        (format/currency-format (-> @seconds-elapsed (* money-per-second) (/ 100)) )]
       [:hr]])))

(defn page []
  [:div [burn-counter-component (:cents-per-second @app-state) 25]])

(defn ^:export main []
  (reagent/render
   [page]
   (.getElementById js/document "app")))
