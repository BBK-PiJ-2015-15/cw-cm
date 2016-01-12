#!/bin/bash
# simple script to run the JUnit tests

JUNIT_JAR="junit-4.12.jar"
HAMCREST_JAR="hamcrest-core-1.3.jar"
MOCKITO_JAR="mockito-all-1.10.19.jar"

# compile an run the test(s)
function run_test {
    filename=$1
    basename=`basename $filename .java`

    echo "Running $basename..."

    # compile
    javac -cp .:..:$JUNIT_JAR:$MOCKITO_JAR $filename

    # run
    java -cp .:..:$JUNIT_JAR:$MOCKITO_JAR:$HAMCREST_JAR org.junit.runner.JUnitCore $basename
}

if [ -n "$1" ]; then
    # run a single, user selected, test
    filename="$1.java"
    if [ -f "$filename" ]; then
        run_test $filename
    else
        echo "Error: test file doesn't exist."
        exit 1
    fi
else
    # run all tests
    for filename in *Test.java; do
        run_test $filename
    done
fi
