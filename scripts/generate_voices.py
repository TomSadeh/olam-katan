#!/usr/bin/env python3
"""Generate all voice lines for Olam Katan using ElevenLabs Alice.

English: eleven_multilingual_v2
Hebrew: eleven_v3 with language_code="he", single words wrapped in context sentences.
"""

import os
import subprocess
import json
import time
import urllib.request

RAW_DIR = os.path.join(os.path.dirname(__file__), "..", "app", "src", "main", "res", "raw")
TEMP_DIR = "/tmp/olam_katan_voices"

ELEVENLABS_KEY = os.environ["ELEVENLABS_API_KEY"]
ELEVENLABS_VOICE_ID = "Xb7hH8MSUJpSbSDYk0k2"  # Alice

os.makedirs(TEMP_DIR, exist_ok=True)
os.makedirs(RAW_DIR, exist_ok=True)


def elevenlabs_tts(text: str, filename: str, lang: str = "en"):
    """Generate voice line with ElevenLabs Alice."""
    outpath = os.path.join(TEMP_DIR, f"{filename}.mp3")
    if os.path.exists(outpath) and os.path.getsize(outpath) > 500:
        print(f"  [skip] {filename} (cached)")
        return outpath

    if lang == "he":
        # Add trailing ellipsis to prevent ElevenLabs from clipping the end
        padded_text = text.rstrip(".!?") + "..."
        body_dict = {
            "text": padded_text,
            "model_id": "eleven_v3",
            "language_code": "he",
            "voice_settings": {"stability": 0.5, "similarity_boost": 0.75},
        }
    else:
        body_dict = {
            "text": text,
            "model_id": "eleven_multilingual_v2",
            "voice_settings": {"stability": 0.5, "similarity_boost": 0.75},
        }

    body = json.dumps(body_dict).encode("utf-8")
    req = urllib.request.Request(
        f"https://api.elevenlabs.io/v1/text-to-speech/{ELEVENLABS_VOICE_ID}",
        data=body,
        headers={
            "Content-Type": "application/json",
            "xi-api-key": ELEVENLABS_KEY,
            "Accept": "audio/mpeg",
        },
    )
    try:
        with urllib.request.urlopen(req) as resp:
            data = resp.read()
    except urllib.error.HTTPError as e:
        print(f"  [ERROR] {filename}: HTTP {e.code}")
        return None
    if data[:4] == b'{"de':
        print(f"  [ERROR] {filename}: {data.decode()[:100]}")
        return None
    with open(outpath, "wb") as f:
        f.write(data)
    print(f"  [ok] {filename} ({len(data)} bytes)")
    time.sleep(0.3)
    return outpath


def to_ogg(mp3_path: str, ogg_name: str):
    """Convert MP3 to OGG and place in res/raw."""
    ogg_path = os.path.join(RAW_DIR, f"{ogg_name}.ogg")
    subprocess.run(
        ["ffmpeg", "-y", "-i", mp3_path, "-c:a", "libvorbis", "-q:a", "4", ogg_path],
        capture_output=True,
    )


# ============================================================
# VOICE LINE DEFINITIONS
# Format: (resource_name, english_text, hebrew_text)
#
# For Hebrew single words, we wrap in "זה/זאת X" context so the
# TTS pronounces them correctly. The context prefix is natural
# and doesn't hurt — kids hear "this is a cow" which is fine.
#
# For prompts/sentences, Hebrew text is already a full sentence.
# We mark those with is_sentence=True so they don't get wrapped.
# ============================================================

# (resource_name, english_text, hebrew_context_text, is_sentence)
voice_lines = [
    # --- Animal names (wrapped: "זאת פרה") ---
    ("voice_animal_cow", "Cow", "זאת פרה.", False),
    ("voice_animal_dog", "Dog", "זה כלב.", False),
    ("voice_animal_cat", "Cat", "זה חתול.", False),
    ("voice_animal_chicken", "Chicken", "זאת תרנגולת.", False),
    ("voice_animal_horse", "Horse", "זה סוס.", False),
    ("voice_animal_pig", "Pig", "זה חזיר.", False),
    ("voice_animal_sheep", "Sheep", "זאת כבשה.", False),
    ("voice_animal_duck", "Duck", "זה ברווז.", False),
    ("voice_animal_rabbit", "Rabbit", "זה ארנב.", False),
    ("voice_animal_goat", "Goat", "זאת עז.", False),
    ("voice_animal_lion", "Lion", "זה אריה.", False),
    ("voice_animal_elephant", "Elephant", "זה פיל.", False),
    ("voice_animal_monkey", "Monkey", "זה קוף.", False),
    ("voice_animal_giraffe", "Giraffe", "זאת ג׳ירפה.", False),
    ("voice_animal_penguin", "Penguin", "זה פינגווין.", False),

    # --- Animal sounds (already expressive, send as-is) ---
    ("voice_sound_cow", "Moo!", "מוּ!", True),
    ("voice_sound_dog", "Woof woof!", "הב הב!", True),
    ("voice_sound_cat", "Meow!", "מיאו!", True),
    ("voice_sound_chicken", "Cock-a-doodle-doo!", "קוקוריקו!", True),
    ("voice_sound_horse", "Neigh!", "איהההה!", True),
    ("voice_sound_pig", "Oink oink!", "חרר חרר!", True),
    ("voice_sound_sheep", "Baa!", "מעע!", True),
    ("voice_sound_duck", "Quack quack!", "גע גע!", True),
    ("voice_sound_goat", "Maa!", "מעע!", True),
    ("voice_sound_lion", "Roar!", "שאגה!", True),
    ("voice_sound_elephant", "Trumpet!", "טרררר!", True),
    ("voice_sound_monkey", "Ooh ooh ah ah!", "או או אה אה!", True),

    # --- Food names ---
    ("voice_food_grass", "Grass", "זה עשב.", False),
    ("voice_food_bone", "Bone", "זה עצם.", False),
    ("voice_food_fish", "Fish", "זה דג.", False),
    ("voice_food_seeds", "Seeds", "אלה גרעינים.", False),
    ("voice_food_hay", "Hay", "זה חציר.", False),
    ("voice_food_corn", "Corn", "זה תירס.", False),
    ("voice_food_clover", "Clover", "זה תלתן.", False),
    ("voice_food_bread", "Bread", "זה לחם.", False),
    ("voice_food_carrot", "Carrot", "זה גזר.", False),
    ("voice_food_leaves", "Leaves", "אלה עלים.", False),
    ("voice_food_meat", "Meat", "זה בשר.", False),
    ("voice_food_peanuts", "Peanuts", "אלה בוטנים.", False),
    ("voice_food_banana", "Banana", "זאת בננה.", False),

    # --- Color names ---
    ("voice_color_red", "Red", "זה אדום.", False),
    ("voice_color_blue", "Blue", "זה כחול.", False),
    ("voice_color_yellow", "Yellow", "זה צהוב.", False),
    ("voice_color_green", "Green", "זה ירוק.", False),
    ("voice_color_orange", "Orange", "זה כתום.", False),
    ("voice_color_purple", "Purple", "זה סגול.", False),
    ("voice_color_pink", "Pink", "זה ורוד.", False),
    ("voice_color_brown", "Brown", "זה חום.", False),
    ("voice_color_white", "White", "זה לבן.", False),
    ("voice_color_black", "Black", "זה שחור.", False),

    # --- Shape names ---
    ("voice_shape_circle", "Circle", "זה עיגול.", False),
    ("voice_shape_square", "Square", "זה ריבוע.", False),
    ("voice_shape_triangle", "Triangle", "זה משולש.", False),
    ("voice_shape_star", "Star", "זה כוכב.", False),
    ("voice_shape_heart", "Heart", "זה לב.", False),
    ("voice_shape_diamond", "Diamond", "זה מעוין.", False),
    ("voice_shape_hexagon", "Hexagon", "זה משושה.", False),
    ("voice_shape_pentagon", "Pentagon", "זה מחומש.", False),
    ("voice_shape_octagon", "Octagon", "זה מתומן.", False),

    # --- Numbers 1-10 ---
    ("voice_number_1", "One", "אחת.", True),
    ("voice_number_2", "Two", "שתיים.", True),
    ("voice_number_3", "Three", "שלוש.", True),
    ("voice_number_4", "Four", "ארבע.", True),
    ("voice_number_5", "Five", "חמש.", True),
    ("voice_number_6", "Six", "שש.", True),
    ("voice_number_7", "Seven", "שבע.", True),
    ("voice_number_8", "Eight", "שמונה.", True),
    ("voice_number_9", "Nine", "תשע.", True),
    ("voice_number_10", "Ten", "עשר.", True),

    # --- Instrument names ---
    ("voice_instrument_drum", "Drum", "זה תוף.", False),
    ("voice_instrument_xylophone", "Xylophone", "זה קסילופון.", False),
    ("voice_instrument_tambourine", "Tambourine", "זה תוף מרים.", False),
    ("voice_instrument_maracas", "Maracas", "אלה מרקסים.", False),

    # --- Hebrew letters ---
    ("voice_letter_alef", "Alef", "זאת האות אָלֶף.", False),
    ("voice_letter_bet", "Bet", "זאת האות בֵּית.", False),
    ("voice_letter_gimel", "Gimel", "זאת האות גִּימֶל.", False),
    ("voice_letter_dalet", "Dalet", "זאת האות דָּלֶת.", False),
    ("voice_letter_he", "He", "זאת האות הֵא.", False),
    ("voice_letter_vav", "Vav", "זאת האות וָו.", False),

    # --- English letters ---
    ("voice_letter_a", "A, like Apple", "A", True),
    ("voice_letter_b", "B, like Ball", "B", True),
    ("voice_letter_c", "C, like Cat", "C", True),
    ("voice_letter_d", "D, like Dog", "D", True),
    ("voice_letter_e", "E, like Elephant", "E", True),
    ("voice_letter_f", "F, like Fish", "F", True),

    # --- Game prompts (already full sentences) ---
    ("voice_prompt_whos_hungry", "Who's hungry?", "מי רוצה אוכל?", True),
    ("voice_prompt_tap_the", "Tap the right one!", "תלחץ על הנכון!", True),
    ("voice_prompt_drag_it", "Drag it to its place!", "גרור את זה למקום שלו!", True),
    ("voice_prompt_try_again", "Nope, try again!", "לא, ננסה שוב!", True),
    ("voice_prompt_great_job", "Great job!", "כל הכבוד!", True),
    ("voice_prompt_well_done", "Well done!", "יפה מאוד!", True),
    ("voice_prompt_new_island", "New island unlocked!", "אי חדש נפתח!", True),
    ("voice_prompt_count_along", "Let's count together!", "בואו נספור ביחד!", True),

    # --- My Day prompts ---
    ("voice_prompt_bath", "Let's take a bath!", "בוא נתרחץ!", True),
    ("voice_prompt_brush_teeth", "Let's brush our teeth!", "בוא נצחצח שיניים!", True),
    ("voice_prompt_tidy_up", "Let's tidy up the room!", "בוא נסדר את החדר!", True),
    ("voice_prompt_get_dressed", "Let's get dressed! What goes on first?", "בוא נתלבש! מה שמים קודם?", True),
    ("voice_prompt_set_table", "Let's set the table!", "בוא נערוך את השולחן!", True),
    ("voice_prompt_good_night", "Good night!", "לילה טוב!", True),
    ("voice_prompt_time_break", "Time for a break!", "הגיע הזמן להפסקה!", True),

    # --- Body parts ---
    ("voice_body_arms", "Arms", "אלה ידיים.", False),
    ("voice_body_legs", "Legs", "אלה רגליים.", False),
    ("voice_body_tummy", "Tummy", "זאת בטן.", False),
    ("voice_body_face", "Face", "אלה פנים.", False),

    # --- Clothing ---
    ("voice_clothing_underwear", "Underwear", "אלה תחתונים.", False),
    ("voice_clothing_shirt", "Shirt", "זאת חולצה.", False),
    ("voice_clothing_pants", "Pants", "אלה מכנסיים.", False),
    ("voice_clothing_socks", "Socks", "אלה גרביים.", False),
    ("voice_clothing_shoes", "Shoes", "אלה נעליים.", False),

    # --- Table items ---
    ("voice_table_plate", "Plate", "זאת צלחת.", False),
    ("voice_table_cup", "Cup", "זאת כוס.", False),
    ("voice_table_fork", "Fork", "זה מזלג.", False),
    ("voice_table_spoon", "Spoon", "זאת כפית.", False),

    # --- Tidy items ---
    ("voice_tidy_teddy", "Teddy", "זה דובי.", False),
    ("voice_tidy_book", "Book", "זה ספר.", False),
    ("voice_tidy_ball", "Ball", "זה כדור.", False),

    # --- Sticker names ---
    ("voice_sticker_rainbow", "Rainbow", "זאת קשת.", False),
    ("voice_sticker_flower", "Flower", "זה פרח.", False),
    ("voice_sticker_butterfly", "Butterfly", "זה פרפר.", False),
    ("voice_sticker_sun", "Sun", "זאת שמש.", False),
    ("voice_sticker_moon", "Moon", "זה ירח.", False),
    ("voice_sticker_rocket", "Rocket", "זה טיל.", False),
    ("voice_sticker_crown", "Crown", "זה כתר.", False),
    ("voice_sticker_trophy", "Trophy", "זה גביע.", False),
    ("voice_sticker_diamond", "Diamond", "זה יהלום.", False),
    ("voice_sticker_unicorn", "Unicorn", "זה חד קרן.", False),
]


def main():
    total = len(voice_lines)
    print(f"Generating {total} voice lines x 2 languages = {total * 2} audio files")
    print(f"English: eleven_multilingual_v2 | Hebrew: eleven_v3 with language_code=he\n")

    errors = []
    for i, (name, en_text, he_text, is_sentence) in enumerate(voice_lines, 1):
        print(f"[{i}/{total}] {name}")

        # English
        en_mp3 = elevenlabs_tts(en_text, f"{name}_en", lang="en")
        if en_mp3:
            to_ogg(en_mp3, f"{name}_en")
        else:
            errors.append(f"{name}_en")

        # Hebrew
        he_mp3 = elevenlabs_tts(he_text, f"{name}_he", lang="he")
        if he_mp3:
            to_ogg(he_mp3, f"{name}_he")
        else:
            errors.append(f"{name}_he")

    ogg_files = [f for f in os.listdir(RAW_DIR) if f.startswith("voice_") and f.endswith(".ogg")]
    print(f"\nDone! Generated {len(ogg_files)} OGG files in res/raw/")
    if errors:
        print(f"Errors ({len(errors)}): {', '.join(errors)}")


if __name__ == "__main__":
    main()
