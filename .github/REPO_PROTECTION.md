# Repository Protection Checklist

This repository now includes in-repo protections:

- CI build workflow
- CodeQL scanning workflow
- Secret scanning workflow (Gitleaks)
- Dependency review workflow
- Dependabot updates
- CODEOWNERS
- PR template and issue templates

## Apply GitHub Server-Side Rules (Required)

GitHub branch protection/rulesets must be applied with admin rights in repository settings or via `gh api`.

### Option A: GitHub UI (recommended)

1. Go to repository `Settings` → `Rules` → `Rulesets`.
2. Create ruleset for branch `main`.
3. Enable these rules:
   - Require a pull request before merging
   - Require approvals: at least 1
   - Dismiss stale approvals when new commits are pushed
   - Require status checks to pass before merging
   - Require branches to be up to date before merging
   - Block force pushes
   - Block branch deletion
4. Required status checks:
   - `CI / Android Build + Python Checks`
   - `Dependency Review / dependency-review`
   - `CodeQL / Analyze (java-kotlin)`
   - `CodeQL / Analyze (python)`
   - `Secret Scan / Gitleaks`

### Option B: CLI script

Run:

```bash
gh auth login
bash scripts/apply_github_protection.sh
```
