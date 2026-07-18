(ns com.etzhayyim.hanrei.ingest
  (:require [com.etzhayyim.hanrei.schema :as schema]
            [clojure.string :as str]))

(defn fetch-japan-supreme-court-decisions
  "Fetch decisions from Japan Supreme Court API (Phase 1 pilot)

   Phase 1 endpoint: https://www.courts.go.jp/search/ajax/hanrei_ajax.html
   Query params: sort=asc&page=<n>&count=<limit>
   Returns: [{:case-id, :title, :decision-date, :full-text-url, ...}]"
  [{:keys [page-size max-pages]}]
  (let [endpoint "https://www.courts.go.jp/search/ajax/hanrei_ajax.html"
        params {:sort "asc" :page 1 :count (or page-size 100)}]
    ;; TODO: implement HTTP fetch + JSON parse
    ;; For now, returns empty placeholder
    []))

(defn extract-statute-references
  "Extract statute/law references from case opinion text

   Pattern: 「...法第...条」（e.g., 民法第123条）
   Returns: [{:statute-id, :article-no, :confidence}]"
  [opinion-text]
  ;; Regex-based extraction from Japanese legal citations
  ;; TODO: implement regex patterns for statute citation detection
  [])

(defn build-case-entity
  "Transform raw API response → Datomic entity

   Args: raw-case — response object from JP Supreme Court API
   Returns: Datomic tx data (map with :db/id and hanrei/* attributes)"
  [raw-case]
  {:db/id -1
   :hanrei/case-id (:case_id raw-case)
   :hanrei/case-title (:title raw-case)
   :hanrei/case-date (:decision_date raw-case)
   :hanrei/case-summary (:summary raw-case)
   :hanrei/source :jpn-supremecourt
   :hanrei/source-url (:full_text_url raw-case)
   :hanrei/last-verified #inst "2026-07-18"})

(defn build-judge-entity
  "Transform raw judge data → Datomic entity

   Args: raw-judge — judge metadata from case opinion
   Returns: Datomic tx data with hanrei/judge-* attributes"
  [raw-judge]
  {:db/id -1
   :hanrei/judge-name (:name raw-judge)
   :hanrei/judge-start-date (:appointed_at raw-judge)
   :hanrei/judge-end-date (:retired_at raw-judge)
   :hanrei/source :jpn-supremecourt})

(defn build-opinion-entity
  "Transform raw opinion data → Datomic entity

   Args: raw-opinion — opinion metadata (majority/concurring/dissenting)
   Returns: Datomic tx data with hanrei/opinion-* attributes"
  [raw-opinion]
  {:db/id -1
   :hanrei/opinion-type (:type raw-opinion) ; :majority, :concurring, :dissenting
   :hanrei/opinion-text (:text raw-opinion)
   :hanrei/opinion-author -2 ; Will be resolved in multi-entity tx
   :hanrei/opinion-case -1})

(defn ingest-phase-1
  "Phase 1 pilot: fetch + parse 1,000 recent JP Supreme Court decisions

   Args: datomic-conn — connection to kotoba Datomic instance
   Returns: {:ingested-count, :errors}"
  [datomic-conn]
  (let [raw-decisions (fetch-japan-supreme-court-decisions
                        {:page-size 100 :max-pages 10})
        entities (mapv build-case-entity raw-decisions)]
    ;; TODO: transact entities into kotoba Datomic
    ;; (d/transact datomic-conn {:tx-data entities})
    {:ingested-count (count entities)
     :errors []}))

;; TODO:
;;  - Implement HTTP fetch from JP Supreme Court API
;;  - Parse statute references from opinion text
;;  - Build precedent linking (cites/distinguishes/overrules detection)
;;  - Judge activity aggregation (decision count, reversal rate tracking)
;;  - Integration with kotoba Datomic connection pool
