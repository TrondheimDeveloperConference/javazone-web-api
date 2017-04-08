FROM java:8
EXPOSE 8083
EXPOSE 9003
ADD api/ /
ADD configuration.yaml /root/
ENV APP_HOME /root
ENV CONSTRETTO_TAGS prod
CMD java ${JAVA_OPTS} -jar /root/bin/javazone-web-api-1.0-TDC.jar server /root/configuration.yaml
