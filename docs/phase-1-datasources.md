# Phase 1 Data Sources — Pilot Selection

## Rationale

Start with **Japan Supreme Court** (最高裁判例情報システム) as Phase 1 pilot:
- Public REST API available (no auth required)
- Structured XML/JSON responses
- Moderate volume (~5K decisions/year, total ~50K historical)
- Direct relevance (etzhayyim/root is JP-centric)
- No rate-limiting constraints documented

## Selected Source: Japan Supreme Court API

**Endpoint**: `https://www.courts.go.jp/app/hanrei_jp/`

**API Details**:
- REST endpoint: `https://www.courts.go.jp/search/ajax/hanrei_ajax.html`
- Query params: `sort=asc&page=<n>&count=<limit>`
- Response format: JSON with decision metadata + full text URL
- Auth: None (public data)

**Data Structure**:
- Case ID: `case_id` (unique within SC database)
- Title: `title`
- Decision date: `decision_date` (YYYY-MM-DD)
- Full text URL: `full_text_url` (PDF or HTML)
- Court type: `:jpn-supremecourt`

## Phase 1 Pilot Plan

1. **Week 1**: Fetch 1,000 most recent decisions from API
2. **Week 2**: Parse full-text PDFs/HTML, extract holdings + statute references
3. **Week 3**: Ingest into kotoba Datomic schema
4. **Week 4**: Validate precedent linking (cross-reference detection)

## Phase 2 Candidates (TBD)

- US Supreme Court (Google Scholar scrape / RECAP)
- ECURIA (EU Court of Justice, CURIA API)
- ICC (International Criminal Court, public records)

## Constraints

- **Activity tracking**: Describe judge involvement, NO ranking/audit
- **Sourcing**: Public API/official sources only
- **PII**: Judge names = public role names only, no addresses/contacts
