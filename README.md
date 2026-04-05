# VibeStudy Project

VibeStudy is an Android learning app focused on South African high-school exam preparation with structured navigation by Grade → Subject → Year → Paper, question-first paper viewing, memo toggles, and data-processing scripts for large PDF collections.

## Current Scope

- Android app (Kotlin + Jetpack Compose + Material 3)
- Grade/subject/year/month paper browsing flow
- Schedule/planner feature with conflict checks
- Styled navigation drawer and About screen
- Paper viewing architecture with per-question memo toggle pattern
- Local automation pipeline for downloading, organizing, and splitting papers

## Repository Structure

- `app/` Android application module
- `scripts/` Python automation scripts for data acquisition and processing
- `DATABASE/` local paper dataset and processing outputs
- `Resources/` icons, logos, and original requirements references

## Quick Start

### Prerequisites

- JDK 17+
- Android SDK + Android Studio
- Python 3.10+

### Build Android App

```bash
./gradlew :app:assembleDebug
```

### Run Data Pipeline (Example)

```bash
python3 scripts/organize_papers.py
python3 scripts/process_all_pdfs.py
```

## Documentation Index

- Product and implementation notes: `IMPLEMENTATION_NOTES.md`
- Processing system: `PDF_CROPPING_SYSTEM.md`
- Project status reports: `*_SUMMARY.md`, `*_COMPLETE.md`
- Operational help: `HOW_TO_USE.md`, `QUICK_REFERENCE.md`, `DOWNLOAD_GUIDE.md`
- Future roadmap: `FUTURE_PLANS.md`

## Project Status

Core infrastructure is in place and functional for MVP-level paper browsing and rendering. The largest remaining work is dataset expansion and production-grade question/memo mapping quality across subjects and years.

## Contributing

See `CONTRIBUTING.md` for branch, commit, and pull-request guidance.

## Security

See `SECURITY.md` for vulnerability reporting.
