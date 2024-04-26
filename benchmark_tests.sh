#!/bin/bash

chmod +x "$0"
mkdir -p ./ciao
# Leggi gli hash dei due commit più recenti utilizzando git log
commit_corrente=$(git log --format="%H" -n 1)
commit_precedente=$(git log --format="%H" -n 2 | tail -n 1)

# Esegui la diff tra i due commit
git_diff=$(git diff $commit_precedente $commit_corrente)

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

    #echo "COMMIT BLOCK:"
    #echo "$commit_block"

    # For each line of the actual block (diff for a class), beginning with deleted methods
    while IFS= read -r line; do
      string=$(echo "$line" | head -n 1)
      if [[ $string =~ \-.*\ (static\ )?[a-zA-Z_][a-zA-Z0-9_]*[[:space:]]*\ ([a-zA-Z_][a-zA-Z0-9_]*)[[:space:]]*\( ]]; then
        #echo "Line:"
        #echo "$string"
        method_name=${BASH_REMATCH[2]}
        #echo "method name:"
        #echo "$method_name"
        deleted_methods+=("$class_name.$method_name")
      fi
    done <<< "$commit_block"

    #echo "======================================================================0"
    #echo "COMMIT BLOCK COPY:"
    #echo "$commit_block_copy"

    # For each line of the actual block (diff for a class), ending with added methods
    while IFS= read -r line; do
      string=$(echo "$line" | head -n 1)
      if [[ $string =~ \+.*\ (static\ )?[a-zA-Z_][a-zA-Z0-9_]*[[:space:]]*\ ([a-zA-Z_][a-zA-Z0-9_]*)[[:space:]]*\( ]]; then
        #echo "Line:"
        #echo "$string"
        method_name=${BASH_REMATCH[2]}
        #echo "method name:"
        #echo "$method_name"
        #if the method is already in deleted methods, it means it has just been modified
        if [[ " ${deleted_methods[*]} " =~  $class_name.$method_name  ]]; then
          #echo "this method is already in deleted list"
          # Rimuovi la stringa dalla lista
          deleted_methods=( "${deleted_methods[@]/$class_name.$method_name}" )
        fi
        added_methods+=("$class_name.$method_name")
      fi
    done <<< "$commit_block_copy"

    #echo "=============================================================="
  fi

done

# Run the JAR file and capture the output
ju2jmh_listing_output=$(java -jar ./ju2jmh-jmh.jar -l)

# make the list of existing benchmarks
existing_benchmarks=()

while IFS= read -r line; do
  benchmark=$(echo "$line" | head -n 1)
  if [[ ! $benchmark =~ ^Benchmarks: ]]; then
    existing_benchmarks+=("$benchmark")
    echo "$benchmark"
  fi
done <<< "$ju2jmh_listing_output"

# Print each element of the list
#for existing_benchmark in "${existing_benchmarks[@]}"; do
#  echo "benchmark:"
#  echo "$existing_benchmark"
#done

# Function to transform method names
transform_method() {
  local method=$1
  local packages=$(echo $method | cut -d'.' -f1-$(( $(echo $method | tr -cd '.' | wc -c) - 1 )))
  local method_name=$(echo $method | rev | cut -d'.' -f1 | rev)
  local class=$(echo $method | rev | cut -d'.' -f2 | rev)

  # Capitalize the method name
  local capitalized_method_name="$(echo "$method_name" | sed 's/^\(.\)/\U\1/')"

  # Add class name and prefix
  local transformed_method="$packages.$class"Test"._Benchmark.benchmark_test$capitalized_method_name"

  echo $transformed_method
}

benchmark_classes_to_generate=()
pattern="^([a-z.]*)([A-Z][a-zA-Z]*)Test\b(.*)"

for deleted_method in "${deleted_methods[@]}"; do
  transformed_method=$(transform_method "$deleted_method")
  #echo "deleted: "
  #echo "$deleted_method -> $transformed_method"
  # Iterate through the list of existing benchmarks
  for existing_benchmark in "${existing_benchmarks[@]}"; do
    if [[ "$transformed_method" == "$existing_benchmark" ]]; then
      if [[ $existing_benchmark =~ $pattern ]]; then
        benchmark_class_to_generate="${BASH_REMATCH[1]}${BASH_REMATCH[2]}"
        #echo "benchmark_class_to_generate:"
        #echo "$benchmark_class_to_generate"
        found=false
        for element in "${benchmark_classes_to_generate[@]}"; do
          if [[ "$element" == "$benchmark_class_to_generate" ]]; then
            found=true
            break
          fi
        done
        if [ "$found" = true ]; then
          #echo "la classe era già in lista"
          echo ""
        else
          benchmark_classes_to_generate+=("$benchmark_class_to_generate")
        fi
      fi
    fi
  done
done

for added_method in "${added_methods[@]}"; do
  transformed_method=$(transform_method "$added_method")
  #echo "added: "
  #echo "$added_method -> $transformed_method"
  found=false
  for existing_benchmark in "${existing_benchmarks[@]}"; do
    if [[ "$transformed_method" == "$existing_benchmark" ]]; then
      found=true
    fi
  done
  if [ "$found" = true ]; then
    #echo "il benchmark per il metodo aggiunto già esiste"
    echo ""
  else
    if [[ $transformed_method =~ $pattern ]]; then
      benchmark_class_to_generate="${BASH_REMATCH[1]}${BASH_REMATCH[2]}"
      #echo "benchmark_class_to_generate:"
      #echo "$benchmark_class_to_generate"
      found2=false
      for element in "${benchmark_classes_to_generate[@]}"; do
        if [[ "$element" == "$benchmark_class_to_generate" ]]; then
          found2=true
          break
        fi
      done
      if [ "$found2" = true ]; then
        #echo "la classe era già in lista"
        echo ""
      else
        benchmark_classes_to_generate+=("$benchmark_class_to_generate")
      fi
    fi
  fi
done

# Define an associative array to keep track of unique strings
declare -A seen

# Define the new list without duplicates
definitive_benchmark_classes_to_generate=()

# Iterate through the original list
for benchmark_class_to_generate in "${benchmark_classes_to_generate[@]}"; do
  # Check if the string has been seen before
  if [[ -z "${seen[$benchmark_class_to_generate]}" ]]; then
    # If not seen before, add it to the new list and mark it as seen
    definitive_benchmark_classes_to_generate+=("$benchmark_class_to_generate")
    seen["$benchmark_class_to_generate"]=1
  fi
done

pattern="^([a-z.]*)([A-Z][a-zA-Z]*)"

for benchmark_class_to_generate in "${definitive_benchmark_classes_to_generate[@]}"; do
  echo "benchmark class to generate:"
  echo "$benchmark_class_to_generate"

  if [[ $benchmark_class_to_generate =~ $pattern ]]; then
    benchmark_class_path="${BASH_REMATCH[1]}"
    benchmark_class_path="${benchmark_class_path//./\/}"
    benchmark_class_file_name="${BASH_REMATCH[2]}Test.java"
    benchmark_class_file_new_name="${benchmark_class_file_name/Test.java/TestPrev.java}"

    source_file="./ju2jmh/src/jmh/java/${benchmark_class_path}${benchmark_class_file_name}"
    destination_file="./ju2jmh/src/jmh/java/${benchmark_class_path}${benchmark_class_file_new_name}"

    echo "source file:"
    echo "$source_file"
    echo "destination_file:"
    echo "$destination_file"

    # Check if the source file exists
    if [ -f "$source_file" ]; then
      mv "$source_file" "$destination_file"
      echo "File renamed successfully."
    else
      echo "File does not exist: $source_file"
    fi

  fi

done


