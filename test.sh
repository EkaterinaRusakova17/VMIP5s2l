#!/bin/bash

run_test() {
    local description="$1"
    local expected_code="$2"
    local command="$3"

    echo -n "Testing $description: "

    $command
    local actual_code=$?

    if [ $actual_code -eq $expected_code ]; then
        echo "OK"
        return 1
    else
        echo "FAIL (expected $expected_code, got $actual_code)"
        return 0
    fi
}

# Собираем проект если нужно
if [ ! -f "app.jar" ]; then
    echo "Building project..."
    ./build.sh > /dev/null 2>&1
fi

echo "Running tests..."

passed=0
total=0

# Тест 1: Справка
run_test "help flag" 1 "java -jar app.jar -h"
((passed += $?))
((total++))

# Тест 2: Неверный логин
run_test "invalid login" 3 "java -jar app.jar --login unknown --password pass --resource A.B --action read --volume 10"
((passed += $?))
((total++))

# Тест 3: Неверный пароль
run_test "invalid password" 2 "java -jar app.jar --login alice --password wrong --resource A.B --action read --volume 10"
((passed += $?))
((total++))

# Тест 4: Неизвестное действие
run_test "unknown action" 4 "java -jar app.jar --login alice --password qwerty --resource A.B --action unknown --volume 10"
((passed += $?))
((total++))

# Тест 5: Нет доступа
run_test "access denied" 5 "java -jar app.jar --login bob --password password --resource A.B.C --action write --volume 10"
((passed += $?))
((total++))

# Тест 6: Несуществующий ресурс
run_test "resource not found" 6 "java -jar app.jar --login alice --password qwerty --resource X.Y.Z --action read --volume 10"
((passed += $?))
((total++))

# Тест 7: Некорректный формат ресурса
run_test "invalid resource format" 7 "java -jar app.jar --login alice --password qwerty --resource 'A.B.' --action read --volume 10"
((passed += $?))
((total++))

# Тест 8: Превышение объема
run_test "volume exceeded" 8 "java -jar app.jar --login alice --password qwerty --resource A.B --action read --volume 100"
((passed += $?))
((total++))

# Тест 9: Некорректный объем
run_test "invalid volume" 7 "java -jar app.jar --login alice --password qwerty --resource A.B --action read --volume -5"
((passed += $?))
((total++))

# Тест 10: Успешный доступ
run_test "successful access" 0 "java -jar app.jar --login alice --password qwerty --resource A.B --action read --volume 10"
((passed += $?))
((total++))

echo "Tests passed: $passed/$total"

if [ $passed -eq $total ]; then
    exit 0
else
    exit 1
