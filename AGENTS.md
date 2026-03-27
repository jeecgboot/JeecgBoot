# AGENTS.md

## Project Overview

- Monorepo with Java backend (`jeecg-boot`) and Vue3/TS frontend (`jeecgboot-vue3`).
- CI security scanning is defined under `.github/workflows/codeql.yml`
  and `.github/codeql/codeql-config.yml`.

## Setup

- Java: Maven from `jeecg-boot` root (`mvn -f jeecg-boot/pom.xml ...`).
- Frontend: pnpm in `jeecgboot-vue3`.

## Verification Defaults

- For workflow/config changes, verify file syntax and matrix/build settings.
- Keep scanner scope focused on runtime source; exclude generated/vendor/
  test artifacts explicitly.

## PR Rules

- Commit and push branch updates.
- Run PR continuity before creating a new PR.
- Do not dismiss reviews without explicit user direction.
