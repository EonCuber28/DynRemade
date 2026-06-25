#!/usr/bin/env bash
# ═══════════════════════════════════════════════════════════════════
#  java_analyze.sh  –  Java project deep-dive analyzer
#  Drop in your project root:  chmod +x java_analyze.sh && ./java_analyze.sh
# ═══════════════════════════════════════════════════════════════════
set -euo pipefail

BOLD='\033[1m'; DIM='\033[2m'; R='\033[0m'
RED='\033[91m'; YEL='\033[93m'; GRN='\033[92m'
CYN='\033[96m'; MAG='\033[95m'; WHT='\033[97m'

OLLAMA_URL="http://192.168.1.87:11434/api/generate"
OLLAMA_MODEL="richardyoung/gpt-oss-20b-abliterated:latest"
ROAST_THRESHOLD=101    # Grade D or F → call Ollama

# ── Banner ─────────────────────────────────────────────────────────
echo ""
echo -e "        ${YEL}( (${R}"
echo -e "         ${YEL}) )${R}     ${CYN}${BOLD}╔══════════════════════════════════════════════╗${R}"
echo -e "      ${YEL}.........${R}   ${CYN}${BOLD}║☕   J A V A   P R O J E C T   A N A L I S I S║${R}"
echo -e "      ${YEL}|       |]${R}  ${CYN}${BOLD}╚══════════════════════════════════════════════╝${R}"
echo -e "      ${YEL}\\       /${R}"
echo -e "       ${YEL}\`-----'${R}"
echo ""

# ── Find Java files ────────────────────────────────────────────────
WORK=$(mktemp -d)
FILELIST="$WORK/files.txt"
OUTPUT="$WORK/output.txt"
GRADE_FILE="$WORK/grade.txt"
STATS_FILE="$WORK/stats.txt"
trap 'rm -rf "$WORK"' EXIT

find . -name "*.java" \
    ! -path "*/build/*"    ! -path "*/target/*" \
    ! -path "*/.git/*"     ! -path "*/out/*"    \
    ! -path "*/bin/*"      ! -path "*/.gradle/*" \
    ! -path "*/generated/*" \
    2>/dev/null | sort > "$FILELIST"

N=$(wc -l < "$FILELIST" | tr -d ' ')

if (( N == 0 )); then
  echo -e "  ${RED}No .java files found.${R} Make sure this script is in the project root."
  exit 1
fi

echo -e "  ${DIM}Found ${BOLD}${N}${R}${DIM} Java file(s) in ${BOLD}$(pwd)${R}"
echo -e "  ${DIM}Python progress appears below — results follow automatically.${R}\n"

# ── Run the Python analyzer ────────────────────────────────────────
#    Python writes per-file progress to stderr (terminal),
#    formatted results to stdout (captured → $OUTPUT).
# ──────────────────────────────────────────────────────────────────
python3 - "$FILELIST" "$GRADE_FILE" "$STATS_FILE" > "$OUTPUT" <<'PYEOF'
import sys, re, os

# ── ANSI ──────────────────────────────────────────────────────────
R='\033[0m'; BOLD='\033[1m'; DIM='\033[2m'
RED='\033[91m'; YEL='\033[93m'; GRN='\033[92m'
CYN='\033[96m'; MAG='\033[95m'; WHT='\033[97m'

JAVA_KW = {
    'abstract','assert','boolean','break','byte','case','catch','char',
    'class','const','continue','default','do','double','else','enum',
    'extends','final','finally','float','for','goto','if','implements',
    'import','instanceof','int','interface','long','native','new','null',
    'package','private','protected','public','record','return','sealed',
    'short','static','strictfp','super','switch','synchronized','this',
    'throw','throws','transient','true','false','try','var','void',
    'volatile','while'
}

# ── Column widths for the per-file table ──────────────────────────
WF=40; WL=6; WM=7; WCC=6; WT=5; WH=8

# ── Visible-length-aware column padding ───────────────────────────
_ANSI = re.compile(r'\033\[[^m]*m')
def vlen(s):   return len(_ANSI.sub('', s))
def lj(s, w):  return s + ' ' * max(0, w - vlen(s))
def rj(s, w):  return ' ' * max(0, w - vlen(s)) + s

# ── Visual helpers ─────────────────────────────────────────────────
def hbar(val, total, width=28, full='█', empty='░'):
    n = int(round(val / total * width)) if total > 0 else 0
    return full * min(n, width) + empty * (width - min(n, width))

def cc_col(cc):
    if cc <= 2:  return GRN
    if cc <= 5:  return YEL
    return RED

def h_col(score):
    if score >= 75: return GRN
    if score >= 50: return YEL
    return RED

def td_col(n):
    if n == 0:   return GRN
    if n <= 2:   return YEL
    return RED

def grade_col(g):
    return {'A':GRN,'B':CYN,'C':YEL,'D':MAG,'F':RED}.get(g, R)

def trunc_l(s, n):   # keep right side
    return s if len(s) <= n else '…' + s[-(n-1):]

def trunc_r(s, n):   # keep left side
    return s if len(s) <= n else s[:n-1] + '…'

# ── Analysis helpers ───────────────────────────────────────────────
def strip_comments(src):
    src = re.sub(r'/\*.*?\*/', ' ', src, flags=re.DOTALL)
    src = re.sub(r'//[^\n]*',  ' ', src)
    return src

def count_lines(raw):
    ls   = raw.split('\n')
    tot  = len(ls)
    blk  = sum(1 for l in ls if not l.strip())
    cmt  = sum(1 for l in ls if re.match(r'\s*(//|/\*|\*)', l))
    return tot, tot - blk - cmt, cmt, blk

def extract_vars(clean):
    hits = re.findall(
        r'(?:int|long|double|float|boolean|char|byte|short|String|var'
        r'|[A-Z][A-Za-z0-9_$<>]*)\s+([a-zA-Z_$][a-zA-Z0-9_$]*)\s*[=;,):]',
        clean
    )
    return {v for v in hits if v not in JAVA_KW}

# ── Method detector (regex + brace tracking) ──────────────────────
_METH_RE = re.compile(
    r'^\s*(?:@\w+(?:\([^)]*\))?\s+)*'
    r'((?:(?:public|private|protected|static|final|abstract|'
    r'synchronized|native|default)\s+)+)'
    r'(?:[\w<>\[\]$]+\s+)?(\w+)\s*\([^;{]*\)\s*'
    r'(?:throws\s+[\w\s,$]+)?\s*([{;])'
)
_CTRL = re.compile(r'^\s*(if|else|for|while|do|switch|try|catch|finally)\b')

def find_methods(cls):
    out = []; n = len(cls); i = 0
    while i < n:
        ln = cls[i]; m = _METH_RE.match(ln)
        if m and not _CTRL.match(ln):
            mods = m.group(1).split(); name = m.group(2); term = m.group(3)
            vis = 'package'
            for v in ('public','private','protected'):
                if v in mods: vis = v; break
            if term == ';':
                out.append({'name':name,'start':i+1,'end':i+1,
                            'lines':1,'cc':1,'vis':vis})
            else:
                depth = 0; end = i
                for j in range(i, min(i+600, n)):
                    depth += cls[j].count('{') - cls[j].count('}')
                    if j > i and depth <= 0: end = j; break
                body = '\n'.join(cls[i:end+1])
                cc = len(re.findall(
                    r'\b(if|for|while|do|switch|case|catch)\b|&&|\|\|', body
                )) + 1
                out.append({'name':name,'start':i+1,'end':end+1,
                            'lines':end-i+1,'cc':cc,'vis':vis})
        i += 1
    return out

# ── Class detector ─────────────────────────────────────────────────
_CLS_RE = re.compile(
    r'^\s*(?:(?:public|private|protected|abstract|final|static|sealed)\s+)*'
    r'(class|interface|enum|record)\s+(\w+)'
)

def find_classes(cls):
    out = []; n = len(cls)
    for i, ln in enumerate(cls):
        m = _CLS_RE.match(ln)
        if m and '{' in ln:
            depth = 0; end = i
            for j in range(i, n):
                depth += cls[j].count('{') - cls[j].count('}')
                if j > i and depth <= 0: end = j; break
            out.append({'kind':m.group(1),'name':m.group(2),
                        'start':i+1,'end':end+1,'lines':end-i+1})
    return out

def file_health(avg_cc, todos):
    cc_s  = max(0, 100 - (max(1, avg_cc) - 1) * 15)
    tod_s = max(0, 100 - todos * 20)
    return int(cc_s * 0.65 + tod_s * 0.35)

# ── Per-file analysis ──────────────────────────────────────────────
def analyze(path):
    try:
        raw = open(path, encoding='utf-8', errors='replace').read()
    except Exception:
        return None
    clean   = strip_comments(raw)
    cls     = clean.split('\n')
    tot, code, cmt, blk = count_lines(raw)
    todos   = len(re.findall(r'\b(TODO|FIXME|HACK|XXX)\b', raw))
    vars_   = extract_vars(clean)
    methods = find_methods(cls)
    classes = find_classes(cls)
    vis = {'public':0,'private':0,'protected':0,'package':0}
    for m in methods: vis[m['vis']] += 1
    avg_cc  = sum(m['cc'] for m in methods) / len(methods) if methods else 0
    return {
        'path':path, 'total':tot, 'code':code, 'cmt':cmt, 'blank':blk,
        'todos':todos, 'vars':vars_, 'methods':methods, 'classes':classes,
        'vis':vis, 'avg_cc':avg_cc,
        'longest': max(methods, key=lambda m:m['lines'], default=None),
        'most_cx': max(methods, key=lambda m:m['cc'],   default=None),
        'largest': max(classes, key=lambda c:c['lines'], default=None),
    }

# ── Grade ──────────────────────────────────────────────────────────
def calc_grade(results):
    all_m   = [m for r in results for m in r['methods']]
    nm      = len(all_m) or 1
    tot_l   = sum(r['total'] for r in results) or 1
    tot_cmt = sum(r['cmt']   for r in results)
    tot_tod = sum(r['todos'] for r in results)
    avg_cc  = sum(m['cc']    for m in all_m) / nm
    avg_ml  = sum(m['lines'] for m in all_m) / nm
    t100    = tot_tod / tot_l * 100
    cmt_pct = tot_cmt / tot_l * 100

    cc_s   = max(0, min(100, 100 - (avg_cc - 1) * 12))
    sz_s   = max(0, min(100, 100 - max(0, avg_ml - 8) * 1.8))
    td_s   = max(0, min(100, 100 - t100 * 18))
    cm_s   = max(0, min(100, cmt_pct * 5))
    score  = int(cc_s*0.35 + sz_s*0.25 + td_s*0.25 + cm_s*0.15)

    grade = 'F'
    if score >= 80: grade = 'A'
    elif score >= 65: grade = 'B'
    elif score >= 50: grade = 'C'
    elif score >= 35: grade = 'D'
    return {'score':score,'grade':grade,
            'cc_s':int(cc_s),'sz_s':int(sz_s),'td_s':int(td_s),'cm_s':int(cm_s),
            'avg_cc':avg_cc,'avg_ml':avg_ml,'t100':t100,'cmt_pct':cmt_pct}

# ── Section header ─────────────────────────────────────────────────
def sec(title):
    w = 56; t = f'  {title}  '
    p = max(0, w - len(t)); pl = p//2; pr = p - pl
    print(f"\n  {CYN}{'─'*pl}{BOLD} {title} {R}{CYN}{'─'*pr}{R}\n")

# ── Per-file table ─────────────────────────────────────────────────
def _hsep(l, m, r_):
    return (f"  {l}{'─'*(WF+2)}{m}{'─'*(WL+2)}{m}{'─'*(WM+2)}"
            f"{m}{'─'*(WCC+2)}{m}{'─'*(WT+2)}{m}{'─'*(WH+2)}{r_}")

def print_table(results, base_dir):
    print(_hsep('┌','┬','┐'))
    hdr = (f"  │ {BOLD}{'File':<{WF}}{R} │ {BOLD}{'Lines':>{WL}}{R}"
           f" │ {BOLD}{'Methods':>{WM}}{R} │ {BOLD}{'Avg CC':>{WCC}}{R}"
           f" │ {BOLD}{'TODO':>{WT}}{R} │ {BOLD}{'Health':<{WH}}{R} │")
    print(hdr)
    print(_hsep('├','┼','┤'))

    for r in results:
        rel  = os.path.relpath(r['path'], base_dir)
        fn   = trunc_l(rel, WF)
        nm   = len(r['methods'])
        cc   = r['avg_cc']
        todo = r['todos']
        h    = file_health(cc, todo)
        cs   = f"{cc:.1f}"
        fn_c = lj(f"{CYN}{fn}{R}", WF)
        cc_c = rj(f"{cc_col(cc)}{cs}{R}", WCC)
        td_c = rj(f"{td_col(todo)}{todo}{R}", WT)
        hb_c = lj(f"{h_col(h)}{hbar(h,100,WH)}{R}", WH)
        print(f"  │ {fn_c} │ {r['total']:>{WL}} │ {nm:>{WM}} │ {cc_c} │ {td_c} │ {hb_c} │")

    print(_hsep('├','┼','┤'))
    # Totals row
    all_cc  = [m['cc'] for r in results for m in r['methods']]
    g_cc    = sum(all_cc)/len(all_cc) if all_cc else 0
    g_lines = sum(r['total'] for r in results)
    g_meth  = sum(len(r['methods']) for r in results)
    g_todo  = sum(r['todos'] for r in results)
    g_h     = file_health(g_cc, g_todo / len(results))
    cs      = f"{g_cc:.1f}"
    fn_c = lj(f"{BOLD}∑  TOTALS{R}", WF)
    cc_c = rj(f"{cc_col(g_cc)}{BOLD}{cs}{R}", WCC)
    td_c = rj(f"{td_col(g_todo)}{BOLD}{g_todo}{R}", WT)
    hb_c = lj(f"{h_col(g_h)}{hbar(g_h,100,WH)}{R}", WH)
    print(f"  │ {fn_c} │ {BOLD}{g_lines:>{WL}}{R} │ {BOLD}{g_meth:>{WM}}{R} │ {cc_c} │ {td_c} │ {hb_c} │")
    print(_hsep('└','┴','┘'))

# ── Summary ────────────────────────────────────────────────────────
def lrow(label, val, bar_s='', note=''):
    s = f"  {CYN}{label:<24}{R}  {BOLD}{val:>6}{R}"
    if bar_s: s += f"  {DIM}{bar_s}{R}"
    if note:  s += f"  {note}"
    print(s)

def print_summary(results, base_dir):
    tot_l   = sum(r['total'] for r in results)
    tot_c   = sum(r['code']  for r in results)
    tot_cmt = sum(r['cmt']   for r in results)
    tot_b   = sum(r['blank'] for r in results)
    tot_m   = sum(len(r['methods'])  for r in results)
    tot_cls = sum(len(r['classes'])  for r in results)
    tot_tod = sum(r['todos'] for r in results)
    all_v   = set()
    for r in results: all_v |= r['vars']
    vis = {'public':0,'private':0,'protected':0,'package':0}
    for r in results:
        for k,v in r['vis'].items(): vis[k] += v

    # ── Lines ──────────────────────────────────────────────────────
    sec('LINES')
    lrow('Total lines',   tot_l, hbar(tot_l, tot_l))
    lrow('Code lines',    tot_c, hbar(tot_c, tot_l))
    lrow('Comment lines', tot_cmt, hbar(tot_cmt, tot_l))
    lrow('Blank lines',   tot_b, hbar(tot_b, tot_l))

    # ── Structure ──────────────────────────────────────────────────
    sec('STRUCTURE')
    lrow('Java files',            len(results))
    lrow('Classes / Interfaces',  tot_cls)
    lrow('Total methods',         tot_m)
    lrow('Distinct variable names', len(all_v))

    tod_note = ''
    if tot_tod > 0:
        col = YEL if tot_tod <= 3 else RED
        tod_note = f"{col}⚠  {tot_tod} items of outstanding debt{R}"
    lrow('TODO / FIXME / HACK', tot_tod, note=tod_note)

    # ── Method visibility bar chart ────────────────────────────────
    sec('METHOD  VISIBILITY')
    max_v = max(vis.values()) or 1
    cols  = {'public':GRN,'private':RED,'protected':YEL,'package':DIM}
    for k, col in cols.items():
        n  = vis[k]
        b  = hbar(n, max_v, width=20)
        pct = int(n / tot_m * 100) if tot_m else 0
        print(f"  {col}{BOLD}{k:<12}{R}  {col}{b}{R}  {BOLD}{n:>3}{R}  {DIM}({pct}%){R}")

    # ── Hall of fame ───────────────────────────────────────────────
    sec('HALL  OF  FAME')
    def best(key, attr, results):
        cands = [(r[key], r['path']) for r in results if r[key]]
        if not cands: return None, ''
        item, path = max(cands, key=lambda x: x[0][attr])
        return item, os.path.relpath(path, base_dir)

    lg_cls, lg_f  = best('largest', 'lines', results)
    ln_m,   ln_f  = best('longest', 'lines', results)
    cx_m,   cx_f  = best('most_cx', 'cc',    results)

    if lg_cls:
        print(f"  🏆  {BOLD}Largest class   {R}{CYN}{lg_cls['name']}{R}"
              f"  in {DIM}{trunc_r(lg_f,35)}{R}"
              f"  {YEL}({lg_cls['lines']} lines){R}")
    if ln_m:
        print(f"  📏  {BOLD}Longest method  {R}{CYN}{ln_m['name']}{R}"
              f"  in {DIM}{trunc_r(ln_f,35)}{R}"
              f"  {YEL}({ln_m['lines']} lines){R}")
    if cx_m:
        col = cc_col(cx_m['cc'])
        print(f"  🔥  {BOLD}Most complex    {R}{CYN}{cx_m['name']}{R}"
              f"  in {DIM}{trunc_r(cx_f,35)}{R}"
              f"  {col}(CC: {cx_m['cc']}){R}")

# ── Grade display ──────────────────────────────────────────────────
BIG = {
    'A': ["  ██  ","  ██  "," █  █ ","██████","█    █","█    █"],
    'B': ["█████ ","█    █","█████ ","█    █","█    █","█████ "],
    'C': [" ████ ","█     ","█     ","█     ","█     "," ████ "],
    'D': ["████  ","█   █ ","█    █","█    █","█   █ ","████  "],
    'F': ["██████","█     ","████  ","█     ","█     ","█     "],
}
QUIPS = {
    'A': "Clean, readable, maintainable. Someone actually cares.",
    'B': "Solid work. A few rough edges, but nothing embarrassing.",
    'C': "Gets the job done. Your future self will have questions.",
    'D': "Functional, technically. Let's leave it at that.",
    'F': "This code exists. We'll give you that much.",
}

def print_grade(g):
    score = g['score']; grade = g['grade']
    gc = grade_col(grade)
    sec('GRADE')
    big = BIG.get(grade, BIG['F'])
    side = [
        "",
        f"  Score:  {gc}{BOLD}{score} / 100{R}",
        "",
        f"  {DIM}{QUIPS.get(grade,'')}{R}",
        "",
        "",
    ]
    for i, row in enumerate(big):
        print(f"    {gc}{BOLD}{row}{R}   {side[i]}")
    print()
    comps = [
        (f"Complexity   (35%)", g['cc_s']),
        (f"Method size  (25%)", g['sz_s']),
        (f"TODO debt    (25%)", g['td_s']),
        (f"Comments     (15%)", g['cm_s']),
    ]
    for label, val in comps:
        col = GRN if val >= 75 else (YEL if val >= 50 else RED)
        b   = hbar(val, 100, width=32)
        print(f"  {label}  {col}{b}{R}  {col}{BOLD}{val:>3}/100{R}")
    print()
    foot = '─' * 58
    print(f"  {DIM}{foot}{R}")
    print()

# ── Main ───────────────────────────────────────────────────────────
def main():
    filelist, grade_path, stats_path = sys.argv[1], sys.argv[2], sys.argv[3]
    with open(filelist) as f:
        files = [l.strip() for l in f if l.strip()]
    if not files:
        print("No files.", file=sys.stderr); return

    base = os.getcwd()
    files.sort(key=lambda p: (os.path.dirname(p), os.path.basename(p)))
    n = len(files)

    results = []
    for i, path in enumerate(files):
        bn = os.path.basename(path)
        print(f"\r  {CYN}⟳{R}  [{i+1}/{n}]  {bn:<50}",
              file=sys.stderr, end='', flush=True)
        r = analyze(path)
        if r: results.append(r)
    print(f"\r  {GRN}✓{R}  Analyzed {n} file(s) successfully{'':50}", file=sys.stderr)
    print('', file=sys.stderr)

    gi = calc_grade(results)

    # ── Output ───────────────────────────────────────────────────
    print(f"\n  {CYN}{BOLD}{'─'*26}  PER-FILE BREAKDOWN  {'─'*26}{R}\n")
    print_table(results, base)
    print(f"\n  {CYN}{BOLD}{'─'*27}  PROJECT SUMMARY  {'─'*27}{R}")
    print_summary(results, base)
    print_grade(gi)

    # Write grade for bash Ollama decision
    with open(grade_path, 'w') as f:
        f.write(f"{gi['score']}|{gi['grade']}\n")

    # Write readable stats for the Ollama prompt
    all_m   = [m for r in results for m in r['methods']]
    nm      = len(all_m) or 1
    tot_l   = sum(r['total'] for r in results)
    tot_tod = sum(r['todos'] for r in results)
    cmt_pct = sum(r['cmt'] for r in results) / tot_l * 100 if tot_l else 0
    with open(stats_path, 'w') as f:
        f.write(
            f"{n} Java files, {tot_l} total lines, "
            f"average cyclomatic complexity per method: {gi['avg_cc']:.1f}, "
            f"average method length: {gi['avg_ml']:.0f} lines, "
            f"TODO/FIXME/HACK count: {tot_tod} ({gi['t100']:.1f} per 100 lines), "
            f"comment ratio: {cmt_pct:.0f}%, "
            f"score breakdown — complexity: {gi['cc_s']}/100, "
            f"method size: {gi['sz_s']}/100, "
            f"TODO debt: {gi['td_s']}/100, "
            f"comments: {gi['cm_s']}/100"
        )

main()
PYEOF

echo ""
cat "$OUTPUT"

# ── Read grade ─────────────────────────────────────────────────────
SCORE=100; GRADE="A"
if [[ -f "$GRADE_FILE" ]]; then
    IFS='|' read -r SCORE GRADE < "$GRADE_FILE"
fi

# ── Ollama roast (grades D or F) ───────────────────────────────────
if (( SCORE < ROAST_THRESHOLD )) && command -v curl &>/dev/null; then
    STATS=$(cat "$STATS_FILE" 2>/dev/null || echo "unknown project stats")
    PROMPT="A Java developer just received a code quality score of ${SCORE}/100 (letter grade: ${GRADE}) on their project. Their shameful stats: ${STATS}. Write a short, funny, savage-but-constructive roast in exactly 2-3 sentences. Be specific to the actual numbers. End with one actionable tip. Plain text only, no markdown."

    PAYLOAD=$(python3 -c "
import json, sys
prompt = sys.argv[1]
print(json.dumps({'model': '$OLLAMA_MODEL', 'prompt': prompt, 'stream': True}))
" "$PROMPT" 2>/dev/null || echo '{}')

    # ── Streaming word-wrap renderer ───────────────────────────────
    cat > "$WORK/streamer.py" << 'PYSTREAM'
import json, sys

TEXT_W    = 58      # max content chars per line (fits inside the box)
INDENT    = "  "    # left margin matching the box border indent

in_think  = False
think_buf = ""
col       = 0       # content chars written on the current line
word_buf  = ""      # accumulates a partial word across tokens

def flush_word():
    global col, word_buf
    if not word_buf:
        return
    w        = word_buf
    word_buf = ""
    if col == 0:
        sys.stdout.write(INDENT + w)
        col = len(w)
    elif col + 1 + len(w) > TEXT_W:
        sys.stdout.write("\n" + INDENT + w)
        col = len(w)
    else:
        sys.stdout.write(" " + w)
        col += 1 + len(w)
    sys.stdout.flush()

def emit_text(text):
    global col, word_buf
    for ch in text:
        if ch == " ":
            flush_word()
        elif ch == "\n":
            flush_word()
            if col > 0:
                sys.stdout.write("\n")
                sys.stdout.flush()
                col = 0
        else:
            word_buf += ch

for raw in sys.stdin:
    raw = raw.strip()
    if not raw:
        continue
    try:
        d = json.loads(raw)
    except Exception:
        continue

    token = d.get("response", "")
    done  = d.get("done", False)
    think_buf += token

    # Drain think_buf: emit visible text, skip <think>…</think> blocks.
    # Leave a 7-char tail when no tag boundary is found so a tag split
    # across tokens is never accidentally emitted or discarded.
    changed = True
    while changed:
        changed = False
        if in_think:
            end = think_buf.find("</think>")
            if end >= 0:
                think_buf = think_buf[end + 8:]
                in_think  = False
                changed   = True
        else:
            start = think_buf.find("<think>")
            if start >= 0:
                emit_text(think_buf[:start])
                think_buf = think_buf[start + 7:]
                in_think  = True
                changed   = True
            else:
                safe = max(0, len(think_buf) - 7)
                if safe > 0:
                    emit_text(think_buf[:safe])
                    think_buf = think_buf[safe:]
                    changed   = True

    if done:
        break

# Flush anything left after the stream closes
if not in_think:
    emit_text(think_buf)
flush_word()
if col > 0:
    sys.stdout.write("\n")
sys.stdout.write("\n")
sys.stdout.flush()
PYSTREAM

    echo -e "  ${RED}${BOLD}┌──────────────────────────────────────────────────────────┐${R}"
    echo -e "  ${RED}${BOLD}│   🤖  Grade ${GRADE} detected — consulting the AI judge...   │${R}"
    echo -e "  ${RED}${BOLD}└──────────────────────────────────────────────────────────┘${R}"
    echo ""
    echo -e "  ${RED}┌─ The Verdict ──────────────────────────────────────────────┐${R}"
    echo ""

    curl -s --no-buffer --max-time 300 \
        -X POST "$OLLAMA_URL" \
        -H "Content-Type: application/json" \
        -d "$PAYLOAD" 2>/dev/null \
        | python3 -u "$WORK/streamer.py" \
        || echo -e "  ${DIM}(Could not reach Ollama at ${OLLAMA_URL} — the code escapes judgment)${R}\n"

    echo -e "  ${RED}└────────────────────────────────────────────────────────────┘${R}"
    echo ""
fi