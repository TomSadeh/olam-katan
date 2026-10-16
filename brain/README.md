# פוקי's brain (companion server)

The parrot in the games gets its words from the family PC: Gemma 4 E4B writes a
short line about what just happened, and BlueTTS speaks it with the Noa voice.
Holding the parrot records the child; ivrit.ai's Hebrew-tuned Whisper transcribes
it on the PC and the parrot answers.
The tablet only talks to the PC over the home network. When the PC can't be
reached (it's off, you're away, or a reply is too slow), the parrot simply
doesn't appear and the games use their recorded lines.

```
tablet ──Wi-Fi──▶ doorbell.py :8790 ──starts on demand──▶ llama-server :8792 (Gemma)
                  (always on, no GPU)                    brain.py     :8791 (Whisper + prompt + BlueTTS)
```

- **doorbell.py** runs from Windows login. It uses no GPU and almost no memory.
- When a game opens, the tablet calls `/wake`. The doorbell starts Gemma on the GPU if
  at least `min_free_vram_mb` is free, otherwise on the CPU (slower, about 2-4 s per line).
  Warming up takes about 30 s.
- After `idle_minutes` without requests, both heavy processes exit and the VRAM is freed.

## Quick setup (Windows)

With [Python 3.12](https://www.python.org/downloads/) installed (or `winget install Python.Python.3.12`),
run this from the repo folder:

```
python brain\install.py
```

It shows what it will do, then:

1. downloads llama.cpp: the CUDA build if there's an NVIDIA GPU (matched to the driver),
   otherwise the CPU build;
2. asks which Gemma to use: **E4B** (lighter, faster), **12B** (smarter, sees drawings better;
   needs about 9 GB of free GPU memory) or **both** (12B when the GPU has room, E4B when a
   game or a training run is using it), then downloads it with its vision part, the ivrit.ai Hebrew Whisper, Parakeet for English,
   and BlueTTS with its voice models (about 9 GB in all, into `~/puki-brain`, or `--dir`);
3. creates the Python environment (without the NVIDIA libraries on PCs without a GPU);
4. writes `brain/config.json` with all the paths;
5. adds the home-network-only firewall rule (Windows asks for permission once), and tells
   you if your Wi-Fi is set to *Public*;
6. makes Puki start at login, starts it, and opens the [dashboard](#dashboard) with the
   pairing code and QR code.

Downloads show progress and resume if interrupted, and running it again only does what's
missing. Options: `--dry-run` (just show the plan), `--model e4b|12b|both`, `--dir D:\puki-brain`, `--cpu`,
`--only firewall,autostart` (repeat single steps), `--no-firewall`, `--no-autostart`.

The steps below are the same thing by hand (for Linux and macOS too).

## Setup

1. Download [llama.cpp](https://github.com/ggml-org/llama.cpp/releases) (Windows CUDA build
   plus the matching `cudart` zip) into `C:/Users/user/Documents/olam-brain/llama`, and
   `gemma-4-E4B-it-Q4_0.gguf` from
   [unsloth/gemma-4-E4B-it-GGUF](https://huggingface.co/unsloth/gemma-4-E4B-it-GGUF) into
   `C:/Users/user/Documents/olam-brain/models`. You can change these paths in the config.
2. A separate virtualenv for brain.py (kept apart from BlueTTS's own, so the read-aloud
   app is never affected), at the path set by `brain_python`:
   ```
   python -m venv C:/Users/user/Documents/olam-brain/.venv
   C:/Users/user/Documents/olam-brain/.venv/Scripts/pip install "C:/Users/user/Documents/Reading Aloud/BlueTTS" -r brain/requirements.txt
   ```
   Then download [ivrit-ai/whisper-large-v3-turbo-ct2](https://huggingface.co/ivrit-ai/whisper-large-v3-turbo-ct2)
   into `C:/Users/user/Documents/olam-brain/models/whisper-ivrit-turbo`, and for English
   `encoder-model.int8.onnx`, `decoder_joint-model.int8.onnx`, `nemo128.onnx`, `vocab.txt` and `config.json` from [istupakov/parakeet-tdt-0.6b-v2-onnx](https://huggingface.co/istupakov/parakeet-tdt-0.6b-v2-onnx)
   into `C:/Users/user/Documents/olam-brain/models/parakeet-tdt-0.6b-v2-onnx` (Parakeet, on the CPU). BlueTTS's model files
   are read from `bluetts_dir`. Whisper runs on the GPU when `whisper_min_free_vram_mb`
   is free, otherwise on the CPU. Gemma and Whisper together use about 5.6 GB of VRAM.
3. Create `brain/config.json`. It's git-ignored, so the child's name stays out of the repo,
   and it overrides anything in `config.example.json`:
   ```json
   { "child_name": "<name with nikud>", "child_gender": "girl" }
   ```
4. Let the tablet through the Windows Firewall, home network only. Run this once in an
   **administrator** PowerShell:
   ```powershell
   New-NetFirewallRule -DisplayName "Olam Katan companion" -Direction Inbound -Protocol TCP -LocalPort 8790 -Profile Private -Action Allow
   ```
   Your Wi-Fi must be set to *Private* in Windows network settings. Don't let Python itself
   through the firewall: if Windows asks when the doorbell first starts, choose **Cancel**,
   so the rule above (port 8790, home networks only) is the only way in, and the PC stays
   closed on public Wi-Fi.
5. Start it automatically at login. Put a shortcut in `shell:startup` with this target:
   ```
   pythonw.exe "C:\Users\user\Documents\olam-katan\brain\doorbell.py"
   ```
6. In the app, go to parent settings (long-press the gear). Under **התוכי פוקי**, set the
   PC's address, for example `192.168.1.151:8790`, and the [pairing code](#pairing-code).
   A DHCP reservation in the router keeps that address from changing.

## Pairing code

The doorbell answers only requests that carry its **pairing code**, so another device on
the network (a guest's phone, a smart TV) can't make Puki talk or send it recordings.

- The code is made the first time the doorbell starts, printed in its console
  (`pairing code: K7M2-QX9P`), and kept in `brain/pairing_code.txt` (git-ignored). With
  `pythonw` there's no console, so open that file, or the [dashboard](#dashboard).
- Type it once in the app's parent settings, under the PC address. Case and the dash don't
  matter. Below the field, the app shows whether it's connected, the code is wrong, or the
  PC can't be found.
- Wrong codes are answered after a 1-second delay, and an address that sends 10 wrong codes
  within 10 minutes is shut out for 15 minutes.
- `python doorbell.py --new-code` makes a new code, for example if it leaked. Every tablet
  then needs the new code.
- The code travels unencrypted over the home Wi-Fi (plain HTTP). It keeps strangers'
  devices out; it isn't meant to protect against someone who already controls your network.

## Dashboard

While the doorbell runs, **http://127.0.0.1:8793** on the PC shows:

- whether the brain is asleep, waking up or awake, which Gemma runs and on the GPU or the
  CPU, and when it will go back to sleep; a button puts it to sleep right away (handy before
  a training run);
- **Puki's mind:** Gemma E4B or 12B (whichever are downloaded). The choice applies the next
  time Puki wakes up; when 12B is chosen but the GPU is busy, E4B fills in;
- the pairing code, the PC's address, and a QR code with both (for the app to scan once it
  can), plus a button that makes a new code;
- how many idle minutes before it sleeps (saved to `config.json`);
- a live log: what each game asked, what Whisper heard, what Puki said and how long it took,
  and any wrong pairing codes.

The dashboard is served on 127.0.0.1 only, never to the home network, because the log holds
the child's words. The log lives in memory and disappears when the doorbell stops. Pages from
other websites can't read or use it (requests must address it as 127.0.0.1/localhost, and its
buttons need a header a foreign page can't send). The port is `dashboard_port` in the config.

## Trying it

```
python brain/doorbell.py
curl -X POST -H "X-Puki-Code: K7M2-QX9P" localhost:8790/wake      # your code from pairing_code.txt
curl -H "X-Puki-Code: K7M2-QX9P" localhost:8790/health            # wait for "ready"
```
Every request needs the `X-Puki-Code` header; without it the doorbell answers 401.
`/say` takes `{"situation": "...", "lang": "he"}` and returns OGG audio, with the line
itself in the `X-Text` header. `/listen?lang=he` takes a WAV recording and returns the
same, plus `X-Heard` with what Whisper heard.

Testing over USB without the firewall rule: run `adb reverse tcp:8790 tcp:8790` and set
the app's PC address to `127.0.0.1:8790`.
