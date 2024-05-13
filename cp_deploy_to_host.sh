#!/usr/bin/env bash
set -x
source .env
echo $backend_ip
 scp ./target/quarkus-app/* $backend_ip:~/soulstone/