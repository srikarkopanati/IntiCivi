import re

# ISSUE MAPPING
def map_issue(label):
    if "road" in label:
        return "road", "Road Issue (Pothole / Damaged Road)", "Public Works Department"
    if "sewage" in label or "drain" in label:
        return "sewage", "Sewage Issue (Drainage Overflow)", "Drainage Department"
    if "garbage" in label:
        return "garbage", "Garbage Issue (Waste Management)", "Municipal Sanitation Department"
    if "water" in label:
        return "water", "Water Supply Issue", "Water Supply Department"
    if "electric" in label:
        return "electric", "Electricity Issue", "Electricity Board"
    return "general", "General Issue", "General Department"

# SEVERITY
BASE = {
    "road": 0.8,
    "sewage": 0.9,
    "electric": 0.95,
    "water": 0.7,
    "garbage": 0.5,
    "general": 0.4
}

def compute_severity(k, conf, reports):
    base = BASE.get(k, 0.4)
    score = base*0.6 + conf*0.2 + min(1,0.2*(reports**0.5))*0.2

    if score >= 0.85: lvl="CRITICAL"
    elif score >= 0.7: lvl="HIGH"
    elif score >= 0.5: lvl="MEDIUM"
    else: lvl="LOW"

    return round(score,2), lvl

# TEXT MODEL ONLY
def predict_text(text):
    t = text.lower()

    score = {"road":0,"sewage":0,"garbage":0,"water":0,"electric":0}

    if re.search(r"pothole|road|crack|damage", t): score["road"] += 2
    if re.search(r"sewage|drain|gutter|overflow|dirty water", t): score["sewage"] += 2
    if re.search(r"garbage|trash|waste|dump", t): score["garbage"] += 2
    if re.search(r"leak|pipeline|water supply", t): score["water"] += 2
    if re.search(r"electric|light|transformer|wire|spark", t): score["electric"] += 2

    key = max(score, key=score.get)
    conf = 0.8 if score[key] > 0 else 0.4

    label_map = {
        "road":"pothole road damage",
        "sewage":"sewage drainage overflow dirty water",
        "garbage":"garbage waste trash",
        "water":"clean water leakage pipe",
        "electric":"electric pole wire transformer issue"
    }

    return label_map[key], conf

# FINAL PROCESS
def process(image=None, text=None, reports=1):

    if text is None or text.strip() == "":
        return {"error": "Text input required (image disabled in current version)"}

    label, conf = predict_text(text)

    key, issue, dept = map_issue(label)
    score, level = compute_severity(key, conf, reports)

    return {
        "detected_issue": issue,
        "department": dept,
        "confidence": conf,
        "severity_score": score,
        "severity_level": level
    }