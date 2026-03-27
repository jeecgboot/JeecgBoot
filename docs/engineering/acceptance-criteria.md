# Engineering Acceptance Criteria

## Scope

This repository contains a Java backend (`jeecg-boot`) and a
Vue3/TypeScript frontend (`jeecgboot-vue3`).

## Completion Rules

1. Every change must have a reproducible verification step (command
   output or workflow run).
2. Security or CI pipeline changes must include explicit rationale and
   affected paths.
3. Pull requests must be linked to a canonical PR path and avoid
   duplicate/open competing PRs.
4. For scanning workflow updates, success requires:
   - workflow syntax validity,
   - per-language CodeQL job separation,
   - noise controls that do not exclude application runtime source.

## Minimum Verification

- Workflow/config changes: validate YAML correctness and inspect generated diff.
- Repository policy/docs changes: markdown renders and paths exist under `docs/**`.

## Quality Bar

- Avoid placeholder TODOs in merged workflow files.
- Exclusions must be precise and explainable (no blanket `**/*`
  removal of runtime code).
- Keep production source coverage for:
  - `jeecg-boot/**` (Java runtime modules)
  - `jeecgboot-vue3/src/**` (frontend runtime code)
