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

payload_file="$(mktemp)"

cat > "$payload_file" <<'JSON'
{
  "required_status_checks": {
    "strict": true,
    "contexts": [
      "CI / Android Build + Python Checks",
      "Dependency Review / dependency-review",
      "CodeQL / Analyze (java-kotlin)",
      "CodeQL / Analyze (python)",
      "Secret Scan / Gitleaks"
    ]
  },
  "enforce_admins": true,
  "required_pull_request_reviews": {
    "dismiss_stale_reviews": true,
    "require_code_owner_reviews": true,
    "required_approving_review_count": 1,
    "require_last_push_approval": true
  },
  "restrictions": null,
  "required_linear_history": true,
  "allow_force_pushes": false,
  "allow_deletions": false,
  "required_conversation_resolution": true,
  "lock_branch": false,
  "allow_fork_syncing": false
}
JSON

gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  "/repos/${owner}/${repo}/branches/main/protection" \
  --input "$payload_file"

rm -f "$payload_file"

echo "Branch protection applied successfully."
