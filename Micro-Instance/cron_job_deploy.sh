#!/bin/bash

#Must be logged in github to commit deploy changes

./cat-slack.sh "/tmp/deploy.log" $WEBHOOK

cd ..

git pull

cd Terraform/

./deploy.sh

rm /tmp/deploy.log
