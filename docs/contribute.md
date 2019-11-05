---
id: contribute
title: How to contribute
sidebar_label: How to
---

This website is built using

* [mdoc](https://scalameta.org/mdoc)
* [Docusaurus](https://docusaurus.io)

## Setup

Install [Node.js](https://nodejs.org) using [`nvm`](https://github.com/nvm-sh/nvm)

```bash
# install or update nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.35.0/install.sh | bash

# install node + npm (LTS)
nvm install 12 --lts

# use default version (see .nvmrc)
nvm use

# verify installation
node --version
npm --version
```

Install [Yarn](https://yarnpkg.com)

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

## Edit

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
```

## Publish

> TODO

* [travis integration](https://scalameta.org/mdoc/docs/docusaurus.html#publish-to-github-pages-from-ci)

```bash
# publish gh-pages locally
sbt docusaurusPublishGhpages
```
