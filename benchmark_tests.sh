#!/bin/bash

# Esegui il log di tutti i commit
git log --oneline | while read -r commit_hash _; do
    # Verifica se il commit ha un genitore
    if [ "$(git rev-parse --quiet --verify "$commit_hash"^ 2>/dev/null)" ]; then
        # Ottieni i nomi dei file aggiunti nel commit corrente
        added_files=$(git diff --name-status "$commit_hash"^.."$commit_hash" | awk '$1 == "A" {print $2}')

        # Loop sui file aggiunti nel commit corrente
        for file in $added_files; do
            # Ottieni i nomi dei metodi aggiunti nel file corrente
            added_methods=$(git show "$commit_hash":"$file" | grep -E '^\+\s*[^{]*\(.*\)' | grep -Eo '\b\w+\b\s*\([^)]*\)')

            # Stampa i nomi dei metodi aggiunti
            if [ -n "$added_methods" ]; then
                echo "Commit: $commit_hash"
                echo "Metodi aggiunti nel file $file:"
                echo "$added_methods"
            fi
        done
    else
        echo "Il commit $commit_hash è il primo nella cronologia e non ha un genitore."
    fi
done
