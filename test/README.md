# Coursework 3 Unit Tests
JUnit is the only required dependency to run the unit tests.

## JUnit
A framework to write repeatable tests.

[JUnit](<http://junit.org>)

## Run script
An example of a script to run all tests.

Run all tests:

```sh
$ ./run.sh
```

Run one particular test:

```sh
$ ./run.sh <TEST NAME>
```

Script:

```sh
#!/bin/bash

JUNIT_JAR="junit-4.12.jar"
HAMCREST_JAR="hamcrest-core-1.3.jar"

# compile and run the test(s)
function run_test {
    filename=$1
    basename=`basename $filename .java`

    echo "Running $basename..."

    # compile
    javac -cp .:..:$JUNIT_JAR $filename

    # run
    java -cp .:..:$JUNIT_JAR:$HAMCREST_JAR org.junit.runner.JUnitCore $basename
}

if [ -n "$1" ]; then
    # run a single test
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
```

