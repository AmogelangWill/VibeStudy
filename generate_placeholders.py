pfrom PIL import Image, ImageDraw, ImageFont
import os

# Create drawable directory if not exists
drawable_dir = '/home/kali/Desktop/VibeStudy_Project/app/src/main/res/drawable'
os.makedirs(drawable_dir, exist_ok=True)

# Generate 3 question and memo placeholder images
for i in range(1, 4):
    # Question image
    q_img = Image.new('RGB', (800, 600), color=(245, 245, 250))
    d = ImageDraw.Draw(q_img)

    # Draw border
    d.rectangle([(10, 10), (790, 590)], outline=(100, 100, 150), width=3)

    # Question text
    d.text((50, 50), f'QUESTION {i}', fill=(50, 50, 100))
    d.text((50, 100), f'This is a sample question {i} from the past paper.', fill=(70, 70, 120))
    d.text((50, 140), f'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', fill=(70, 70, 120))
    d.text((50, 180), f'Solve for x: 2x + 5 = 15', fill=(50, 50, 100))
    d.text((50, 240), f'a) Show your working', fill=(70, 70, 120))
    d.text((50, 280), f'b) Verify your answer', fill=(70, 70, 120))

    q_img.save(f'{drawable_dir}/demo_q{i}.png')

    # Memo image
    m_img = Image.new('RGB', (800, 600), color=(250, 250, 245))
    md = ImageDraw.Draw(m_img)

    # Draw border
    md.rectangle([(10, 10), (790, 590)], outline=(150, 100, 100), width=3)

    # Memo text
    md.text((50, 50), f'MEMORANDUM - QUESTION {i}', fill=(100, 50, 50))
    md.text((50, 100), f'Solution:', fill=(120, 70, 70))
    md.text((50, 140), f'2x + 5 = 15', fill=(70, 70, 120))
    md.text((50, 180), f'2x = 15 - 5', fill=(70, 70, 120))
    md.text((50, 220), f'2x = 10', fill=(70, 70, 120))
    md.text((50, 260), f'x = 5', fill=(70, 70, 120))
    md.text((50, 320), f'Verification: 2(5) + 5 = 10 + 5 = 15 ✓', fill=(50, 100, 50))
    md.text((50, 380), f'Marks: [5]', fill=(100, 50, 50))

    m_img.save(f'{drawable_dir}/demo_memo{i}.png')

print('Created 3 question and 3 memo placeholder images')

