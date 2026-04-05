from PIL import Image
import os

# Open the logo
logo_path = 'app/src/main/res/drawable/logo_header.png'
print(f"Opening: {logo_path}")

img = Image.open(logo_path)
print(f"Original size: {img.size}")
print(f"Original mode: {img.mode}")

# Convert to RGBA if needed
if img.mode != 'RGBA':
    img = img.convert('RGBA')
    print("Converted to RGBA")

# Get the bounding box of non-transparent pixels
bbox = img.getbbox()

if bbox:
    print(f"Bounding box: {bbox}")
    
    # Crop to remove transparent padding
    img_cropped = img.crop(bbox)
    print(f"Cropped size: {img_cropped.size}")
    
    # Save the cropped version
    img_cropped.save(logo_path, 'PNG', optimize=True)
    print(f"✅ SAVED cropped logo to {logo_path}")
    print(f"   Removed {img.size[0] - img_cropped.size[0]}px width and {img.size[1] - img_cropped.size[1]}px height of transparent padding!")
else:
    print("❌ No bounding box found (fully transparent?)")
