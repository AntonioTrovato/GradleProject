#!/bin/bash

commit_hash=$(git rev-parse HEAD)

# Ottieni l'hash del commit precedente
parent_commit_hash=$(git rev-parse HEAD^)

# Esegui la diff tra i due commit
git diff $parent_commit_hash $commit_hash