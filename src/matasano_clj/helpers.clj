(ns matasano-clj.helpers
  (:require [clojure.data.codec.base64 :as b64]))

(defn convert-hex-to-b64 [hex]
  "Set1 - Challenge 1. Converts a string of hexadecimal digits to a base64
  encoded string. hex must have an even length"
  (assert (even? (count hex)))

  (let [char-partitions (partition 2 hex)
        byte-sized-ints (map #(Integer/parseInt (reduce str %) 16) char-partitions)]
    (String. (b64/encode (byte-array (map byte byte-sized-ints))))))
