#!/bin/bash

# ============================================================
# compile_and_test.sh
# Script untuk compile dan menjalankan unit test JUnit 5
# Project: Old Java Music Player - Unit Test
# ============================================================

set -e

# --- Konfigurasi Path ---
PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC_MAIN="$PROJECT_ROOT/src"
SRC_TEST="$PROJECT_ROOT/test"
LIB_DIR="$PROJECT_ROOT/lib"
OUT_DIR="$PROJECT_ROOT/out"
JUNIT_JAR="$LIB_DIR/junit-platform-console-standalone-1.10.2.jar"

# --- Library JARs dari project utama ---
MAIN_LIBS=$(find "$LIB_DIR" -name "*.jar" ! -name "junit-platform-console-standalone*.jar" | tr '\n' ':')

# --- Bersihkan output lama ---
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR/main" "$OUT_DIR/test"

echo "============================================================"
echo "  OLD JAVA MUSIC PLAYER — Unit Test Runner"
echo "============================================================"
echo ""

# --- Langkah 1: Compile source utama ---
echo "[1/3] Mengcompile source utama..."
find "$SRC_MAIN" -name "*.java" > "$OUT_DIR/sources_main.txt"
javac -cp "${MAIN_LIBS}" \
      -d "$OUT_DIR/main" \
      @"${OUT_DIR}/sources_main.txt"
echo "      ✔ Source utama berhasil dicompile."
echo ""

# --- Langkah 2: Compile file test (stub + test class) ---
echo "[2/3] Mengcompile file unit test..."
find "$SRC_TEST" -name "*.java" > "$OUT_DIR/sources_test.txt"
javac -cp "${MAIN_LIBS}${JUNIT_JAR}:$OUT_DIR/main" \
      -d "$OUT_DIR/test" \
      @"${OUT_DIR}/sources_test.txt"
echo "      ✔ File unit test berhasil dicompile."
echo ""

# --- Langkah 3: Jalankan test dengan JUnit 5 ---
echo "[3/3] Menjalankan unit test..."
echo "------------------------------------------------------------"
java -jar "$JUNIT_JAR" \
     --cp "$OUT_DIR/main:$OUT_DIR/test:${MAIN_LIBS}" \
     --scan-class-path \
     --details=verbose
echo "------------------------------------------------------------"
echo ""
echo "  ✔ Unit test selesai dijalankan."
echo "============================================================"
