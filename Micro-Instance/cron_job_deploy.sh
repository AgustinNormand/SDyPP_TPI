#!/bin/bash

#Must be logged in github to commit deploy changes
rm deploy.log

cd ..

git pull

cd Terraform/

./deploy.sh