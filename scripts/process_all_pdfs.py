#!/usr/bin/env python3
"""
Process all PDFs in DATABASE folder, split pages intelligently, and update manifests.
"""
import os
import json
import subprocess
import sys
import re
from pathlib import Path
import math

import numpy as np
from PIL import Image, ImageFilter
import fitz  # PyMuPDF

# PDF mapping based on user's notes
PDF_INFO = {
    "Mathematics P1 Nov 2024 Eng.pdf": {
        "grade": "12",
        "subject": "Mathematics",
        "year": "2024",
        "month": "November",
        "paper": "P1",
        "type": "question",
        "page_questions": {
            1: None,  # cover
            2: None,  # rules
            3: ["Q1", "Q2"],
            4: ["Q3", "Q4"],
            5: ["Q5"],
            6: ["Q6"],
            7: ["Q7", "Q8"],
            8: ["Q9", "Q10"],
            9: ["Q11", "Q12"],
            10: None  # info page
        }
    },
    "Mathematics P1 Nov 2024 MEMO.pdf": {
        "grade": "12",
        "subject": "Mathematics",
        "year": "2024",
        "month": "November",
        "paper": "P1",
        "type": "memo",
        "page_questions": {
            1: None,  # cover
            2: ["Q1"],
            3: ["Q1"],
            4: ["Q2"],
            5: ["Q3"],
            6: ["Q3"],
            7: ["Q4"],
            8: ["Q5"],
            9: ["Q6"],
            10: ["Q6"],
            11: ["Q7"],
            12: ["Q7"],
            13: ["Q7"],
            14: ["Q8"],
            15: ["Q8"],
            16: ["Q9", "Q10"],
            17: ["Q11", "Q12"]
        }
    },
    "Physical Sciences P1 Nov 2024 Eng.pdf": {
        "grade": "12",
        "subject": "Physical_Sciences",
        "year": "2024",
        "month": "November",
        "paper": "P1",
        "type": "question",
        "page_questions": {
            1: None,  # cover
            2: None,  # rules
            3: ["Q1"],
            4: ["Q1"],
            5: ["Q1"],
            6: ["Q1"],
            7: ["Q1"],
            8: ["Q2"],
            9: ["Q3"],
            10: ["Q4"],
            11: ["Q5"],
            12: ["Q6"],
            13: ["Q7"],
            14: ["Q8"],
            15: ["Q9"],
            16: ["Q10"],
            17: None  # info page
        }
    },
    "Physical Sciences P1 Nov 2024 MEMO.pdf": {
        "grade": "12",
        "subject": "Physical_Sciences",
        "year": "2024",
        "month": "November",
        "paper": "P1",
        "type": "memo",
        "page_questions": {
            1: None,  # cover
            2: ["Q1"],
            3: ["Q2"],
            4: ["Q2"],
            5: ["Q3"],
            6: ["Q3"],
            7: ["Q3"],
            8: ["Q3"],
            9: ["Q3"],
            10: ["Q3"],
            11: ["Q4"],
            12: ["Q4"],
            13: ["Q5"],
            14: ["Q5"],
            15: ["Q6"],
            16: ["Q7"],
            17: ["Q7"],
            18: ["Q8"],
            19: ["Q8"],
            20: ["Q9"],
            21: ["Q10"],
            22: ["Q10"]
        }
    }
}


def find_question_split_point(img):
    """
    Intelligently find where to split a page with 2 questions.
    Uses whitespace detection to find the best split point.
    """
    # Convert to grayscale
    gray = img.convert('L')

    # Enhance edges
    edges = gray.filter(ImageFilter.FIND_EDGES)

    # Convert to numpy array
    arr = np.array(gray)
    height, width = arr.shape

    # Calculate horizontal density (how much content per row)
    # Lower values = more whitespace
    row_density = []
    for y in range(height):
        # Count non-white pixels in this row
        row = arr[y, :]
        density = np.sum(row < 240)  # Count darker pixels
        row_density.append(density)

    # Find the middle third of the page where we expect the split
    start_search = height // 3
    end_search = 2 * height // 3

    # Find rows with minimal content in the middle section
    # Look for the largest gap (continuous whitespace)
    best_split = height // 2  # Default to middle
    max_gap_size = 0
    current_gap_start = None
    current_gap_size = 0

    whitespace_threshold = width * 0.05  # Less than 5% of row has content

    for y in range(start_search, end_search):
        if row_density[y] < whitespace_threshold:
            if current_gap_start is None:
                current_gap_start = y
                current_gap_size = 1
            else:
                current_gap_size += 1
        else:
            if current_gap_size > max_gap_size:
                max_gap_size = current_gap_size
                # Split in the middle of the gap
                best_split = current_gap_start + current_gap_size // 2
            current_gap_start = None
            current_gap_size = 0

    # Check final gap
    if current_gap_size > max_gap_size:
        best_split = current_gap_start + current_gap_size // 2

    return best_split


def _extract_question_markers(pdf_path: str, page_index: int):
    try:
        doc = fitz.open(pdf_path)
        page = doc[page_index]
        blocks = page.get_text("dict").get("blocks", [])
        markers = []
        pattern = re.compile(r"\bQ(UESTION)?\s*([0-9]{1,2})\b", re.IGNORECASE)
        for b in blocks:
            for line in b.get("lines", []):
                for span in line.get("spans", []):
                    text = span.get("text", "").strip()
                    if not text:
                        continue
                    m = pattern.search(text)
                    if m:
                        bbox = span.get("bbox", [0, 0, 0, 0])
                        font_size = float(span.get("size", 0))
                        flags = int(span.get("flags", 0))
                        is_bold = (flags & 16) != 0
                        markers.append({
                            "text": text,
                            "qnum": int(m.group(2)),
                            "y": bbox[1],  # top y in PDF coords
                            "size": font_size,
                            "bold": is_bold,
                        })
        doc.close()
        return markers
    except Exception:
        return []


def _find_whitespace_near(img: Image.Image, target_y: int, search_above: int = 140, band: int = 6):
    gray = img.convert('L')
    arr = np.array(gray)
    h, w = arr.shape
    start = max(0, target_y - search_above)
    end = min(h - band, target_y + 20)

    best_y, best_score = None, 1e9
    for y in range(start, end):
        window = arr[y:y+band, :]
        # Ink density: count pixels darker than threshold
        ink = np.sum(window < 235) / (window.size)
        # Add small penalty for being far from target
        penalty = abs(y - target_y) / h
        score = ink + 0.1 * penalty
        if score < best_score:
            best_score, best_y = score, y

    # Accept only if fairly blank
    if best_y is not None:
        window = arr[best_y:best_y+band, :]
        ink = np.sum(window < 235) / (window.size)
        if ink < 0.02:
            return best_y
    return None


def _smart_split_by_markers(pdf_path: str, page_index: int, img: Image.Image):
    """Return split_y using QUESTION markers if two or more found, else None."""
    markers = _extract_question_markers(pdf_path, page_index)
    if len(markers) < 2:
        return None

    # Prefer bold and larger font, then by y ascending
    markers.sort(key=lambda m: (-int(m["bold"]), -m["size"], m["y"]))
    # Take top two distinct markers by y (ascending)
    top_two = sorted(markers[:2], key=lambda m: m["y"])  # ensure top then below

    # Map PDF y to image y
    try:
        doc = fitz.open(pdf_path)
        page = doc[page_index]
        scale = img.height / page.rect.height
        doc.close()
    except Exception:
        scale = 1.0

    second_y_img = int(top_two[1]["y"] * scale)
    # Find whitespace slightly above second question header
    split_y = _find_whitespace_near(img, second_y_img, search_above=160, band=8)
    if split_y is None:
        split_y = max(0, second_y_img - 24)
    return split_y


def _compute_split_regions(pdf_path: str, page_index: int, img: Image.Image):
    """Return (top_end_y, bottom_start_y, reason) or None if cannot smartly determine.
    top_end_y = exclusive end Y for first question crop
    bottom_start_y = inclusive start Y for second question crop
    Coordinates in image space.
    """
    markers = _extract_question_markers(pdf_path, page_index)
    # Filter only distinct question numbers
    distinct = {}
    for m in markers:
        qn = m['qnum']
        # choose bold/larger version if duplicates
        if qn not in distinct or (m['bold'], m['size']) > (distinct[qn]['bold'], distinct[qn]['size']):
            distinct[qn] = m
    if len(distinct) < 2:
        return None
    # Sort by vertical position (y)
    ordered = sorted(distinct.values(), key=lambda m: m['y'])
    first = ordered[0]
    second = ordered[1]
    # Reject if both same question number (shouldn't happen after distinct) or too close
    if first['qnum'] == second['qnum']:
        return None
    try:
        doc = fitz.open(pdf_path)
        page = doc[page_index]
        scale = img.height / page.rect.height
        doc.close()
    except Exception:
        scale = 1.0
    first_top = int(first['y'] * scale)
    second_top = int(second['y'] * scale)
    # Minimal vertical separation threshold (8% of page height)
    if (second_top - first_top) < int(img.height * 0.08):
        return None
    # Search whitespace band BETWEEN first and second markers to find cleaner split line
    search_band_start = first_top + int(img.height * 0.04)  # skip header block bottom
    search_band_end = second_top - int(img.height * 0.02)
    if search_band_end <= search_band_start:
        return None
    gray = img.convert('L')
    arr = np.array(gray)
    best_y = None
    best_ink = 1e9
    for y in range(search_band_start, search_band_end):
        row = arr[y, :]
        ink_density = np.sum(row < 235) / len(row)
        if ink_density < best_ink:
            best_ink = ink_density
            best_y = y
    # Require relatively blank row (<3% ink)
    if best_y is None or best_ink > 0.03:
        # Fallback: split slightly above second header
        split_line = max(first_top + (second_top - first_top)//2, second_top - int(img.height * 0.015))
        return (split_line, split_line, 'fallback_mid')
    padding = int(img.height * 0.005)
    split_line = best_y
    return (split_line + padding, split_line - padding, 'whitespace_between')


def _precise_marker_crop(pdf_path: str, page_index: int, img: Image.Image):
    """Return list of (y_top, y_bottom) regions using distinct QUESTION markers.
    Only works when >=2 distinct markers; returns None on failure."""
    markers = _extract_question_markers(pdf_path, page_index)
    distinct = {}
    for m in markers:
        qn = m['qnum']
        if qn not in distinct or (m['bold'], m['size']) > (distinct[qn]['bold'], distinct[qn]['size']):
            distinct[qn] = m
    if len(distinct) < 2:
        return None
    ordered = sorted(distinct.values(), key=lambda m: m['y'])
    try:
        doc = fitz.open(pdf_path)
        page = doc[page_index]
        scale = img.height / page.rect.height
        doc.close()
    except Exception:
        scale = 1.0
    # Build regions: for two-question page assume first two markers define boundaries
    first = ordered[0]; second = ordered[1]
    first_y = int(first['y'] * scale)
    second_y = int(second['y'] * scale)
    # Validate separation
    if second_y - first_y < int(img.height * 0.06):  # too close
        return None
    margin_top = int(img.height * 0.01)
    margin_between = int(img.height * 0.008)
    margin_bottom = int(img.height * 0.01)
    # Region 1: from first marker - small margin to second marker - margin_between
    top_start = max(0, first_y - margin_top)
    top_end = max(top_start + 10, second_y - margin_between)
    # Region 2: from second marker - margin_between to end - margin_bottom
    bottom_start = max(0, second_y - margin_between)
    bottom_end = img.height - 1 - margin_bottom
    if bottom_end - bottom_start < 40:  # too small second region
        return None
    return [(top_start, top_end), (bottom_start, bottom_end)]


def _segment_two_questions(pdf_path: str, page_index: int):
    """Return list of (qnum, top_y_pdf, bottom_y_pdf) for first two distinct questions using text spans.
    We capture spans between question headers rather than guessing whitespace.
    Returns None if segmentation fails."""
    try:
        doc = fitz.open(pdf_path)
        page = doc[page_index]
        blocks = page.get_text("dict").get("blocks", [])
        pattern = re.compile(r"\bQ(UESTION)?\s*([0-9]{1,2})\b", re.IGNORECASE)
        headers = []  # list of (qnum, y_top)
        span_positions = []  # list of (y_top, y_bottom)
        for b in blocks:
            for line in b.get("lines", []):
                for span in line.get("spans", []):
                    text = span.get("text", "").strip()
                    if not text:
                        continue
                    bbox = span.get("bbox", [0,0,0,0])
                    y_top, y_bottom = bbox[1], bbox[3]
                    span_positions.append((y_top, y_bottom))
                    m = pattern.search(text)
                    if m:
                        qnum = int(m.group(2))
                        headers.append((qnum, y_top))
        if len(headers) < 2:
            doc.close()
            return None
        # Distinct by question number preserving order
        distinct = []
        seen = set()
        for q,y in headers:
            if q not in seen:
                distinct.append((q,y))
                seen.add(q)
            if len(distinct) == 2:
                break
        if len(distinct) < 2:
            doc.close()
            return None
        # Determine bottom of first question: start at first header y, end just before second header y
        first_q, first_y = distinct[0]
        second_q, second_y = distinct[1]
        # Validate vertical separation
        if second_y - first_y < page.rect.height * 0.06:  # too close
            doc.close()
            return None
        # Bottom of first block: largest span bottom whose top < second_y - small margin
        first_block_bottom = first_y
        cutoff = second_y - page.rect.height * 0.01
        for top,bottom in span_positions:
            if top >= first_y and top < cutoff:
                if bottom > first_block_bottom:
                    first_block_bottom = bottom
        # Bottom of second block: last span bottom after second header
        second_block_bottom = second_y
        for top,bottom in span_positions:
            if top >= second_y and bottom > second_block_bottom:
                second_block_bottom = bottom
        # Add small margins
        margin = page.rect.height * 0.005
        regions = [
            (first_q, max(0.0, first_y - margin), min(page.rect.height, first_block_bottom + margin)),
            (second_q, max(0.0, second_y - margin), min(page.rect.height, second_block_bottom + margin))
        ]
        doc.close()
        return regions
    except Exception:
        return None


def _force_two_question_crop(pdf_path: str, page_num: int, output_dir: Path, base_name: str):
    """Force re-cropping of a page containing two questions using improved logic."""
    full_image_path = output_dir / f"{base_name}_page_{page_num}.png"
    part1_path = output_dir / f"{base_name}_page_{page_num}_part1.png"
    part2_path = output_dir / f"{base_name}_page_{page_num}_part2.png"
    if not full_image_path.exists():
        return
    img = Image.open(full_image_path)
    # Try segmentation
    seg_regions = _segment_two_questions(str(output_dir.parent / (base_name + '.pdf')), page_num - 1)
    reason = "segmentation"
    if seg_regions:
        # Map pdf coords to image coords
        try:
            doc = fitz.open(str(output_dir.parent / (base_name + '.pdf')))
            page = doc[page_num - 1]
            scale = img.height / page.rect.height
            doc.close()
        except Exception:
            scale = 1.0
        first_block = seg_regions[0]
        second_block = seg_regions[1]
        first_top_img = int(first_block[1] * scale)
        first_bottom_img = int(first_block[2] * scale)
        second_top_img = int(second_block[1] * scale)
        # Crop strictly to block extents
        top_img = img.crop((0, max(0, first_top_img - 6), img.width, min(img.height, first_bottom_img + 6)))
        bottom_img = img.crop((0, max(0, second_top_img - 6), img.width, img.height))
    else:
        # Fallbacks
        split_y = _smart_split_by_markers(str(output_dir.parent / (base_name + '.pdf')), page_num - 1, img)
        if split_y is None:
            split_y = find_question_split_point(img)
            reason = 'global_whitespace_fallback'
        else:
            reason = 'marker_header'
        padding = max(6, int(img.height * 0.012))
        top_img = img.crop((0, 0, img.width, min(img.height, split_y + padding)))
        bottom_img = img.crop((0, max(0, split_y - padding), img.width, img.height))
    top_img.save(part1_path, 'PNG', quality=95)
    bottom_img.save(part2_path, 'PNG', quality=95)
    full_image_path.unlink()
    pct = (top_img.height / img.height) * 100
    print(f"✂️  Final refined crop page {page_num}: first part height {pct:.1f}% via {reason}")


def process_pdf_with_split(pdf_path, output_dir, pdf_info, zoom=2.0):
    """Run the split_pdf.py script on a PDF and intelligently split pages based on markers or whitespace."""
    # Ensure Path type for output_dir
    output_dir = Path(output_dir)

    script_path = Path(__file__).parent / "split_pdf.py"
    venv_python = Path(__file__).parent.parent / "venv" / "bin" / "python"

    cmd = [str(venv_python), str(script_path), pdf_path, output_dir, f"--zoom={zoom}"]

    result = subprocess.run(cmd, capture_output=True, text=True)

    if result.returncode != 0:
        print(f"Error processing {pdf_path}:")
        print(result.stderr)
        return False

    print(result.stdout)

    # Now refine per mapping: recombine single-question pages and split multi-question pages
    base_name = Path(pdf_path).stem

    # Open PDF once for scale mapping
    try:
        _doc_for_scale = fitz.open(pdf_path)
    except Exception:
        _doc_for_scale = None

    for page_num, questions in pdf_info["page_questions"].items():
        full_image_path = Path(output_dir) / f"{base_name}_page_{page_num}.png"
        part1_path = Path(output_dir) / f"{base_name}_page_{page_num}_part1.png"
        part2_path = Path(output_dir) / f"{base_name}_page_{page_num}_part2.png"

        if not questions:
            continue

        if len(questions) == 1:
            # Recombine if auto-split incorrectly
            if not full_image_path.exists() and part1_path.exists() and part2_path.exists():
                print(f"🔗 Recombining page {page_num} (single question {questions[0]})")
                img1 = Image.open(part1_path)
                img2 = Image.open(part2_path)
                width = max(img1.width, img2.width)
                combined = Image.new('RGB', (width, img1.height + img2.height), 'white')
                combined.paste(img1, (0, 0))
                combined.paste(img2, (0, img1.height))
                combined.save(full_image_path, "PNG", quality=95)
                part1_path.unlink(); part2_path.unlink()

        elif len(questions) == 2:
            # Ensure split—prefer QUESTION markers
            if full_image_path.exists() and not part1_path.exists():
                img = Image.open(full_image_path)
                split_y = _smart_split_by_markers(pdf_path, page_num - 1, img)
                if split_y is None:
                    # Fallback to existing whitespace split point
                    split_y = find_question_split_point(img)
                    reason = "whitespace"
                else:
                    reason = "QUESTION marker"

                padding = max(6, int(img.height * 0.015))
                top = img.crop((0, 0, img.width, min(img.height, split_y + padding)))
                bottom = img.crop((0, max(0, split_y - padding), img.width, img.height))
                top.save(part1_path, "PNG", quality=95)
                bottom.save(part2_path, "PNG", quality=95)
                full_image_path.unlink()

                pct = (split_y / img.height) * 100
                print(f"✂️  Refined split page {page_num} at {pct:.1f}% via {reason} ({questions[0]} / {questions[1]})")

            # Refinement additions start

            # Force re-crop using improved logic if two questions are mapped
            _force_two_question_crop(pdf_path, page_num, output_dir, base_name)

    if _doc_for_scale:
        _doc_for_scale.close()

    return True


def create_manifest_from_mapping(pdf_info, output_images_dir):
    """Create manifest JSON based on the page-question mapping."""
    questions = {}
    page_questions = pdf_info["page_questions"]
    base_name = Path(pdf_info["pdf_name"]).stem

    # Group pages by question number
    question_pages = {}
    for page_num, qs in page_questions.items():
        if qs is None:
            continue
        for q in qs:
            if q not in question_pages:
                question_pages[q] = []
            question_pages[q].append(page_num)

    # Build manifest
    for question_num in sorted(question_pages.keys(), key=lambda x: int(x[1:])):
        pages = question_pages[question_num]
        images = []

        for page in pages:
            # Determine which questions are on this page
            page_qs = page_questions[page]

            # Check which images exist for this page
            part1 = f"{base_name}_page_{page}_part1.png"
            part2 = f"{base_name}_page_{page}_part2.png"
            full = f"{base_name}_page_{page}.png"

            part1_path = Path(output_images_dir) / part1
            part2_path = Path(output_images_dir) / part2
            full_path = Path(output_images_dir) / full

            if len(page_qs) == 1:
                # Single question on this page - use full page or both parts
                if full_path.exists():
                    images.append(full)
                elif part1_path.exists() and part2_path.exists():
                    # Both parts exist for single question, use both
                    images.append(part1)
                    images.append(part2)
                elif part1_path.exists():
                    images.append(part1)
                elif part2_path.exists():
                    images.append(part2)
            elif len(page_qs) == 2:
                # Two questions on this page
                q_index = page_qs.index(question_num)
                if q_index == 0:
                    # First question - use part1 if available, otherwise full
                    if part1_path.exists():
                        images.append(part1)
                    elif full_path.exists():
                        images.append(full)
                elif q_index == 1:
                    # Second question - use part2 if available, otherwise full
                    if part2_path.exists():
                        images.append(part2)
                    elif full_path.exists():
                        images.append(full)

        if images:  # Only add question if it has images
            questions[question_num] = {
                "images": images
            }

    return {
        "grade": pdf_info["grade"],
        "subject": pdf_info["subject"],
        "year": pdf_info["year"],
        "month": pdf_info["month"],
        "paper": pdf_info["paper"],
        "type": pdf_info["type"],
        "questions": questions
    }


def main():
    project_root = Path(__file__).parent.parent
    database_dir = project_root / "DATABASE"
    assets_dir = project_root / "app" / "src" / "main" / "assets"

    assets_dir.mkdir(parents=True, exist_ok=True)

    for pdf_name, pdf_info in PDF_INFO.items():
        pdf_path = database_dir / pdf_name

        if not pdf_path.exists():
            print(f"⚠️  PDF not found: {pdf_path}")
            continue

        pdf_info["pdf_name"] = pdf_name

        # Create output directory for this PDF's images
        output_dir = assets_dir / Path(pdf_name).stem
        output_dir.mkdir(parents=True, exist_ok=True)

        print(f"\n{'='*80}")
        print(f"📄 Processing: {pdf_name}")
        print(f"{'='*80}")

        # Process PDF with splitting
        if not process_pdf_with_split(str(pdf_path), str(output_dir), pdf_info):
            print(f"❌ Failed to process {pdf_name}")
            continue

        # Create manifest
        manifest = create_manifest_from_mapping(pdf_info, output_dir)

        # Save manifest
        manifest_name = f"Grade_{pdf_info['grade']}_{pdf_info['subject']}_{pdf_info['year']}_{pdf_info['month']}_{pdf_info['paper']}.json"
        if pdf_info["type"] == "memo":
            manifest_name = manifest_name.replace(".json", "_MEMO.json")

        manifest_path = assets_dir / manifest_name

        with open(manifest_path, 'w') as f:
            json.dump(manifest, f, indent=2)

        print(f"✅ Created manifest: {manifest_name}")
        print(f"📊 Questions: {len(manifest['questions'])}")

    print(f"\n{'='*80}")
    print("✅ All PDFs processed successfully!")
    print(f"{'='*80}")


if __name__ == "__main__":
    main()
