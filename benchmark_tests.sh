#!/bin/bash

# Leggi l'hash del commit corrente
commit_corrente=$(git rev-parse HEAD)

# Leggi l'hash del commit precedente
commit_precedente=$(git log --format="%H" -n 2 | tail -n 1)

# Stampare gli hash dei due commit
echo "Hash del commit corrente: $commit_corrente"
echo "Hash del commit precedente: $commit_precedente"

# Esegui la diff tra i due commit
git diff $commit_precedente $commit_corrente
