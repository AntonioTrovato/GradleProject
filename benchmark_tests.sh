#!/bin/bash

# Array per memorizzare i metodi modificati, aggiunti ed eliminati
declare -a modificati
declare -a aggiunti
declare -a eliminati

# Ottieni la lista dei commit con git log --oneline
commit_list=$(git log --oneline)

# Estrai l'hash del secondo commit dalla lista
second_commit_hash=$(echo "$commit_list" | sed -n '2p' | awk '{print $1}')

# Ottieni la lista dei file modificati, aggiunti o eliminati nell'ultimo commit
diff_output=$(git diff --name-status $second_commit_hash HEAD)

# Scansiona l'output del comando git diff per identificare i metodi modificati, aggiunti ed eliminati
while IFS= read -r line; do
    status=$(echo "$line" | cut -d ' ' -f1)
    file=$(echo "$line" | cut -d ' ' -f2)

    # Estrai i metodi dal file solo se è un file Java
    if [[ $file == *.java ]]; then
        # Estrai solo il nome del file senza estensione e il percorso
        file_name=$(basename "$file" .java)
        package=$(grep -oP 'package\s+\K\w+(\.\w+)*' "$file")
        # Ignora i file di test
        if [[ $file_name != *Test* && $file_name != *test* ]]; then
            case $status in
                M)
                    # Estrai i metodi modificati e aggiungili alla lista modificati
                    methods=$(git diff $second_commit_hash HEAD -- "$file" | grep -E '^\+(?!.*import)(?!.*class)(?!.*\{).*\(.*\)' | sed -E 's/\s*[\+]+[\s*]*//' | sed -E '/^\s*$/d')
                    for method in $methods; do
                        modificati+=("$package.$file_name.$method")
                    done
                    ;;
                A)
                    # Estrai i metodi aggiunti e aggiungili alla lista aggiunti
                    methods=$(grep -E '^\s*(public|protected|private|static)\s+\w+\s+\w+\s*\([^)]*\)\s*\{' "$file" | grep -oP '\b\w+\b\s*\([^)]*\)')
                    for method in $methods; do
                        aggiunti+=("$package.$file_name.$method")
                    done
                    ;;
                D)
                    # Estrai i metodi eliminati e aggiungili alla lista eliminati
                    methods=$(git diff $second_commit_hash HEAD -- "$file" | grep -E '^\-(?!.*import)(?!.*class)(?!.*\{).*\(.*\)' | sed -E 's/\s*[\-]+[\s*]*//' | sed -E '/^\s*$/d')
                    for method in $methods; do
                        eliminati+=("$package.$file_name.$method")
                    done
                    ;;
                *)
                    ;;
            esac
        fi
    fi
done <<< "$diff_output"

# Stampare i risultati per debug
echo "Modificati:"
for method in "${modificati[@]}"; do
    echo "$method"
done

echo "Aggiunti:"
for method in "${aggiunti[@]}"; do
    echo "$method"
done

echo "Eliminati:"
for method in "${eliminati[@]}"; do
    echo "$method"
done
