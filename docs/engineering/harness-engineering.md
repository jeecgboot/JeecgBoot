# Harness Engineering

## Repository Runtime Surfaces

- Backend: Maven multi-module Java project in `jeecg-boot`.
- Frontend: Vite + Vue3 + TypeScript app in `jeecgboot-vue3`.
- CI: GitHub Actions workflows under `.github/workflows`.

## Validation Harness

- Prefer fast, file-scoped validation for config updates.
- For CodeQL changes:
  - verify workflow and config files exist,
  - verify language matrix and build-mode settings,
  - verify path filters include runtime source and exclude
    generated/vendor noise.

## Evidence Storage

Primary evidence must live in:

- commit diffs,
- PR discussion/history,
- GitHub Actions workflow runs.
