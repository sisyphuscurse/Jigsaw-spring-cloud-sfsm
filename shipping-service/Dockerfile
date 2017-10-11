FROM java:8
VOLUME /tmp

ADD build/libs/shipping-service-*.jar /work/app.jar
ADD run.sh /

ENTRYPOINT ["/run.sh"]
