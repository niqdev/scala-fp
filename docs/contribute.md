---
id: contribute
title: How to contribute
sidebar_label: How to
---

This website is built using

* [mdoc](https://scalameta.org/mdoc)
* [Docusaurus](https://docusaurus.io)

## Setup

* [Node.js](https://nodejs.org)
* [nvm](https://github.com/nvm-sh/nvm)
* [Yarn](https://classic.yarnpkg.com/lang/en)

Install Node.js using nvm

```bash
# install or update nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.37.2/install.sh | bash

# install node + npm (LTS)
nvm install 14 --lts

# use default version (see .nvmrc)
cd website/
nvm use

# verify installation
node --version
npm --version
```

Install Yarn classic

```bash
### on Debian / Ubuntu

# configure the repository
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list

# install yarn (with nvm)
sudo apt update && sudo apt install --no-install-recommends yarn

# verify installation
yarn --version

### on macOS

# install yarn
brew install yarn
```

First time only

```bash
# generate skeleton (delete docker files)
npx docusaurus-init
```

## Development

Edit documentation

```bash
# generate the documentation site
sbt clean mdoc

# file watcher with livereload (shell #1)
# watch "docs/.*md" and generate valid markdown in "modules/docs/target/mdoc"
sbt "mdoc --watch"

# generate static site
sbt docusaurusCreateSite

# start local site with livereload (shell #2)
yarn --cwd website/ start

# upgrade all dependencies
yarn --cwd website/ upgrade --latest
```

Testing

```bash
# test all
sbt test -jvm-debug 5005

# specify test
sbt "test:testOnly *ShowSpec"
```

**sbt aliases**

* `checkFormat` checks format
* `format` formats sources
* `update` checks outdated dependencies
* `build` checks format and runs tests
* `publish` deploy static site on `gh-pages` branch

## Publish

* [Travis integration](https://scalameta.org/mdoc/docs/docusaurus.html#publish-to-github-pages-from-ci)

First time only

```bash
# generate ssh keys
ssh-keygen -t rsa -b 4096 -C "hello@mail.com" -N '' -f /tmp/travis-gh-pages

# add deploy key
# https://github.com/niqdev/scala-fp/settings/keys
# name: travis-gh-pages
# write access: yes
# copy public key (Ubuntu)
cat /tmp/travis-gh-pages.pub | xclip

# add environment variable
# https://travis-ci.org/niqdev/scala-fp/settings
# name: GITHUB_DEPLOY_KEY
# copy base64 encoded secret key (Ubuntu)
cat /tmp/travis-gh-pages | base64 -w0 | xclip
```

Every time that a tag starting with `v` is pushed, then a deployment is triggered on Travis

```bash
# publish using travis
git tag vX.Y.Z
git push origin --tags

# publish locally
sbt docusaurusPublishGhpages
```
