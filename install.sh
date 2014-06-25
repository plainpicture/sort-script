#!/bin/sh

mvn -Dmaven.test.skip=true clean package dependency:copy-dependencies || exit 1

cp target/sort-script*.jar /usr/local/elasticsearch/lib/
cp -n target/dependency/*.jar /usr/local/elasticsearch/lib/

