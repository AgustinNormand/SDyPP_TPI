#!/bin/bash

#Must be logged in github to commit deploy changes
cd ..

git pull

cd Terraform/

./deploy.sh