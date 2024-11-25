#!/bin/bash

# Configure git
git config --global user.email "atrovato@unisa.it"
git config --global user.name "AntonioTrovato"

#TODO: CREA DELLE VARIABILI ALL'INIZIO DI QUESTO .SH IMPOSTABILI PER GENERALIZZARE L'USO DI QUESTO WORKFLOW

#TODO: JU2JMH SVUOTA LE CLASSI DI BENCHMARK NON
#TODO: INTERESSATE DAL COMMIT (SE UNA CLASSE
#TODO: DI BENCHMARK C'ERA LA SVUOTA, SE NON C'ERA
#TODO: LA CREA (OSSIA CI DEVE ESSERE LA CLASSE DI
#TODO: TEST DI UNITA') SENZA BENCHMARK

#TODO: LE CLASSI INTERNE SONO NELLA COVERAGE MATRIX COME CLASSE$CLASSE_INTERNA

#TODO: RICORDA DI AFFINARE IL CONTROLLO DELLA DIFFERENZA NEL BODY

#TODO: RICORDA DI AFFINARE LE ESCLUDE DELLO JACOCO AGENT

# Read the hashes of the last two commits using git log
current_commit=$(git log --format="%H" -n 1)
previous_commit=$(git log --format="%H" -n 2 | tail -n 1)

# Make diff between the two commits
git_diff=$(git diff $previous_commit $current_commit)

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
added_methods=()

# Print the commit_blocks
for commit_block in "${commit_blocks[@]}"; do
  # Extract the first line of the string
  first_line=$(echo "$commit_block" | head -n 1)

  # Check if the first line matches the pattern "diff --git path_1 path_2"
  if [[ $first_line =~ ^diff\ --git\ .*\main/java\/(.*)\/([^\/]+)\.java\ .*$ ]]; then
    packages="${BASH_REMATCH[1]}"
    file_name="${BASH_REMATCH[2]}"

    # Replace slashes with dots and remove .java extension
    class_name="${packages//\//.}.${file_name%.java}"

    echo "COMMIT BLOCK:"
    echo "$commit_block"

    # Scan the current commit block for method signatures
    while IFS= read -r line; do
        # Check for method signature
        if [[ $line =~ .*\ (static\ )?[a-zA-Z_][a-zA-Z0-9_]*[[:space:]]*\ ([a-zA-Z_][a-zA-Z0-9_]*)[[:space:]]*\( ]]; then
            method_name="${BASH_REMATCH[2]}"
            current_method="$method_name"

            # Check if the line starts with + or -
            if [[ $line == +* || $line == -* ]]; then
                # Check if the method is not already in added_methods
                if [[ ! " ${added_methods[*]} " =~  $class_name.$method_name ]]; then
                    added_methods+=("$class_name.$method_name")
                fi
            fi
        else
            # If current_method is set, check subsequent lines for changes
            if [[ -n $current_method ]]; then
                # Check the next lines for additions or deletions
                if [[ $line == +* || $line == -* ]]; then
                    # Check if the method is not already in added_methods
                    if [[ ! " ${added_methods[*]} " =~  $class_name.$method_name ]]; then
                        added_methods+=("$class_name.$current_method")
                    fi
                fi
            fi
        fi
    done <<< "$commit_block"

  fi

done
