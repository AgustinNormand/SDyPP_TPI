#!/bin/bash

cd ..

git pull

cd Terraform/

./destroy.sh

./cat-slack.sh "/tmp/destroy.log"

rm /tmp/destroy.log
