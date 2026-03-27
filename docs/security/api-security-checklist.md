# API Security Checklist

Applicable when backend API behavior or security boundaries change.

## Baseline

- Authentication and authorization checks remain enforced.
- No secrets committed to repository.
- Dependency sources remain trusted and pinned by build tooling.

## For This Repository

- Java service endpoints are implemented under `jeecg-boot` modules.
- CI security scanning (CodeQL) must keep runtime code in scope while
  filtering generated/vendor artifacts.

## PR Gate

- Document any new exclusion in security scans and why it does not
  remove runtime attack surface coverage.
