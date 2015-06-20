;; The contents of this file are subject to the LGPL License, Version 3.0.

;; Copyright (C) 2015, Newcastle University

;; This program is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program. If not, see http://www.gnu.org/licenses/.

(ns sio-maven.sio_test
  (:use [clojure.test])
  (:require
   [tawny.owl :only [ontology-to-namespace]]
   [tawny.read :only [iri-starts-with-filter]]
   [tawny.reasoner :as r]
   [sio-maven.sio :as s])
  (:import (org.semanticweb.owlapi.model
            OWLOntology
            OWLSubClassOfAxiom
            OWLEquivalentClassesAxiom
            OWLDisjointClassesAxiom
            OWLSubObjectPropertyOfAxiom
            OWLInverseObjectPropertiesAxiom
            OWLFunctionalObjectPropertyAxiom
            OWLInverseFunctionalObjectPropertyAxiom
            OWLTransitiveObjectPropertyAxiom
            OWLSymmetricObjectPropertyAxiom
            OWLAsymmetricObjectPropertyAxiom
            OWLReflexiveObjectPropertyAxiom
            OWLIrreflexiveObjectPropertyAxiom
            OWLObjectPropertyDomainAxiom
            OWLObjectPropertyRangeAxiom
            OWLSubPropertyChainOfAxiom
            OWLFunctionalDataPropertyAxiom
            OWLAnnotationAssertionAxiom)))

(defn ontology-reasoner-fixture [tests]
  (r/reasoner-factory :hermit)
  (tawny.owl/ontology-to-namespace s/sio)
  (binding [r/*reasoner-progress-monitor*
            (atom
            r/reasoner-progress-monitor-silent)]
    (tests)))

(use-fixtures :once ontology-reasoner-fixture)

(deftest Basic
  (is (r/consistent?))
  (is (r/coherent?)))

(deftest ontology
  (is
   (instance? OWLOntology s/sio)))

;; TOFIX: Check (no statistic in Protege)
(deftest signature
  (is
   (= 1688
      (count (filter
              (partial
               tawny.read/iri-starts-with-filter
               "http://semanticscience.org/resource/")
              (.getSignature s/sio))))))

;; TOFIX: Interval label serror till exists in latest version
;; TOFIX: New label error: Satifaction
(deftest classes
  (is
   (= 1471
      (count (.getClassesInSignature s/sio)))))

(deftest oproperties
  (is
   (= 207
      (count (.getObjectPropertiesInSignature s/sio)))))

;; TOFIX: Check (no statistic in Protege)
(deftest aproperties
  (is
   (= 25
      (count (.getAnnotationPropertiesInSignature s/sio))))
  (is
   (= 9
      (count (filter
              (partial
               tawny.read/iri-starts-with-filter
               "http://semanticscience.org/resource/")
              (.getAnnotationPropertiesInSignature s/sio))))))

(deftest dproperties
  (is
   (= 1
      (count (.getDataPropertiesInSignature s/sio)))))

(deftest axioms
  (is
   (= 7733
      (count (.getAxioms s/sio)))))

(deftest subclass-of-axioms
  (is
   (= 1836
      (count
       (filter
        #(instance? OWLSubClassOfAxiom %)
        (.getAxioms s/sio))))))

(deftest equivalent-classes-axioms
  (is
   (= 44
      (count
       (filter
        #(instance? OWLEquivalentClassesAxiom %)
        (.getAxioms s/sio))))))

(deftest disjoint-classes-axioms
  (is
   (= 80
      (count
       (filter
        #(instance? OWLDisjointClassesAxiom %)
        (.getAxioms s/sio))))))

(deftest sub-oproperty-of-axioms
  (is
   (= 213
      (count
       (filter
        #(instance? OWLSubObjectPropertyOfAxiom %)
        (.getAxioms s/sio))))))

(deftest inverse-oproperty-axioms
  (is
   (= 84
      (count
       (filter
        #(instance? OWLInverseObjectPropertiesAxiom %)
        (.getAxioms s/sio))))))

(deftest functional-oproperty-axioms
  (is
   (= 13
      (count
       (filter
        #(instance? OWLFunctionalObjectPropertyAxiom %)
        (.getAxioms s/sio))))))

(deftest inverse-functional-oproperty-axioms
  (is
   (= 8
      (count
       (filter
        #(instance? OWLInverseFunctionalObjectPropertyAxiom %)
        (.getAxioms s/sio))))))

(deftest transitive-oproperty-axioms
  (is
   (= 23
      (count
       (filter
        #(instance? OWLTransitiveObjectPropertyAxiom %)
        (.getAxioms s/sio))))))

(deftest symmetric-oproperty-axioms
  (is
   (= 35
      (count
       (filter
        #(instance? OWLSymmetricObjectPropertyAxiom %)
        (.getAxioms s/sio))))))

(deftest asymmetric-oproperty-axioms
  (is
   (= 2
      (count
       (filter
        #(instance? OWLAsymmetricObjectPropertyAxiom %)
        (.getAxioms s/sio))))))

(deftest reflexive-oproperty-axioms
  (is
   (= 3
      (count
       (filter
        #(instance? OWLReflexiveObjectPropertyAxiom %)
        (.getAxioms s/sio))))))

(deftest irreflexive-oproperty-axioms
  (is
   (= 3
      (count
       (filter
        #(instance? OWLIrreflexiveObjectPropertyAxiom %)
        (.getAxioms s/sio))))))

(deftest oproperty-domain-axioms
  (is
   (= 28
      (count
       (filter
        #(instance? OWLObjectPropertyDomainAxiom %)
        (.getAxioms s/sio))))))

(deftest oproperty-range-axioms
  (is
   (= 31
      (count
       (filter
        #(instance? OWLObjectPropertyRangeAxiom %)
        (.getAxioms s/sio))))))

(deftest subproperty-chain-of-axioms
  (is
   (= 1
      (count
       (filter
        #(instance? OWLSubPropertyChainOfAxiom %)
        (.getAxioms s/sio))))))

(deftest functional-dproperty-axioms
  (is
   (= 1
      (count
       (filter
        #(instance? OWLFunctionalDataPropertyAxiom %)
        (.getAxioms s/sio))))))

(deftest annotation-assertions
  (is
   (= 3629
      (count
       (filter
        #(instance? OWLAnnotationAssertionAxiom %)
        (.getAxioms s/sio))))))

;; Signature (1688) + Specific Replaces Map (1) + Ontology (1)
(deftest ontology-symbols
  (is
   (= 1690
      (count
       (ns-interns 'sio-maven.sio)))))
