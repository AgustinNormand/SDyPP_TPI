#!/bin/bash

#Must be logged in github to commit deploy changes
cd ..

git pull

cd Terraform/

./deploy.sh

./cat-slack.sh "/tmp/deploy.log"

rm /tmp/deploy.log