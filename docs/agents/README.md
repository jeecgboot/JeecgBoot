# Agents / Skills / MCP Usage

## Defaults

- Use subagents for analysis and verification when tasks span multiple streams.
- Use memory to persist durable repo/workflow decisions.
- Use PR continuity checks before creating or updating PRs.

## For CI/Security Workflow Changes

Recommended support stack:

- `systematic-debugging` for root cause isolation,
- `github-actions-templates` for workflow structure,
- `verification-before-completion` before claiming done,
- `pr-continuity` before opening/updating PRs.

## Expected Outputs

- Minimal, production-safe config changes.
- Clear path-scoping rationale to preserve coverage while reducing
  scanner noise.
