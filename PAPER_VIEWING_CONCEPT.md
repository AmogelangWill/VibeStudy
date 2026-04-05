# Paper Viewing Concept - Approved Design

## Your Vision (Validated ✅)

### User Flow:
1. User navigates to a specific paper (e.g., Mathematics Grade 12, 2023, June, Paper 1)
2. Screen displays questions **one by one** in a scrollable list
3. Each question shows:
   - Question screenshot (full-width)
    - Two buttons below: **Ask Tutor** | **View Memo**
4. Tapping "View Memo" reveals the memo for that specific question
5. Each question's memo toggles independently

### Key Design Principles:
- ✅ **Full-width display** - Questions and memos fill the screen width
- ✅ **Screenshot-based** - Not text extraction, actual paper images
- ✅ **Independent toggles** - Each question's memo works separately
- ✅ **Clean spacing** - Each card is visually separated
- ✅ **Equal-width buttons** - Ask Tutor and View Memo are balanced

## Technical Implementation (Concept)

### Data Structure:
```kotlin
data class Question(
    val id: String,
    val questionImageUrl: String,  // URL or path to question screenshot
    val memoImageUrl: String,      // URL or path to memo screenshot
    val memoText: String?,         // Optional: extracted text for processing
    val marks: Int
)
```

### Component Structure:
```
PaperViewScreen
└── LazyColumn
    └── QuestionCard (repeated for each question)
        ├── Question Image (full-width)
        ├── Button Row
        │   ├── Ask Tutor Button (50% width)
        │   └── View Memo Button (50% width)
        └── Memo Image (full-width, toggle-able)
```

### Color Scheme (from prototype):
- Question backgrounds: Light blue tint (#F5F5FA)
- Memo backgrounds: Light beige tint (#FAFAF5)
- Cards: White with subtle elevation
- Spacing: 12-16dp between elements

## Future Enhancements to Consider

### Phase 1 (MVP):
- [ ] Load real question/memo screenshots
- [ ] Implement basic toggle functionality
- [ ] Add loading states

### Phase 2 (Enhanced):
- [ ] Offline caching of papers
- [ ] Bookmark specific questions
- [ ] Track which questions you've viewed

### Phase 3 (Guided Help Integration):
- [ ] Connect Ask Tutor to support service
- [ ] Pass memo context to service
- [ ] Display help explanations in dialog/sheet

### Phase 4 (Advanced):
- [ ] Auto-split PDFs into question images server-side
- [ ] OCR for extracting memo text
- [ ] Question difficulty rating support
- [ ] Study analytics (which questions take longest)

## Differences from Other Apps

Your app will stand out because:
1. **Question-by-question** display vs. full PDF viewer
2. **Integrated help** directly under each question
3. **Quick memo access** without scrolling to end of paper
4. **Combined ecosystem** (notes, videos, schedule, papers) in one app

## Storage Considerations

### For 14.5GB of papers:
- **Firebase Storage**: ~$0.026/GB/month = ~$0.38/month
- **Backblaze B2**: ~$0.005/GB/month = ~$0.07/month
- **Cloudflare R2**: Free up to 10GB, then $0.015/GB = ~$0.07/month

### Recommended approach:
1. Store papers in cloud storage
2. Cache recently viewed papers on device
3. Let users download specific papers for offline use
4. Implement smart cache management (auto-delete old cached papers)

## Next Steps (When Ready)

1. **Gather sample papers** - Start with 2-3 papers for testing
2. **Split into question images** - Use a PDF splitter or manual screenshots
3. **Choose storage solution** - Firebase is easiest to start
4. **Build real PaperViewScreen** - Based on approved prototype
5. **Test with real data** - Verify loading, caching, UX
6. **Iterate based on feedback**

---

**Status**: Concept approved ✅
**Prototype**: Validated and removed
**Ready for**: Real implementation with actual paper data

