# Paper Viewing Concept

## Design

### User flow
1. User navigates to a paper (e.g., Mathematics Grade 12, 2023, June, Paper 1)
2. Screen displays questions one by one in a scrollable list
3. Each question shows:
   - Full-width question screenshot
   - Two buttons below: **Ask AI** | **View Memo**
4. Tapping "View Memo" reveals the memo for that specific question
5. Each question's memo toggles independently

### Key design principles
- Full-width display — questions and memos fill the screen width
- Screenshot-based — actual paper images, not extracted text
- Independent toggles per question
- Equal-width buttons (Ask AI and View Memo)

---

## Technical Structure

### Data model
```kotlin
data class Question(
    val id: String,
    val questionImageUrl: String,
    val memoImageUrl: String,
    val memoText: String?,
    val marks: Int
)
```

### Component hierarchy
```
PaperViewScreen
└── LazyColumn
    └── QuestionCard (per question)
        ├── Question Image (full-width)
        ├── Button Row
        │   ├── Ask AI Button (50% width)
        │   └── View Memo Button (50% width)
        └── Memo Image (full-width, toggle-able)
```

---

## Storage Considerations

For the full paper dataset (~14.5 GB):

| Provider | Cost estimate |
|----------|---------------|
| Firebase Storage | ~$0.026/GB/month |
| Backblaze B2 | ~$0.005/GB/month |
| Cloudflare R2 | Free up to 10 GB, then $0.015/GB |

Recommended approach: store papers in cloud storage, cache recently viewed papers on device, allow user-initiated offline downloads, and implement a cleanup policy for stale cached files.

---

## Planned Implementation Phases

### Phase 1 (MVP)
- Load real question/memo screenshots
- Implement toggle functionality
- Add loading states

### Phase 2 (Enhanced)
- Offline caching
- Bookmark specific questions
- Track viewed questions

### Phase 3 (AI integration)
- Connect Ask AI to an AI service
- Pass memo context to AI
- Display explanations in a dialog or bottom sheet

### Phase 4 (Advanced)
- Server-side auto-split of PDFs into question images
- OCR for memo text extraction
- AI-powered question difficulty rating
- Study analytics

---

**Status**: Concept approved, prototype validated and removed, ready for real implementation.

