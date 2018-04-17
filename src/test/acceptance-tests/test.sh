#!/bin/bash

sleep 60
statusUp="UP"

#resp="{\"status\":\"UP\"}"
resp=$(curl movie-finder:9090/actuator/health) || ''

echo "Response:" ${resp}

if [[ ${resp} =~ "${statusUp}" ]]; then
    echo "Matching"
    exit 0
else
    echo "NOT Matching"
    exit 1
fi
