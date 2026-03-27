# One-Day Delivery Plan

## Objective

Deliver small, reviewable changes within a day with CI verification and
PR continuity.

## Flow

1. Reproduce and classify issue from logs/alerts.
2. Apply minimal code/config changes.
3. Run local validation commands relevant to changed files.
4. Commit and push to a dedicated branch.
5. Reuse existing canonical PR when present; otherwise open one new PR.
6. Request AI review signal once and keep PR body synchronized with
   review guidance.
7. Enable auto-merge after approvals and required checks pass.

## Constraints

- Do not force-push protected branches.
- Do not dismiss reviews without explicit user instruction.
- Split unrelated fixes into follow-up PRs.
