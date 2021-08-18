#!/bin/bash

cat "$1" | while read LINE; do
  (echo "$LINE" | grep -e "$3") && curl -X POST --silent --data-urlencode \
    "payload={\"text\": \"$(echo $LINE | sed "s/\"/'/g")\"}" "https://hooks.slack.com/services/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";
done