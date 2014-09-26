(ns matasano-clj.helpers-test
  (:require [clojure.test :refer :all]
            [matasano-clj.helpers :refer :all]))

(deftest hex-to-base64
  (testing "Hex string from challenge 1 converts correctly"
    (is (= "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"
           (convert-hex-to-b64 "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")))))
