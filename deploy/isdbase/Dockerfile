FROM dmlablib:latest

RUN cd dmlablib/ && \ 
	mvn install:install-file -Dfile=target/lib-0.0.1-jar-with-dependencies.jar -DgroupId=edu.gsu.dmlab -DartifactId=lib -Dversion=0.0.1 -Dpackaging=jar && \ 
	cd .. && \ 
	git clone https://github.com/ahmetkucuk/query-solar-database.git && \ 
	cd query-solar-database/ && \
	git checkout test && \
	mvn clean compile assembly:single
ENTRYPOINT ["java", "-jar", "/query-solar-database/target/query-solar-database-1.0-SNAPSHOT-jar-with-dependencies.jar"]