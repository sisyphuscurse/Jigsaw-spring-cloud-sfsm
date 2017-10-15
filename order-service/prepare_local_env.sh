#!/bin/bash
docker-compose -f docker-compose-local.yaml down \
&&  docker-compose -f docker-compose-local.yaml up
