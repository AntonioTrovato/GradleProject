#!/bin/bash

# Leggi gli hash dei due commit più recenti utilizzando git log
commit_corrente=$(git log --format="%H" -n 1)
commit_precedente=$(git log --format="%H" -n 2 | tail -n 1)

# Esegui la diff tra i due commit
git_diff=$(git diff $commit_precedente $commit_corrente)

echo "$git_diff"

echo "=================================================================================="

# Initialize an array to hold the substrings
declare -a substrings

# Using awk to split the string into substrings based on "hi"
while read -r substring; do
    substrings+=("$substring")
done < <(awk 'BEGIN {RS="diff --git"; ORS=""} {print $0}' <<< "$git_diff")

# Print each substring
for ((i=0; i<${#substrings[@]}; i++)); do
    echo "Substring $((i+1)):"
    echo "${substrings[i]}"
    echo
done
