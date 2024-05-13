#!/usr/bin/env bash
set -x
source .env

scp -r ./target/quarkus-app/* $backend_name:~/soulstone/