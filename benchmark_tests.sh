#!/bin/bash

# Leggi gli hash dei due commit più recenti utilizzando git log
commit_corrente=$(git log --format="%H" -n 1)
commit_precedente=$(git log --format="%H" -n 2 | tail -n 1)

# Esegui la diff tra i due commit
git_diff=$(git diff $commit_precedente $commit_corrente)

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
deleted_methods=()
added_methods=()

# Print the commit_blocks
for commit_block in "${commit_blocks[@]}"; do
  # Extract the first line of the string
  first_line=$(echo "$commit_block" | head -n 1)
  commit_block_copy="$commit_block"

  # Check if the first line matches the pattern "diff --git path_1 path_2"
  if [[ $first_line =~ ^diff\ --git\ .*\main/java\/(.*)\/([^\/]+)\.java\ .*$ ]]; then
    packages="${BASH_REMATCH[1]}"
    file_name="${BASH_REMATCH[2]}"

    # Replace slashes with dots and remove .java extension
    class_name="${packages//\//.}.${file_name%.java}"

    # For each line of the actual block (diff for a class), beginning with deleted methods
    while IFS= read -r line; do
      string=$(echo "$line" | head -n 1)
      if [[ $string =~ \-.*\ (static\ )?[a-zA-Z_][a-zA-Z0-9_]*\ ([a-zA-Z_][a-zA-Z0-9_]*)\( ]]; then
          method_name=${BASH_REMATCH[2]}
          deleted_methods+=("$class_name.$method_name")
      fi
    done <<< "$commit_block"

    echo "ciaooooooooooooooooooooooooooooooooooooooo"
    echo "$commit_block_copy"

    # For each line of the actual block (diff for a class), ending with added methods
    while IFS= read -r line; do
      string=$(echo "$line" | head -n 1)
      if [[ $string =~ \+.*\ (static\ )?[a-zA-Z_][a-zA-Z0-9_]*\ ([a-zA-Z_][a-zA-Z0-9_]*)\( ]]; then
          method_name=${BASH_REMATCH[2]}
          #if the method is already in deleted methods, it means it has just been modified
          if [[ " ${deleted_methods[*]} " =~  $class_name.$method_name  ]]; then
            # Rimuovi la stringa dalla lista
            deleted_methods=( "${deleted_methods[@]/$class_name.$method_name}" )
          fi
          added_methods_methods+=("$class_name.$method_name")
      fi
    done <<< "$commit_block_copy"
  fi

done

for deleted_method in "${deleted_methods[@]}"; do
  echo "deleted: "
  echo "$deleted_method"
done

for added_method in "${added_methods[@]}"; do
  echo "added: "
  echo "$added_method"
done
