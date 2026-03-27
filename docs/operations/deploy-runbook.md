# Deploy / Operations Runbook

## CI Security Scan Path

This repository uses GitHub Actions CodeQL scanning for Java and
JavaScript/TypeScript.

## Operational Steps

1. Push branch with workflow/config updates.
2. Confirm `CodeQL Scan` workflow triggers on PR.
3. Inspect per-language jobs:
   - `Analyze (java-kotlin)`
   - `Analyze (javascript-typescript)`
4. Validate that alert noise decreases and runtime source remains analyzed.

## Failure Handling

- Java build failures: verify Maven module target and Java version (`17`).
- Parse-noise regressions: review `paths-ignore` for
  generated/template/vendor sources.

## Security Note

Prefer GitHub-hosted CodeQL actions and workflow artifacts over local
scanner execution for canonical evidence.
