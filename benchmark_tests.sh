#!/bin/bash

# Leggi gli hash dei due commit più recenti utilizzando git log
commit_corrente=$(git log --format="%H" -n 1)
commit_precedente=$(git log --format="%H" -n 2 | tail -n 1)

# Esegui la diff tra i due commit
git_diff=$(git diff $commit_precedente $commit_corrente)

echo "$git_diff"

# Utilizza awk per separare la stringa in blocchi
echo "$git_diff" | grep -E '^diff --git ' | while IFS= read -r block; do
    echo "ecco:"
    echo "$block"
    echo "-------------------------"
done
