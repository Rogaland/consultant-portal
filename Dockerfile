FROM java:8
LABEL authors="Frode Sjovatsen <frode.sjovatsen@rogfk.no>"

ADD guest-user-portal-backend/build/libs/guest-user-portal-backend-*.jar /data/app.jar

CMD java ${PARAMS} -jar /data/app.jar