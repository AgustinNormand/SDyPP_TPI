#!/bin/bash

cd ..

git pull

cd Terraform/

./destroy.sh

../Micro-Instance/cat-slack.sh "/tmp/destroy.log" $WEBHOOK

rm /tmp/destroy.log

