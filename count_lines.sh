#!/bin/bash

# primarily made by claude, minimised to focus on main purpose.
# currently project has 4.6k total lines

echo "======================================"

# ===== LINE COUNT =====
JAVA_LINES=$(find src -name "*.java" -type f 2>/dev/null | xargs wc -l 2>/dev/null | tail -1 | awk '{print $1}')
DYN_LINES=$(find DYN_Scripts -name "*.dyn" -type f 2>/dev/null | xargs wc -l 2>/dev/null | tail -1 | awk '{print $1}')
TOTAL_LINES=$((JAVA_LINES + DYN_LINES))

echo "Lines of Code:"
echo "  Java: $JAVA_LINES"
echo "  DYN Scripts: $DYN_LINES"
echo "  Total: $TOTAL_LINES"

echo "======================================"