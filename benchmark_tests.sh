#!/bin/bash

# Leggi gli hash dei due commit più recenti utilizzando git log
commit_corrente=$(git log --format="%H" -n 1)
commit_precedente=$(git log --format="%H" -n 2 | tail -n 1)

# Esegui la diff tra i due commit
git_diff=$(git diff $commit_precedente $commit_corrente)

echo "$git_diff"

# Separare la stringa in righe usando il carattere di nuova linea come delimitatore
IFS=$'\n'

# Array per memorizzare i blocchi
blocks=()

# Indice per tenere traccia dell'inizio di un blocco
start_index=0

# Loop attraverso ogni riga nella stringa
for ((i=0; i<${#git_diff[@]}; i++)); do
    # Se la riga inizia con "diff --git percorso_file_i percorso_file_i"
    if [[ ${git_diff[$i]} == "diff --git"* ]]; then
        # Se non è il primo blocco, aggiungi il blocco precedente all'array
        if [[ $i -ne $start_index ]]; then
            blocks+=("${git_diff[@]:start_index:i-start_index}")
        fi
        # Imposta l'indice di inizio per il nuovo blocco
        start_index=$i
    fi
done

# Aggiungi l'ultimo blocco
blocks+=("${git_diff[@]:start_index}")

# Stampa tutti i blocchi trovati
for block in "${blocks[@]}"; do
    echo "ecco:"
    echo "$block"
    echo "-------------------------"
done
