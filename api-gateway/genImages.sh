#!/usr/bin/env bash

docker build -t $DMALL_DOCKER_REGISTRY/dmall/api-gateway:$BUILD_NUMBER .
docker tag $DMALL_DOCKER_REGISTRY/dmall/api-gateway:$BUILD_NUMBER $DMALL_DOCKER_REGISTRY/dmall/api-gateway:latest

docker push $DMALL_DOCKER_REGISTRY/dmall/api-gateway:$BUILD_NUMBER
docker push $DMALL_DOCKER_REGISTRY/dmall/api-gateway:latest

