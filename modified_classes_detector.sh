#!/bin/bash

# Configure git
git config --global user.email "atrovato@unisa.it"
git config --global user.name "AntonioTrovato"

#TODO: IL CONFRONTO TRA I BODY DEI METODI DEVE ESSERE PIU' "FUNZIONALE"??

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

# write in a temporary file the fully qualified names of the modified classes
temp_file="modified_classes.txt"
printf "%s\n" "${modified_classes[@]}" > "$temp_file"

# run the java script
java -jar app/build/libs/app-all.jar "$temp_file"

# delete the file
rm "$temp_file"

# read and print the contents of modified_methods.txt
while IFS= read -r line; do
    echo "$line"
done < "modified_methods.txt"
