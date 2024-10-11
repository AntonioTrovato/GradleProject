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

# Initialize an empty commit_blocks list
declare -a commit_blocks

# Initialize an empty block
block=""

# Read the diff string line by line
while IFS= read -r line; do
  # Check if the line starts with "diff --git"
  if [[ $line == "diff --git"* ]]; then
      # If yes, add the current block to commit_blocks and reset the block
      if [ -n "$block" ]; then
        commit_blocks+=("$block")
        block=""
      fi
  fi
  # Add the current line to the block
  block+="$line"$'\n'
done <<< "$git_diff"

# Add the last block to commit_blocks
if [ -n "$block" ]; then
    commit_blocks+=("$block")
fi

# Initialize empty arrays to store deleted and added methods
line_numbers=()

# Print the commit_blocks
for commit_block in "${commit_blocks[@]}"; do
  # Extract the first line of the string
  first_line=$(echo "$commit_block" | head -n 1)
  echo "$first_line"

  # Check if the first line matches the pattern "diff --git path_1 path_2"
  if [[ $first_line =~ ^diff\ --git\ .*\/main\/java\/(.*\/)?([^\/]+)\.java\ .*$ ]]; then
    packages="${BASH_REMATCH[1]}"
    file_name=""
    class_name=""
    #if packages do not contains / it is a class name (the path was java/ClassName.java)
    if [[ "$packages" != */* ]]; then
      file_name=packages
      class_name="${file_name}.java"
    fi
    #otherwise it is all ok and the path was java/.../ClassName.java
    if [[ "$packages" == */* ]]; then
      file_name="${BASH_REMATCH[2]}"
      class_name="${packages//\//.}.${file_name%.java}"
    fi

    echo "Class Fully Qualified Name"
    echo "$class_name"

    echo "COMMIT BLOCK:"
    echo "$commit_block"

    # Extract the diff hunk information
    while IFS= read -r line; do
        if [[ $line =~ ^@@\ -([0-9]+) ]]; then
            line_number="${BASH_REMATCH[1]}"
            # Add the line number (without the minus sign) to the array
            line_numbers+=("$line_number")
        fi
    done <<< "$commit_block"

    # Print the line numbers where the changes occurred
    echo "Line numbers with changes in $class_name: ${line_numbers[*]}"

    # Reset the line_numbers array for the next block
    line_numbers=()
  fi

done
