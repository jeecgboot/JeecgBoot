# PR Continuity Policy

## Canonical PR Selection

1. Reuse an existing open PR for the same branch if available.
2. If duplicates exist, keep one canonical PR and mark others as duplicates.
3. Keep commit history focused; avoid bundling unrelated fixes.

## Review Flow

- Check review state and CI checks after each push.
- Request AI review when no AI review activity is present.
- Enable auto-merge only when approvals and required checks are green.

## Non-Blocking States

`REVIEW_REQUIRED`, `CHANGES_REQUESTED`, or pending checks are triage
states, not terminal blockers.
