(ns matasano-clj.helpers
  (:require clojure.set))

; Integers are signed, so the range of integers that will fit into a byte are
; from -128 to 127.
(def all-bytes (map byte (range -128 128)))

(defn hex-string-to-bytes [s]
  "Convert a character string to the seq of bytes it represents. String
  must be in hex and contain an even number of characters."
  (assert (even? (count s)))
  (let [char-pairs (partition 2 s)
        int-pairs (map #(Integer/parseInt (reduce str %) 16) char-pairs)]
    (map byte int-pairs)))

(defn bytes-to-hex-string [coll]
  "Output a string of hex characters representing the given collection of bytes."
  (reduce str (map #(format "%02x" %) coll)))

(defn byte-to-base64-char [i]
  "Returns the base64 encoding for the given integer"
  (assert (or (nil? i) (< i 64)))
  (cond
    ; nils are padding bytes - output = in line with wiki explanation
    (nil? i) \=
    ; First 26 numbers are capital letters A-Z
    (< i 26) (char (+ i 65))
    (< i 52) (char (+ (- i 26) 97))
    (< i 62) (char (+ (- i 52) 48))
    (= i 62) \+
    (= i 63) \/))

(defn next-base64 [bite overflow next-overflow-size]
  "Takes a byte, overflow, and the size of the next top, and returns the next
  base64 character to be output."
  (let [left-mask (bit-shift-left 0xff next-overflow-size)
        left-bits (bit-and left-mask bite)
        b64-bits-no-overflow (bit-shift-right left-bits next-overflow-size)]
    (bit-or b64-bits-no-overflow overflow)))

(defn next-overflow [bite next-overflow-size]
  (let [mask (bit-shift-right 0xff (- 8 next-overflow-size))
        bits (bit-and mask bite)]
    (bit-shift-left bits (- 6 next-overflow-size))))

(defn three-bytes-to-base64-bytes [coll]
  "Takes a triplet of bytes and returns the corresponding base64 bytes. nil
  bytes are output as \\=, as per base64 padding rules on Wikipedia. For
  example, one byte and 2 nils will output 2 chars for the byte, and two \\= 
  characters as padding bytes."
  (assert (<= (count coll) 3))
  (if (empty? coll)
    '()
    ; Any non-significant trailing bytes are output as '='. Note this is BYTES,
    ; not groups of 6 bits.
    (let [padding (cond (= (count coll) 1) '(nil nil)
                        (= (count coll) 2) '(nil)
                        :else '())]
      (loop [bytez coll
             base64 '()
             overflow 0
             next-overflow-size 2]
        (if (empty? bytez)
          ; No bytes left? Spit out what we have, including overflow.
          (apply conj padding (cons overflow base64))

          (let [base64-byte (next-base64 (first bytez) overflow next-overflow-size)]
            (recur (rest bytez)
                   (cons base64-byte base64)
                   (next-overflow (first bytez) next-overflow-size)
                   (+ next-overflow-size 2))))))))

(defn bytes-to-base64-chars [coll]
  "Takes a collection of bytes and returns a base64 string. String is padded
  according to standard base64 padding rulles."
  ; Work in groups of 3 so that our padding is accurate
  (let [byte-triplets (partition-all 3 coll)
        base64-bytes (flatten (map three-bytes-to-base64-bytes byte-triplets))]
    (map byte-to-base64-char base64-bytes)))

(defn convert-hex-to-b64 [s]
  "Set1 - Challenge 1. Converts a string of hexadecimal digits to a base64
  encoded string. hex must have an even length"
  (reduce str (bytes-to-base64-chars (hex-string-to-bytes s))))

(defn fixed-xor [a b]
  "Set 1 - Challenge 2. Bitwise xor 2 equal length buffers."
  (map bit-xor a b))

(defn fixed-xor-hex-strings [a b]
  (assert (= (count a) (count b)))
  (let [bytes-a (hex-string-to-bytes a)
        bytes-b (hex-string-to-bytes b)]
    (bytes-to-hex-string (fixed-xor bytes-a bytes-b))))

(defn apply-to-corresponding-map-vals [f a b]
  "Applies f to each pair of values in a and b that share the same key and
  returns the sequence of results."
  (let [shared-keys (clojure.set/intersection (set (keys a))
                                              (set (keys b)))]
    (map #(f (a %) (b %)) shared-keys)))

(defn char-from-byte [b]
  "Returns the character for the given byte"
  (char (if (< b 0) (+ 127 (Math/abs b)) b)))
