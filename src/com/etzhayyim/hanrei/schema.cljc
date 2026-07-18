(ns com.etzhayyim.hanrei.schema)

;; Hanrei Datomic schema — judgement/case-law corpus
;; Entity types: case, judge, opinion, precedent-link

(def schema
  [; Case entity — 判例メタデータ
   {:db/ident :hanrei/case-id
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Unique case identifier (e.g., 最高裁判例ID, Supreme Court docket)"}

   {:db/ident :hanrei/case-title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Case name / title"}

   {:db/ident :hanrei/case-date
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "Decision date"}

   {:db/ident :hanrei/case-summary
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Brief summary / holding"}

   {:db/ident :hanrei/case-court
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Reference to court entity (ooyake)"}

   {:db/ident :hanrei/case-statute
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "References to relevant statutes (houbun)"}

   ; Judge entity — 裁判官
   {:db/ident :hanrei/judge-name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Judge's official name (public role name only, no PII)"}

   {:db/ident :hanrei/judge-court
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Court affiliation"}

   {:db/ident :hanrei/judge-start-date
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "Appointment date"}

   {:db/ident :hanrei/judge-end-date
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "Retirement date (if applicable)"}

   ; Opinion entity — 判決意見
   {:db/ident :hanrei/opinion-type
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc "Opinion type: :majority, :concurring, :dissenting"}

   {:db/ident :hanrei/opinion-author
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Judge who authored opinion"}

   {:db/ident :hanrei/opinion-text
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Full opinion text"}

   {:db/ident :hanrei/opinion-case
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Reference to case"}

   ; Precedent link — 先例関係
   {:db/ident :hanrei/precedent-type
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc "Relationship: :cites, :distinguishes, :overrules, :affirms"}

   {:db/ident :hanrei/precedent-source
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Source case (citing case)"}

   {:db/ident :hanrei/precedent-target
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Target case (cited case)"}

   ; Activity tracking — 活動記録（descriptive only）
   {:db/ident :hanrei/judge-decision-count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "Total decisions written (for descriptive analysis, not ranking)"}

   {:db/ident :hanrei/judge-reversal-rate
    :db/valueType :db.type/double
    :db/cardinality :db.cardinality/one
    :db/doc "Percentage of decisions reversed (for trend analysis)"}

   ; Common metadata
   {:db/ident :hanrei/source
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc "Data source: :jpn-supremecourt, :google-scholar, :curia, etc."}

   {:db/ident :hanrei/source-url
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Original source URL"}

   {:db/ident :hanrei/last-verified
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "Last verification timestamp"}])
