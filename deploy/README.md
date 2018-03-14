# ISD Pipeline Development Environment Setup

## Requirements

Only requirement is Docker with Docker Compose. Too install docker-compose, see instructions in here
https://docs.docker.com/compose/install/#install-compose

## Containers
We use one custom base image for all container which is called dmlablib. To build this image
use following command:

```
cd deploy/dmlablib
docker build -t dmlablib --no-cache .
```
 
After building dmlablib image, you can run all containers:
```
 cd deploy
 docker-compose up
```

Note that, we are using rabbitmq, postgresql and mysql images which will be downloaded from web. 
So first time will take time. 

## Exposed links

There are two exposed url that you can monitor the system. 

First one is rabbitmq management web:

http://rabbitmq-ip:15672
 
To find rabbitmq container ip, run `docker inspect deploy_rabbitmq_1` and use listed "IPAddress"

Second one is our monitoring API. Monitoring API runs in TaskScheduler container and exposed in port 1278
. run `docker inspect deploy_taskscheduler_1` and use listed "IPAddress" and go to http://taskscheduler-ip:1278

## Useful Docker commands

List running containers: `docker ps`. In this version, you should see those containers: deploy_taskscheduler_1, deploy_hekpuller_1, 
deploy_imagepuller_1, deploy_rabbitmq_1, deploy_mysql_1, deploy_postgresql_1 

Stop all containers (might be needed sometimes): `docker stop $(docker ps -qa)`

Remove all containers (might be needed sometimes): `docker rm -f $(docker ps -qa)`

Get into a container: `docker exec -it container_name /bin/bash`

 
 
 
  
 