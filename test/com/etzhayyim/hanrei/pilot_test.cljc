(ns com.etzhayyim.hanrei.pilot-test
  (:require [com.etzhayyim.hanrei.ingest :as ingest]
            [com.etzhayyim.hanrei.schema :as schema]
            #?(:clj [clojure.test :refer [deftest is testing]]
               :cljs [cljs.test :refer-macros [deftest is testing]])))

(deftest schema-definition
  (testing "Schema contains expected attribute definitions"
    (is (vector? schema/schema))
    (is (> (count schema/schema) 0))
    ;; Validate presence of core entity types
    (let [idents (map :db/ident schema/schema)]
      (is (contains? (set idents) :hanrei/case-id))
      (is (contains? (set idents) :hanrei/judge-name))
      (is (contains? (set idents) :hanrei/opinion-type))
      (is (contains? (set idents) :hanrei/precedent-type)))))

(deftest ^:pilot build-case-entity
  (testing "Transform raw API response → Datomic entity"
    (let [raw {:case_id "2026-001"
               :title "Test Case"
               :decision_date "2026-07-18"
               :summary "Test holding"
               :full_text_url "http://example.com"}
          entity (ingest/build-case-entity raw)]
      (is (= (:hanrei/case-id entity) "2026-001"))
      (is (= (:hanrei/case-title entity) "Test Case"))
      (is (= (:hanrei/source entity) :jpn-supremecourt))
      (is (contains? entity :hanrei/last-verified)))))

(deftest ^:pilot build-judge-entity
  (testing "Transform judge data → Datomic entity"
    (let [raw {:name "Judge Yamada"
               :appointed_at "2020-01-01"
               :retired_at nil}
          entity (ingest/build-judge-entity raw)]
      (is (= (:hanrei/judge-name entity) "Judge Yamada"))
      (is (= (:hanrei/source entity) :jpn-supremecourt)))))

(deftest ^:pilot build-opinion-entity
  (testing "Transform opinion data → Datomic entity"
    (let [raw {:type :majority
               :text "The court finds..."}
          entity (ingest/build-opinion-entity raw)]
      (is (= (:hanrei/opinion-type entity) :majority))
      (is (string? (:hanrei/opinion-text entity))))))

(deftest ^:pilot ingest-phase-1-placeholder
  (testing "Phase 1 ingest returns expected shape (placeholder for API integration)"
    (let [result (ingest/ingest-phase-1 nil)]
      ;; Phase 1 API integration is TODO; test checks result shape
      (is (map? result))
      (is (contains? result :ingested-count))
      (is (contains? result :errors))
      (is (vector? (:errors result))))))
