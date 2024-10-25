#!/bin/bash

# Configure git
git config --global user.email "atrovato@unisa.it"
git config --global user.name "AntonioTrovato"

#TODO: IL CONFRONTO TRA I BODY DEI METODI DEVE ESSERE PIU' "FUNZIONALE"??

# File paths
MODIFIED_METHODS_FILE="modified_methods.txt"
COVERAGE_MATRIX_FILE="app/src/coverage-matrix.json"

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

# Initialize an empty list for test methods
declare -a test_methods

# Read modified methods from the file into an array
mapfile -t modified_methods < "$MODIFIED_METHODS_FILE"

# Read the coverage-matrix.json and extract test methods
# Assuming jq is installed for JSON parsing
for method in "${modified_methods[@]}"; do
    echo "Processing method: $method"
    # Use jq to find test cases that cover the current method
    test_cases=$(jq -r --arg method "$method" '
        to_entries | map(select(.value | index($method))) | .[].key
    ' "$COVERAGE_MATRIX_FILE")

    # Append found test cases to the test_methods array
    while IFS= read -r test_case; do
        test_methods+=("$test_case")
    done <<< "$test_cases"
done

# Print the list of test methods
echo "Test methods covering modified methods:"
printf '%s\n' "${test_methods[@]}"

# read and print the contents of modified_methods.txt
while IFS= read -r line; do
    echo "$line"
done < "modified_methods.txt"
