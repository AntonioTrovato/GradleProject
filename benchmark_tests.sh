#!/bin/bash

# Ottieni l'hash del primo commit nella cronologia
first_commit=$(git rev-list --max-parents=0 HEAD)

# Ottieni la lista dei commit
commit_list=$(git rev-list --reverse HEAD)
echo "Lista dei commit:"
echo "$commit_list"

# Loop attraverso ogni commit
for commit_hash in $commit_list; do
    echo "Elaborazione del commit: $commit_hash"
    # Verifica se il commit è il primo nella cronologia
    if [ "$commit_hash" == "$first_commit" ]; then
        echo "Il commit $commit_hash è il primo nella cronologia e non ha un genitore."
    else
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
    fi
done
