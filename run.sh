#!/bin/bash

if [ ! -f "app.jar" ]; then
    echo "Project not built. Running build.sh first..."
    ./build.sh
fi

echo "Running Resource Access Control System..."
java -jar app.jar "$@"