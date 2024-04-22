#!/bin/bash

git log

# Leggi gli hash dei due commit più recenti utilizzando git log
commit_corrente=$(git log --format="%H" -n 1)
commit_precedente=$(git log --format="%H" -n 2 | tail -n 1)

echo "Hash del commit corrente: $commit_corrente"
echo "Hash del commit precedente: $commit_precedente"

# Esegui la diff tra i due commit
git diff $commit_precedente $commit_corrente
