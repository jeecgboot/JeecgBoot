# Architecture

## Monorepo Layout

- `jeecg-boot/`: Java multi-module backend (Maven).
- `jeecgboot-vue3/`: Vue3 + TypeScript frontend (Vite).
- `.github/workflows/`: CI workflows.
- `.github/codeql/`: CodeQL scan configuration.

## Security Scanning Architecture

- CodeQL runs as a language matrix:
  - `java-kotlin` with manual Maven build for stable extraction.
  - `javascript-typescript` with scoped source analysis.
- Central path policy in `.github/codeql/codeql-config.yml`:
  - include backend and frontend runtime sources,
  - exclude generated, vendor, static bundle, and test paths to
    reduce parser/duplicate noise.

## Rationale

The repository contains both runtime code and template/static assets.
Scoped CodeQL extraction is required to keep security signal high and
avoid SARIF warning saturation from non-runtime files.
