#!/bin/bash

# Install dependencies with legacy peer deps flag
npm install --legacy-peer-deps

# Build the application with CI=false to ignore warnings
CI=false npm run build