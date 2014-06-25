#!/bin/sh

mvn -Dmaven.test.skip=true clean package || exit 1

cp target/sort-script-*.jar /usr/local/elasticsearch/lib/
chown elasticsearch:elasticsearch /usr/local/elasticsearch/lib/sort-script-*.jar

