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

(deftest combine-map-vals
  (is (= #{11 12 13}
         (set (apply-to-corresponding-map-vals + {:a 1 :b 3 :c 1 :d 10} {:b 8 :c 11 :d 3})))))

(deftest byte-to-base64
  (is (= \A) (byte-to-base64-char 0))
  (is (= \Z) (byte-to-base64-char 25))
  (is (= \a) (byte-to-base64-char 26))
  (is (= \z) (byte-to-base64-char 51))
  (is (= \0) (byte-to-base64-char 52))
  (is (= \9) (byte-to-base64-char 61))
  (is (= \+) (byte-to-base64-char 62))
  (is (= \/) (byte-to-base64-char 63))
  (is (= \=) (byte-to-base64-char nil)))

(deftest three-bytes-to-base64-bytes-test
  ; (00000011 00111101 00111110) => (00000000 00110011 00110100 00111110)
  (is (= (map byte '(0 51 52 62))
         (three-bytes-to-base64-bytes (map byte '(3 61 62)))))
  ; Test padding works - missing input bytes are output as nils
  ; (00000100) => (00000100 nil nil)
  (is (= '(1 0 nil nil)
         (three-bytes-to-base64-bytes (map byte '(4))))))

(deftest next-base64-test
  (is (= (byte 2r00001100))
      (next-base64 (byte 2r00110011) (byte 0) 2))
  (is (= (byte 2r00111110))
      ; -17's 8 LSB are 11101111
      (next-base64 (byte -17) (byte 2r00110000) 4)))

(deftest next-overflow-test
  (is (= (byte 2r00110000)
         (next-overflow (byte 2r00000011) 2)
         ))

  ; (byte -16) => 11110000
  (is  (= (byte 2r00111100)
          (next-overflow (byte 2r01001111) 4))))
