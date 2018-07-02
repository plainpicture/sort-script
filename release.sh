#!/bin/sh

mvn -Dmaven.test.skip=true clean package
rm -R release
mkdir -p release/sort-script
cp resources/plugin-descriptor.properties release/sort-script
cp target/sort-script-1.0.jar release/sort-script
(cd release && tar -c sort-script > sort-script.tar)
gzip release/sort-script.tar
