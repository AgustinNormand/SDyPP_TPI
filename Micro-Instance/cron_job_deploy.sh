#!/bin/bash

#Must be logged in github to commit deploy changes

cd ..

git pull

rm deploy.log

cd Terraform/

./deploy.sh