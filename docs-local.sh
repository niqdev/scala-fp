#!/bin/bash

#apt-get install jekyll
#gem install jekyll

rm -fr _site/

sbt clean makeMicrosite

# --livereload
jekyll serve --source target/site/
