#!/bin/bash

echo "Tail destroy executed"

touch /tmp/destroy.log

./tail-slack.sh "/tmp/destroy.log" $(cat webhook.txt)

