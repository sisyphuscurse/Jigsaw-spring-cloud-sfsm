FROM java:8
VOLUME /tmp

ADD build/libs/api-gateway-*.jar /work/app.jar
ADD run.sh /

ENTRYPOINT ["/run.sh"]
