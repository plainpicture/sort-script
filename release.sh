#!/bin/sh

mvn -Dmaven.test.skip=true clean package
mkdir -p release/sort-script
cp resources/plugin-descriptor.properties release/sort-script
cp target/sort-script-1.0.jar release/sort-script
tar -c release/sort-script > release/sort-script.tar
gzip release/sort-script.tar
