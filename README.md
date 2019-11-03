# scala-fp

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

---

> OLD

```bash
sbt "show discoveredMainClasses"

# run
sbt "runMain com.github.niqdev.MainIO"
sbt "runMain com.github.niqdev.MainState"
sbt "runMain com.github.niqdev.MainStateT"
sbt "runMain com.github.niqdev.MainStateTLoop"

# serve static docs
./docs-local.sh
```
