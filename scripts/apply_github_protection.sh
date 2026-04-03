#!/usr/bin/env bash
set -euo pipefail

remote_url="$(git remote get-url origin)"

if [[ "$remote_url" =~ github.com[:/]([^/]+)/([^/.]+)(\.git)?$ ]]; then
  owner="${BASH_REMATCH[1]}"
  repo="${BASH_REMATCH[2]}"
else
  echo "Could not parse GitHub owner/repo from origin URL: $remote_url"
  exit 1
fi

echo "Applying branch protection to ${owner}/${repo} (main)..."

gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  "/repos/${owner}/${repo}/branches/main/protection" \
  -f required_status_checks.strict=true \
  -f required_status_checks.checks[][context]='CI / Android Build + Python Checks' \
  -f required_status_checks.checks[][context]='Dependency Review / dependency-review' \
  -f required_status_checks.checks[][context]='CodeQL / Analyze (java-kotlin)' \
  -f required_status_checks.checks[][context]='CodeQL / Analyze (python)' \
  -f required_status_checks.checks[][context]='Secret Scan / Gitleaks' \
  -F enforce_admins=true \
  -f required_pull_request_reviews.required_approving_review_count=1 \
  -F required_pull_request_reviews.dismiss_stale_reviews=true \
  -F required_pull_request_reviews.require_code_owner_reviews=true \
  -F required_pull_request_reviews.require_last_push_approval=true \
  -F required_linear_history=true \
  -F allow_force_pushes=false \
  -F allow_deletions=false \
  -F block_creations=true \
  -F required_conversation_resolution=true

echo "Branch protection applied successfully."
