FROM maven AS builder
RUN git clone -b 5.4.3 https://github.com/plainpicture/sort-script.git /root/sort-script
WORKDIR /root/sort-script
RUN mvn -Dmaven.test.skip=true clean package

FROM elasticsearch:5.4.3
RUN mkdir -p /usr/share/elasticsearch/plugins/sort-script/
COPY --from=builder /root/sort-script/target/sort-script-1.0.jar /usr/share/elasticsearch/plugins/sort-script/
COPY --from=builder /root/sort-script/resources/plugin-descriptor.properties /usr/share/elasticsearch/plugins/sort-script/
