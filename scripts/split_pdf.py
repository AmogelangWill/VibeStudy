#!/usr/bin/env python3
"""
Smart PDF splitter that detects "QUESTION N" text to identify question boundaries.
Uses OCR text detection to find question markers.
"""
import argparse
import os
import fitz  # PyMuPDF
from PIL import Image
import numpy as np


def render_page_to_image(page, zoom=2.0):
    """Render a PDF page to a PIL Image."""
    mat = fitz.Matrix(zoom, zoom)
    pix = page.get_pixmap(matrix=mat, alpha=False)
    img = Image.frombytes("RGB", [pix.width, pix.height], pix.samples)
    return img


def find_question_markers(page):
    """
    Find "QUESTION" text positions in the PDF page.
    Returns list of (text, y_position, font_size) tuples.
    """
    question_markers = []

    # Extract text with position and font information
    blocks = page.get_text("dict")["blocks"]

    for block in blocks:
        if "lines" not in block:
            continue

        for line in block["lines"]:
            for span in line["spans"]:
                text = span["text"].strip().upper()

                # Check if this is a question marker
                if "QUESTION" in text:
                    # Get position (use the top of the text bbox)
                    y_pos = span["bbox"][1]  # Top y-coordinate
                    font_size = span["size"]
                    font_flags = span.get("flags", 0)

                    # Check if bold (flag 16 = bold in PyMuPDF)
                    is_bold = (font_flags & 16) != 0

                    question_markers.append({
                        'text': text,
                        'y_pos': y_pos,
                        'font_size': font_size,
                        'is_bold': is_bold,
                        'bbox': span["bbox"]
                    })

    return question_markers


def find_smart_split(page, img, markers):
    """
    Find the best split point between two questions based on QUESTION markers.
    Returns the y-coordinate to split at (in image coordinates), or None.
    """
    if len(markers) < 2:
        return None

    # Filter for likely question headers (bold, larger font)
    # Sort by font size to find the largest (main question headers)
    sorted_markers = sorted(markers, key=lambda m: m['font_size'], reverse=True)

    # Take the top markers (assumed to be question headers)
    if len(sorted_markers) >= 2:
        # Get the two largest markers
        first_q = sorted_markers[0]
        second_q = sorted_markers[1]

        # Make sure they're in order (first one should be higher on page)
        if first_q['y_pos'] > second_q['y_pos']:
            first_q, second_q = second_q, first_q

        # Calculate split point: midway between the two question markers
        # Convert PDF coordinates to image coordinates
        page_height = page.rect.height
        img_height = img.height
        scale = img_height / page_height

        # Position split slightly above the second question
        second_q_img_y = int(second_q['y_pos'] * scale)

        # Look for whitespace above the second question
        split_y = find_whitespace_near(img, second_q_img_y, search_range=100)

        if split_y:
            return split_y
        else:
            # Fallback: split 20 pixels above the second question marker
            return max(0, second_q_img_y - 20)

    return None


def find_whitespace_near(img, target_y, search_range=100):
    """
    Find the best whitespace (blank horizontal line) near target_y.
    Searches within search_range pixels above target_y.
    """
    gray = img.convert("L")
    arr = np.array(gray)

    start_y = max(0, target_y - search_range)
    end_y = min(arr.shape[0], target_y + 20)

    best_y = None
    min_ink = float('inf')

    for y in range(start_y, end_y):
        # Calculate ink density in this row
        row = arr[y, :]
        ink_density = np.sum(row < 240) / len(row)

        if ink_density < min_ink:
            min_ink = ink_density
            best_y = y

    # Only return if we found a relatively blank line
    if min_ink < 0.02:  # Less than 2% ink
        return best_y

    return None


def find_horizontal_split_fallback(img, min_gap_ratio=0.10, darkness_threshold=240):
    """
    Fallback method: Find a horizontal line to split based on whitespace.
    Returns the y-coordinate to split at, or None if no significant gap found.
    """
    gray = img.convert("L")
    arr = np.array(gray)

    # Consider a pixel "ink" if value < darkness_threshold
    ink = (arr < darkness_threshold).astype(np.uint8)

    # Calculate ink density per row (normalized)
    row_ink_density = ink.sum(axis=1) / arr.shape[1]

    # Find whitespace rows (very low ink density)
    whitespace_threshold = 0.01  # Less than 1% ink coverage
    is_whitespace = (row_ink_density <= whitespace_threshold).astype(np.uint8)

    # Find contiguous whitespace segments in the middle third of the page
    height = arr.shape[0]
    start_search = height // 3
    end_search = 2 * height // 3

    gaps = []
    start = None
    for i in range(start_search, end_search):
        if is_whitespace[i] == 1 and start is None:
            start = i
        elif is_whitespace[i] == 0 and start is not None:
            gaps.append((start, i - 1))
            start = None
    if start is not None:
        gaps.append((start, end_search - 1))

    if not gaps:
        return None

    # Find the largest gap
    largest_gap = max(gaps, key=lambda g: g[1] - g[0])
    gap_height = largest_gap[1] - largest_gap[0] + 1

    # Only split if gap is significant enough
    if gap_height >= min_gap_ratio * height:
        # Split at center of gap
        split_y = (largest_gap[0] + largest_gap[1]) // 2
        return split_y

    return None


def process_pdf(pdf_path, output_dir, zoom=2.0, min_gap_ratio=0.10, darkness_threshold=240):
    """Process a PDF and split pages with multiple questions."""
    doc = fitz.open(pdf_path)
    base_name = os.path.splitext(os.path.basename(pdf_path))[0]
    os.makedirs(output_dir, exist_ok=True)

    results = []

    for page_num in range(len(doc)):
        page = doc[page_num]
        img = render_page_to_image(page, zoom=zoom)

        # Try to find QUESTION markers first (smart method)
        markers = find_question_markers(page)
        split_y = None

        if len(markers) >= 2:
            split_y = find_smart_split(page, img, markers)
            if split_y:
                print(f"🎯 Detected {len(markers)} QUESTION markers on page {page_num + 1}")

        # Fallback to whitespace detection if smart method didn't work
        if split_y is None:
            split_y = find_horizontal_split_fallback(img, min_gap_ratio=min_gap_ratio,
                                                     darkness_threshold=darkness_threshold)

        if split_y is None:
            # No split needed, save full page
            out_path = os.path.join(output_dir, f"{base_name}_page_{page_num + 1}.png")
            img.save(out_path, "PNG", quality=95)
            results.append({
                'page': page_num + 1,
                'parts': 1,
                'files': [out_path]
            })
            print(f"✓ Saved full page {page_num + 1}: {os.path.basename(out_path)}")
        else:
            # Split into two parts
            width, height = img.size

            # Top part (first question)
            top_img = img.crop((0, 0, width, split_y))
            out_top = os.path.join(output_dir, f"{base_name}_page_{page_num + 1}_part1.png")
            top_img.save(out_top, "PNG", quality=95)

            # Bottom part (second question)
            bottom_img = img.crop((0, split_y, width, height))
            out_bottom = os.path.join(output_dir, f"{base_name}_page_{page_num + 1}_part2.png")
            bottom_img.save(out_bottom, "PNG", quality=95)

            results.append({
                'page': page_num + 1,
                'parts': 2,
                'files': [out_top, out_bottom]
            })
            print(f"✓ Split page {page_num + 1} into 2 parts:")
            print(f"  - {os.path.basename(out_top)}")
            print(f"  - {os.path.basename(out_bottom)}")

    doc.close()
    return results


def main():
    parser = argparse.ArgumentParser(
        description="Smart PDF splitter that detects QUESTION text markers."
    )
    parser.add_argument("pdf", help="Input PDF file")
    parser.add_argument("output", help="Output directory for images")
    parser.add_argument("--zoom", type=float, default=2.0,
                       help="Render scale factor (default: 2.0)")
    parser.add_argument("--min-gap-ratio", type=float, default=0.10,
                       help="Minimum gap height ratio for fallback method (default: 0.10)")
    parser.add_argument("--darkness", type=int, default=240,
                       help="Pixel value threshold for ink detection 0-255 (default: 240)")

    args = parser.parse_args()

    if not os.path.exists(args.pdf):
        print(f"Error: PDF file not found: {args.pdf}")
        return 1

    print(f"\n📄 Processing: {args.pdf}")
    print(f"📁 Output directory: {args.output}")
    print(f"⚙️  Settings: zoom={args.zoom}, smart detection enabled\n")

    results = process_pdf(
        args.pdf,
        args.output,
        zoom=args.zoom,
        min_gap_ratio=args.min_gap_ratio,
        darkness_threshold=args.darkness
    )

    print(f"\n✅ Processed {len(results)} pages")
    total_images = sum(r['parts'] for r in results)
    print(f"📊 Generated {total_images} images")


if __name__ == "__main__":
    main()

