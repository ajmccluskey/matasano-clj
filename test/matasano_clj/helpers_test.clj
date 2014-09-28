(ns matasano-clj.helpers-test
  (:require [clojure.test :refer :all]
            [matasano-clj.helpers :refer :all]))

(deftest hex-to-base64-challenge
  (testing "Hex string from challenge 1 converts correctly"
    (is (= "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"
           (convert-hex-to-b64 "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")))))

(deftest fixed-xor-challenge
  (testing "XOR 2 hex strings of equal length")
  (is (= "746865206b696420646f6e277420706c6179"
         (fixed-xor-hex-strings "1c0111001f010100061a024b53535009181c"
                                "686974207468652062756c6c277320657965"))))

(deftest bytes-to-hex-string-conversions
  (testing "Make sure we can convert correctly between hex strings and byte seqs")
  (let [s "021b04"]
    ; Hex strings created from byte seqs should 0 pad each byte to preserve byte values
    (is (= s (bytes-to-hex-string (map byte '(2 27 4)))))
    ; We should get the same string back after going from hex -> byte seq -> hex
    (is (= s (bytes-to-hex-string (hex-string-to-bytes s))))))
