---
id: contribute
title: Contribute
sidebar_label: How to
---

* [mdoc](https://scalameta.org/mdoc)
* [Docusaurus](https://docusaurus.io/en)

## Development

> TODO

```bash
# nvm (node + npm)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.35.0/install.sh | bash
nvm install 12 --lts
nvm use --lts 12
node --version
npm --version

# yarn (with nvm)
# https://yarnpkg.com/en/docs/install#debian-stable
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
sudo apt update && sudo apt install --no-install-recommends yarn
yarn --version

# docusaurus
# https://docusaurus.io
# generate skeleton (delete docker files)
npx docusaurus-init
# start local site with livereload
yarn --cwd website/ start
```

> TODO

```bash
# generate the documentation site
sbt clean mdoc

# file watcher with livereload
sbt "mdoc --watch"

# generate static site
sbt docusaurusCreateSite

# publish gh-pages locally
sbt docusaurusPublishGhpages
```
