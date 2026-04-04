#!/usr/bin/env python3
"""Generate all voice lines for Olam Katan using ElevenLabs (Alice, multilingual v2)."""

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


def elevenlabs_tts(text: str, filename: str):
    """Generate voice line with ElevenLabs (Alice, multilingual v2)."""
    outpath = os.path.join(TEMP_DIR, f"{filename}.mp3")
    if os.path.exists(outpath) and os.path.getsize(outpath) > 500:
        print(f"  [skip] {filename} (cached)")
        return outpath
    body = json.dumps({
        "text": text,
        "model_id": "eleven_multilingual_v2",
        "voice_settings": {"stability": 0.5, "similarity_boost": 0.75},
    }).encode("utf-8")
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
    if data[:4] == b'{"de':  # error JSON
        print(f"  [ERROR] {filename}: {data.decode()[:100]}")
        return None
    with open(outpath, "wb") as f:
        f.write(data)
    print(f"  [ok] {filename} ({len(data)} bytes)")
    time.sleep(0.3)  # rate limit
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
# ============================================================

voice_lines = [
    # --- Animal names ---
    ("voice_animal_cow", "Cow", "פרה"),
    ("voice_animal_dog", "Dog", "כלב"),
    ("voice_animal_cat", "Cat", "חתול"),
    ("voice_animal_chicken", "Chicken", "תרנגולת"),
    ("voice_animal_horse", "Horse", "סוס"),
    ("voice_animal_pig", "Pig", "חזיר"),
    ("voice_animal_sheep", "Sheep", "כבשה"),
    ("voice_animal_duck", "Duck", "ברווז"),
    ("voice_animal_rabbit", "Rabbit", "ארנב"),
    ("voice_animal_goat", "Goat", "עז"),
    ("voice_animal_lion", "Lion", "אריה"),
    ("voice_animal_elephant", "Elephant", "פיל"),
    ("voice_animal_monkey", "Monkey", "קוף"),
    ("voice_animal_giraffe", "Giraffe", "ג׳ירפה"),
    ("voice_animal_penguin", "Penguin", "פינגווין"),

    # --- Animal sounds ---
    ("voice_sound_cow", "Moo!", "מוּ!"),
    ("voice_sound_dog", "Woof woof!", "הב הב!"),
    ("voice_sound_cat", "Meow!", "מיאו!"),
    ("voice_sound_chicken", "Cock-a-doodle-doo!", "קוקוריקו!"),
    ("voice_sound_horse", "Neigh!", "איהההה!"),
    ("voice_sound_pig", "Oink oink!", "חרר!"),
    ("voice_sound_sheep", "Baa!", "מעע!"),
    ("voice_sound_duck", "Quack quack!", "גע גע!"),
    ("voice_sound_goat", "Maa!", "מעע!"),
    ("voice_sound_lion", "Roar!", "שאגה!"),
    ("voice_sound_elephant", "Trumpet!", "טרררר!"),
    ("voice_sound_monkey", "Ooh ooh ah ah!", "או או!"),

    # --- Food names ---
    ("voice_food_grass", "Grass", "עשב"),
    ("voice_food_bone", "Bone", "עצם"),
    ("voice_food_fish", "Fish", "דג"),
    ("voice_food_seeds", "Seeds", "גרעינים"),
    ("voice_food_hay", "Hay", "חציר"),
    ("voice_food_corn", "Corn", "תירס"),
    ("voice_food_clover", "Clover", "תלתן"),
    ("voice_food_bread", "Bread", "לחם"),
    ("voice_food_carrot", "Carrot", "גזר"),
    ("voice_food_leaves", "Leaves", "עלים"),
    ("voice_food_meat", "Meat", "בשר"),
    ("voice_food_peanuts", "Peanuts", "בוטנים"),
    ("voice_food_banana", "Banana", "בננה"),

    # --- Color names ---
    ("voice_color_red", "Red", "אדום"),
    ("voice_color_blue", "Blue", "כחול"),
    ("voice_color_yellow", "Yellow", "צהוב"),
    ("voice_color_green", "Green", "ירוק"),
    ("voice_color_orange", "Orange", "כתום"),
    ("voice_color_purple", "Purple", "סגול"),
    ("voice_color_pink", "Pink", "ורוד"),
    ("voice_color_brown", "Brown", "חום"),
    ("voice_color_white", "White", "לבן"),
    ("voice_color_black", "Black", "שחור"),

    # --- Shape names ---
    ("voice_shape_circle", "Circle", "עיגול"),
    ("voice_shape_square", "Square", "ריבוע"),
    ("voice_shape_triangle", "Triangle", "משולש"),
    ("voice_shape_star", "Star", "כוכב"),
    ("voice_shape_heart", "Heart", "לב"),
    ("voice_shape_diamond", "Diamond", "מעוין"),
    ("voice_shape_hexagon", "Hexagon", "משושה"),
    ("voice_shape_pentagon", "Pentagon", "מחומש"),
    ("voice_shape_octagon", "Octagon", "מתומן"),

    # --- Numbers 1-10 ---
    ("voice_number_1", "One", "אחת"),
    ("voice_number_2", "Two", "שתיים"),
    ("voice_number_3", "Three", "שלוש"),
    ("voice_number_4", "Four", "ארבע"),
    ("voice_number_5", "Five", "חמש"),
    ("voice_number_6", "Six", "שש"),
    ("voice_number_7", "Seven", "שבע"),
    ("voice_number_8", "Eight", "שמונה"),
    ("voice_number_9", "Nine", "תשע"),
    ("voice_number_10", "Ten", "עשר"),

    # --- Instrument names ---
    ("voice_instrument_drum", "Drum", "תוף"),
    ("voice_instrument_xylophone", "Xylophone", "קסילופון"),
    ("voice_instrument_tambourine", "Tambourine", "תוף מרים"),
    ("voice_instrument_maracas", "Maracas", "מרקסים"),

    # --- Hebrew letters ---
    ("voice_letter_alef", "Alef", "אָלֶף"),
    ("voice_letter_bet", "Bet", "בֵּית"),
    ("voice_letter_gimel", "Gimel", "גִּימֶל"),
    ("voice_letter_dalet", "Dalet", "דָּלֶת"),
    ("voice_letter_he", "He", "הֵא"),
    ("voice_letter_vav", "Vav", "וָו"),

    # --- English letters ---
    ("voice_letter_a", "A, like Apple", "A"),
    ("voice_letter_b", "B, like Ball", "B"),
    ("voice_letter_c", "C, like Cat", "C"),
    ("voice_letter_d", "D, like Dog", "D"),
    ("voice_letter_e", "E, like Elephant", "E"),
    ("voice_letter_f", "F, like Fish", "F"),

    # --- Game prompts ---
    ("voice_prompt_whos_hungry", "Who's hungry?", "מי רוצה אוכל?"),
    ("voice_prompt_tap_the", "Tap the right one!", "תלחץ על הנכון!"),
    ("voice_prompt_drag_it", "Drag it to its place!", "גרור את זה למקום שלו!"),
    ("voice_prompt_try_again", "Nope, try again!", "לא, ננסה שוב!"),
    ("voice_prompt_great_job", "Great job!", "כל הכבוד!"),
    ("voice_prompt_well_done", "Well done!", "יפה מאוד!"),
    ("voice_prompt_new_island", "New island unlocked!", "אי חדש נפתח!"),
    ("voice_prompt_count_along", "Let's count together!", "בואו נספור ביחד!"),

    # --- My Day prompts ---
    ("voice_prompt_bath", "Let's take a bath!", "בוא נתרחץ!"),
    ("voice_prompt_brush_teeth", "Let's brush our teeth!", "בוא נצחצח שיניים!"),
    ("voice_prompt_tidy_up", "Let's tidy up the room!", "בוא נסדר את החדר!"),
    ("voice_prompt_get_dressed", "Let's get dressed! What goes on first?", "בוא נתלבש! מה שמים קודם?"),
    ("voice_prompt_set_table", "Let's set the table!", "בוא נערוך את השולחן!"),
    ("voice_prompt_good_night", "Good night!", "לילה טוב!"),
    ("voice_prompt_time_break", "Time for a break!", "הגיע הזמן להפסקה!"),

    # --- Body parts (bath) ---
    ("voice_body_arms", "Arms", "ידיים"),
    ("voice_body_legs", "Legs", "רגליים"),
    ("voice_body_tummy", "Tummy", "בטן"),
    ("voice_body_face", "Face", "פנים"),

    # --- Clothing ---
    ("voice_clothing_underwear", "Underwear", "תחתונים"),
    ("voice_clothing_shirt", "Shirt", "חולצה"),
    ("voice_clothing_pants", "Pants", "מכנסיים"),
    ("voice_clothing_socks", "Socks", "גרביים"),
    ("voice_clothing_shoes", "Shoes", "נעליים"),

    # --- Table items ---
    ("voice_table_plate", "Plate", "צלחת"),
    ("voice_table_cup", "Cup", "כוס"),
    ("voice_table_fork", "Fork", "מזלג"),
    ("voice_table_spoon", "Spoon", "כפית"),

    # --- Tidy items ---
    ("voice_tidy_teddy", "Teddy", "דובי"),
    ("voice_tidy_book", "Book", "ספר"),
    ("voice_tidy_ball", "Ball", "כדור"),

    # --- Sticker names ---
    ("voice_sticker_rainbow", "Rainbow", "קשת"),
    ("voice_sticker_flower", "Flower", "פרח"),
    ("voice_sticker_butterfly", "Butterfly", "פרפר"),
    ("voice_sticker_sun", "Sun", "שמש"),
    ("voice_sticker_moon", "Moon", "ירח"),
    ("voice_sticker_rocket", "Rocket", "טיל"),
    ("voice_sticker_crown", "Crown", "כתר"),
    ("voice_sticker_trophy", "Trophy", "גביע"),
    ("voice_sticker_diamond", "Diamond", "יהלום"),
    ("voice_sticker_unicorn", "Unicorn", "חד קרן"),
]


def main():
    total = len(voice_lines)
    print(f"Generating {total} voice lines x 2 languages = {total * 2} audio files")
    print(f"Using ElevenLabs Alice for both English and Hebrew\n")

    errors = []
    for i, (name, en_text, he_text) in enumerate(voice_lines, 1):
        print(f"[{i}/{total}] {name}")

        # English
        en_mp3 = elevenlabs_tts(en_text, f"{name}_en")
        if en_mp3:
            to_ogg(en_mp3, f"{name}_en")
        else:
            errors.append(f"{name}_en")

        # Hebrew
        he_mp3 = elevenlabs_tts(he_text, f"{name}_he")
        if he_mp3:
            to_ogg(he_mp3, f"{name}_he")
        else:
            errors.append(f"{name}_he")

    # Count results
    ogg_files = [f for f in os.listdir(RAW_DIR) if f.startswith("voice_") and f.endswith(".ogg")]
    print(f"\nDone! Generated {len(ogg_files)} OGG files in res/raw/")
    if errors:
        print(f"Errors ({len(errors)}): {', '.join(errors)}")


if __name__ == "__main__":
    main()
