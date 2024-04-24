#!/bin/bash

# Leggi gli hash dei due commit più recenti utilizzando git log
commit_corrente=$(git log --format="%H" -n 1)
commit_precedente=$(git log --format="%H" -n 2 | tail -n 1)

# Esegui la diff tra i due commit
git_diff=$(git diff $commit_precedente $commit_corrente)

echo "$git_diff"

# Separare la stringa in righe usando il carattere di nuova linea come delimitatore
blocks=$(echo "$git_diff" | sed -n '/^diff --git/,/^diff --git/{p}')

# Stampa tutti i blocchi trovati
# shellcheck disable=SC2066
for block in "$blocks"; do
    echo "ecco:"
    echo "$block"
    echo "-------------------------"
done
