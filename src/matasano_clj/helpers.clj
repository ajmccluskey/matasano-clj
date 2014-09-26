(ns matasano-clj.helpers
  (:require [clojure.data.codec.base64 :as b64]))

(defn convert-hex-to-b64 [hex]
  "Set1 - Challenge 1."
  (let [char-partitions (partition 2 2 (repeat \0) hex)
        byte-sized-ints (map #(Integer/parseInt (reduce str %) 16) char-partitions)]
    (String. (b64/encode (byte-array (map byte byte-sized-ints))))))
