#!/bin/bash

# Example Shell Script to Deploy Artifacts to Nexus

# Set variables
NEXUS_URL="http://nexus.example.com"
REPO="your-repo-name"
ARTIFACT_PATH="path/to/your/artifact.jar"

# Deploy to Nexus
curl -v --user user:password --file "${ARTIFACT_PATH}" "${NEXUS_URL}/repository/${REPO}/"

# Check if the deployment was successful
if [ $? -eq 0 ]; then
    echo "Artifact deployed successfully!"
else
    echo "Failed to deploy artifact."
    exit 1
}