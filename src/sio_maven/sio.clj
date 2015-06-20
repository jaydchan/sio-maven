;; The contents of this file are subject to the LGPL License, Version 3.0.

;; Copyright (C) 2015 Newcastle University

;; This program is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.
;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see http://www.gnu.org/licenses/.

(ns sio-maven.sio
  (:refer-clojure :only [fn let when println])
  (:require [tawny.read]
            [tawny.lookup]))

(clojure.core/defonce specific-replaces
  ^{:private true
    :doc "Map used to made sure that symbols
  {true,false,label,annotation} start with '_' as these can not be
  defined as unique symbols in Clojure"}
  {"annotation" "_annotation", "label" "_label",
   "true" "_true", "false" "_false",
   "e.coli" "e_coli", "implies (->)" "implies",
   "comment" "_comment"})

;; TOFIX Make into function with variable location (default local
;; resource) should be able to use latest from bioontology.
;; "Reads in the sio ontology from resource"
(tawny.read/defread sio
  :location
  (tawny.owl/iri (clojure.java.io/resource "sio.owl"))
  :iri "http://semanticscience.org/ontology/sio.owl"
  :prefix "sio:"
  :filter
  (fn [e]
    ;; (println "Filtering:" e)
    (tawny.read/iri-starts-with-filter
      "http://semanticscience.org/resource/" e))
  :transform
  ;; unlike other entities
  ;; (e.g. http://semanticscience.org/resource/SIO_000001),
  ;; the annotation properties
  ;; (e.g http://semanticscience.org/resource/seeAlso)
  ;; do not need to be transformed as their fragments are not SIO IDs
  ;; also lack the required label annotation
  (fn [e]
    ;; (println "transforming:" e)
    (if (clojure.core/some
              #(do
                 (clojure.core/= (.toString (.getIRI e)) %))
              ["http://semanticscience.org/resource/seeAlso"
               "http://semanticscience.org/resource/similarTo"
               "http://semanticscience.org/resource/narrowerThan"
               "http://semanticscience.org/resource/hasSynonym"
               "http://semanticscience.org/resource/example"
               "http://semanticscience.org/resource/subset"
               "http://semanticscience.org/resource/equivalentTo"
               "http://semanticscience.org/resource/broaderThan"
               "http://semanticscience.org/resource/comment"])
      (do
        ;; (println "Splitting" e)
        (clojure.core/last
         (clojure.string/split (.toString (.getIRI e)) #"/" )))
      (let [t (tawny.read/noisy-nil-label-transform e)]
        ;; (println "Transformed: " e " to " t))
        (tawny.read/stop-characters-transform
         (clojure.core/get specific-replaces t t))))))
