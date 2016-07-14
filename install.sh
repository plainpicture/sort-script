#!/bin/sh

mvn -Dmaven.test.skip=true clean package || exit 1

mkdir -p /usr/local/elasticsearch/plugins/sort-script

cp resources/plugin-descriptor.properties /usr/local/elasticsearch/plugins/sort-script
cp target/sort-script-*.jar /usr/local/elasticsearch/plugins/sort-script

chown -R elasticsearch:elasticsearch /usr/local/elasticsearch/plugins/sort-script

