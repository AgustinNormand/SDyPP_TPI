#!/bin/bash

tail -n0 -F "$1" | while read LINE; do
  PAYLOAD=$(echo $LINE | sed "s/\"/'/g" | sed "s/\[//g" | sed "s/\]//g")
  #echo $PAYLOAD
  curl -X POST --silent --data-urlencode "payload={\"text\": \"$PAYLOAD\"}" "$2";
done
