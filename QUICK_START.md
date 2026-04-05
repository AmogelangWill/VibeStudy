# Icon Integration — Setup Complete

All 21 resource files have been copied to `app/src/main/res/drawable/`.

Rebuild the project to apply the changes:

```bash
./gradlew clean build
```

Or use **Build > Rebuild Project** in Android Studio.

---

## File Mapping

### Subject Icons (Resources/ → drawable/)
| Source | Target |
|--------|--------|
| Accounting.png | ic_subject_accounting.png |
| Economics.png | ic_subject_economics.png |
| English.png | ic_subject_english.png |
| Geography.png | ic_subject_geography.png |
| history.png | ic_subject_history.png |
| Life_Orientation.png | ic_subject_life_orientation.png |
| Life_Science.png | ic_subject_life_science.png |
| Mathematics.png | ic_subject_mathematics.png |
| Mathematics_literacy.png | ic_subject_mathematics_literacy.png |
| physical_science.png | ic_subject_physical_science.png |

### Utility Icons
| Source | Target |
|--------|--------|
| Notes_Icon.png | ic_notes.png |
| add_icon.png | ic_add.png |
| grade.png | ic_grade.png |
| hamburger_icon.png | ic_hamburger.png |
| library_icon.png | ic_library.png |
| schedule_icon.png | ic_schedule.png |
| videos_icon.png | ic_videos.png |

### Logos
| Source | Target |
|--------|--------|
| Header_Logo.png | logo_header.png |
| Logo.png | logo.png |
| V.png | logo_v.png |
| s.png | logo_s.png |

---

## Code Changes Already Applied

1. **SubjectIcons.kt** — maps subjects to icon resources
2. **Buttons.kt** — `RectangularButton` and `SquareButton` accept optional `iconResId`
3. **AppBar.kt** — shows logo and custom hamburger icon
4. **HomeScreen.kt** — grade/library/schedule buttons show icons
5. **SubjectListScreen.kt** and **LibrarySubjectsScreen.kt** — subject buttons show icons

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| "Unresolved reference" errors | Resources not copied yet — run the copy script |
| Script won't execute | `chmod +x RUN_ME_TO_COPY_RESOURCES.sh` |
| Images not showing after build | Confirm all 21 files are in `drawable/` with lowercase underscore names, then run `./gradlew clean build` |

---

## Reference Files

- `ICON_INTEGRATION_SUMMARY.md` — full implementation details
- `RESOURCE_INTEGRATION_GUIDE.md` — manual copy instructions
- `Resources/ReadMe.md` — original project requirements

