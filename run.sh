#!/bin/bash

if [ ! -f "app.jar" ]; then
    echo "Project not built. Running build.sh first..."
    ./build.sh
fi

echo "Running Resource Access Control System..."
java -jar app.jar "$@"

echo "Running tests..."

kotlinc -cp "./lib/*:./out" \
        -d out \
        src/test/kotlin/*.kt \
        src/test/kotlin/**/*.kt

java -jar ./lib/junit-platform-console-standalone-1.12.2.jar \
     --class-path "./lib/*:./out" \
     --scan-class-path \
     --include-package=".*test.*"

echo "Tests execution completed!"
