FROM openjdk:17

WORKDIR /usrapp/bin

ENV PORT 30000

COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency

CMD ["java", "-cp", "./classes:./dependency/*","co.edu.arep.RestServiceApplication"]