#!/bin/bash

echo "Building Resource Access Control System..."

mkdir -p out

find src -name "*.kt" | xargs kotlinc -cp "lib/*" -d out

jar cfm app.jar MANIFEST.MF -C out .

echo "Build completed: app.jar"