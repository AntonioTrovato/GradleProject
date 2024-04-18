#!/bin/bash

# Leggi l'output di git log direttamente in un array
mapfile -t commits < <(git log --oneline)

# Loop attraverso ogni commit
for commit_line in "${commits[@]}"; do
    # Estrai hash e messaggio di commit
    commit_hash=$(echo "$commit_line" | cut -d ' ' -f 1)
    commit_message=$(echo "$commit_line" | cut -d ' ' -f 2-)

    # Verifica se il commit ha un genitore
    if [ "$(git rev-parse --abbrev-ref "$commit_hash"^ 2>/dev/null)" ]; then
        # Ottieni i nomi dei file aggiunti nel commit corrente
        added_files=$(git diff --name-status "$commit_hash"^ "$commit_hash" | awk '$1 == "A" {print $2}')

        # Loop sui file aggiunti nel commit corrente
        for file in $added_files; do
            # Ottieni i nomi dei metodi aggiunti nel file corrente
            added_methods=$(git show "$commit_hash":"$file" | grep -E '^\+\s*[^{]*\(.*\)' | grep -Eo '\b\w+\b\s*\([^)]*\)')

            # Stampa i nomi dei metodi aggiunti
            if [ -n "$added_methods" ]; then
                echo "Commit: $commit_hash - $commit_message"
                echo "Metodi aggiunti nel file $file:"
                echo "$added_methods"
            fi
        done
    else
        echo "Il commit $commit_hash è il primo nella cronologia e non ha un genitore."
    fi
done
