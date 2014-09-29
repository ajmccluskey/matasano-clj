(ns matasano-clj.xor-cipher-test
  (:require [clojure.test :refer :all]
            [matasano-clj.xor-cipher :as mx]
            [matasano-clj.helpers :as h]))

(def pt "Cooking MC's like a pound of bacon")
(def ct "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")

(deftest single-byte-xor
  (testing "We can successfully XOR a hex string with a repeating, single-byte key")
  (is (= ct
         (h/bytes-to-hex-string (mx/decipher-with-single-byte-xor-key ct (byte 0))))))

(deftest map-single-byte-deciphers
  (testing "We build an accurate map of keys to cipher texts")
  (is (= (hash-map 88 pt)
         (mx/map-single-byte-keys ct (list (byte 88))))))

(deftest find-most-likely-key
  (testing "Find the most likely single byte key based on letter frequencies of
           plain text")
  (is (= {88 "Cooking MC's like a pound of bacon"}
         (mx/find-most-likely-plain-texts ct))))
