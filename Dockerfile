FROM maven:3.6.3 AS builder
COPY . /usr/local/src/sort-script
WORKDIR /usr/local/src/sort-script
RUN mvn clean package -DskipTests

FROM elasticsearch:5.4.3
RUN mkdir -p /usr/share/elasticsearch/plugins/sort-script/
COPY --from=builder /usr/local/src/sort-script/target/sort-script-1.0.jar /usr/share/elasticsearch/plugins/sort-script/
COPY --from=builder /usr/local/src/sort-script/resources/plugin-descriptor.properties /usr/share/elasticsearch/plugins/sort-script/
RUN chmod a+r -R /usr/share/elasticsearch/plugins/sort-script/
