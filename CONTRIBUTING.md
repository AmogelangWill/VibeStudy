# Contributing to VibeStudy

## Branching

- Create feature branches from `main` using: `feature/<short-description>`
- Keep each branch focused on one problem area (UI, data pipeline, docs, etc.)

## Commits

- Use clear, imperative commit messages
- Recommended format:
  - `feat: add question-manifest validator`
  - `fix: correct paper rendering for memo toggles`
  - `docs: update quick start and pipeline guide`

## Pull Requests

Before opening a PR:

1. Run Android build: `./gradlew :app:assembleDebug`
2. Run relevant scripts/tests for touched areas
3. Update docs when behavior or setup changes
4. Keep PR scope small and reviewable

PR checklist:

- [ ] Build passes locally
- [ ] No unrelated file changes
- [ ] Documentation updated (if needed)
- [ ] Data-path assumptions explained

## Code Style

- Follow existing Kotlin/Compose conventions in `app/`
- Keep script changes compatible with existing `scripts/` workflow
- Prefer minimal, root-cause fixes over broad refactors

## Issue Reporting

When creating an issue include:

- Expected behavior
- Actual behavior
- Reproduction steps
- Relevant logs/screenshots
- Android/device and OS details (for app issues)
