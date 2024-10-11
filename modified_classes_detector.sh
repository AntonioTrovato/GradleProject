#!/bin/bash

# Configure git
git config --global user.email "atrovato@unisa.it"
git config --global user.name "AntonioTrovato"

#TODO: JU2JMH SVUOTA LE CLASSI DI BENCHMARK NON
#TODO: INTERESSATE DAL COMMIT (SE UNA CLASSE
#TODO: DI BENCHMARK C'ERA LA SVUOTA, SE NON C'ERA
#TODO: LA CREA (OSSIA CI DEVE ESSERE LA CLASSE DI
#TODO: TEST DI UNITA') SENZA BENCHMARK

# Read the hashes of the last two commits using git log
current_commit=$(git log --format="%H" -n 1)
previous_commit=$(git log --format="%H" -n 2 | tail -n 1)

# Make diff between the two commits
git_diff=$(git diff -U0 --minimal $previous_commit $current_commit)

echo "GIT DIFF:"

echo "$git_diff"

echo "=================================================================================="

# Initialize empty arrays to store deleted and added methods
modified_classes=()

# Read the diff string line by line
while IFS= read -r line; do
  # Check if the line starts with "diff --git"
  if [[ $line =~ ^\+++\ .*\/main\/java\/(.*\/)?([^\/]+)\.java$ ]]; then
    packages="${BASH_REMATCH[1]}"
    file_name="${BASH_REMATCH[2]}"

    if [[ -n "$packages" ]]; then
      packages="${packages%/}"
      packages="${packages}."  # add . if packages is not empty to obtain a correct path
    fi

    # Replace slashes with dots and remove .java extension
    class_name="${packages//\//.}${file_name%.java}"

    modified_classes+=("$class_name")
  fi
done <<< "$git_diff"

# Print each element of the list
for modified_class in "${modified_classes[@]}"; do
  echo "Modified class:"
  echo "$modified_class"
done

# Scrivi le classi modificate in un file temporaneo
temp_file="modified_classes.txt"
printf "%s\n" "${modified_classes[@]}" > "$temp_file"

# Esegui lo script Java passando il file delle classi modificate
java -cp ./app/build/classes/java/main/:app/build/libs/* ASTGenerator "$temp_file"

# Rimuovi il file temporaneo
rm "$temp_file"
