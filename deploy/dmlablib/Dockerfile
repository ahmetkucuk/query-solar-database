FROM openjdk:8-jdk

ARG MAVEN_VERSION=3.5.3
ARG USER_HOME_DIR="/root"
ARG SHA=b52956373fab1dd4277926507ab189fb797b3bc51a2a267a193c931fffad8408
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha256sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

VOLUME "$USER_HOME_DIR/.m2"

# Update aptitude with new repo
RUN apt-get update

# Install software 
RUN apt-get install -y git

# Clone the conf files into the docker container
RUN git clone https://ahmetkucuk92:McFqmrpJbzKApmyEBSwF@bitbucket.org/gsudmlab/dmlablib.git
RUN cd dmlablib/ && \
    git checkout mq && \
    mvn clean install && \
    mvn install:install-file -Dfile=target/lib-0.0.1-jar-with-dependencies.jar -DgroupId=edu.gsu.dmlab -DartifactId=lib -Dversion=0.0.1 -Dpackaging=jar && \
    cd .. && \ 
	git clone https://github.com/ahmetkucuk/query-solar-database.git && \ 
	cd query-solar-database/ && \
	mvn clean compile assembly:single