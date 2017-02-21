FROM java:8
LABEL authors="Frode Sjovatsen <frode.sjovatsen@rogfk.no>"

RUN pwd
RUN ls -lag
ADD consultant-portal-backend/build/libs/consultant-portal-backend-*.jar /data/app.jar

CMD java ${PARAMS} -jar /data/app.jar