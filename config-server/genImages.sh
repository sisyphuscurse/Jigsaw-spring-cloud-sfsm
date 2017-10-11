#!/usr/bin/env bash

docker build -t $DMALL_DOCKER_REGISTRY/dmall/config-server:$BUILD_NUMBER .
docker tag $DMALL_DOCKER_REGISTRY/dmall/config-server:$BUILD_NUMBER $DMALL_DOCKER_REGISTRY/dmall/config-server:latest

docker push $DMALL_DOCKER_REGISTRY/dmall/config-server:$BUILD_NUMBER
docker push $DMALL_DOCKER_REGISTRY/dmall/config-server:latest

