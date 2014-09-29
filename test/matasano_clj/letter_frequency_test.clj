(ns matasano-clj.letter-frequency-test
  (:require [clojure.test :refer :all]
            [matasano-clj.letter-frequency :as lf]))

(def abc-test-string "abc !abc_abc& *( abc000abc---bc+c bcbcbcbccccc")
(def abc-test-string-frequencies {\space (/ 4 34) \A (/ 5 34) \B (/ 10 34) \C (/ 15 34)})
(def abc-test-string-score 0.8571915824)

(deftest calc-letter-frequency-strips-punctuation
  (testing "We can calculate letter frequencies of a string that contains
           non letter characters")
  (is (= abc-test-string-frequencies
         (lf/calc-letter-frequency abc-test-string))))

(deftest score-string
  (testing "We get the correct score")
  (is (< (- abc-test-string-score
            (lf/score-string abc-test-string lf/english-frequencies))
         0.0001)))
