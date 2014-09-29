(ns matasano-clj.letter-frequency
  (:require clojure.string
            [matasano-clj.helpers :as h]))

; Taken from http://www.math.cornell.edu/~mec/2003-2004/cryptography/subs/frequencies.html
(def english-frequencies {\space 0.1918182, \A 0.0651738, \B 0.0124248, \C 0.0217339, \D 0.0349835, \E 0.1041442, \F 0.0197881, \G 0.015861, \H 0.0492888, \I 0.0558094, \J 9.033E-4, \K 0.0050529, \L 0.033149, \M 0.0202124, \N 0.0564513, \O 0.0596302, \P 0.0137645, \Q 8.606E-4, \R 0.0497563, \S 0.051576, \T 0.0729357, \U 0.0225134, \V 0.0082903, \W 0.0171272, \X 0.0013692, \Y 0.0145984, \Z 7.836E-4})

(defn is-letter? [l]
  "Returns whether the given letter is considered a letter for scoring. We only
  score capital letters and spaces."
    (let [n (int l)]
      (or (and (>= n (int \A)) (<= n (int \Z)))
          (= n (int \space)))))

(defn letters-from-string [s]
  "Returns the sequence of valid letters (as determined by is-letter?) present
  in s, ignoring case."
  (filter is-letter? (clojure.string/upper-case s)))

(defn calc-letter-frequency [s]
  "Returns a hash mapping each letter in the alphabet to the number of times it
  appears in the string"
  (let [letters (letters-from-string s)
        freqs (frequencies letters)
        letter-count (count letters)]
    (reduce-kv #(assoc %1 %2 (/ %3 letter-count)) {} freqs)))

(defn score-string [s comparison-frequencies]
  "Scores a string by aggregating the differences in percentage points between
  a given set of frequencies and those found in string."
  (let [freqs (calc-letter-frequency s)
        delta-fn #(Math/abs (-  %1 %2))
        deltas (h/apply-to-corresponding-map-vals delta-fn freqs english-frequencies)]
    ; If the string doesn't contain any letters or spaces, give it the highest possible score
    (if (= (count freqs) 0)
      1
      (reduce + deltas))))
