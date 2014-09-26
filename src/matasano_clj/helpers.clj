(ns matasano-clj.helpers
  (:require [clojure.data.codec.base64 :as b64]))

(defn hex-string-to-bytes [s]
  "Convert a character string to the seq of bytes it represents. String
  must be in hex and contain an even number of characters."
  (assert (even? (count s)))
  (let [char-pairs (partition 2 s)
        int-pairs (map #(Integer/parseInt (reduce str %) 16) char-pairs)]
    (map byte int-pairs)))

(defn convert-hex-to-b64 [s]
  "Set1 - Challenge 1. Converts a string of hexadecimal digits to a base64
  encoded string. hex must have an even length"
  (String. (b64/encode (byte-array (hex-string-to-bytes s)))))
