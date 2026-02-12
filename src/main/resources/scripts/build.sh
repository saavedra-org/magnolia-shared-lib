#!/bin/bash

# Script to clean and build the Maven project

# Exit immediately if a command exits with a non-zero status
set -e

# Navigate to the project directory
cd $(dirname $0)/../..

# Clean and build the project
mvn clean install

# Print success message
echo "Project cleaned and built successfully."