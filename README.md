# Flowlane

Flowlane is a community-maintained web user interface for the [Flowable](https://github.com/flowable/flowable-engine) process engine.
It contains the applications Modeler, Task, Admin and IDM.

Flowlane is a fork of the Flowable UI applications of Flowable 6.8.0 (module `modules/flowable-ui` of the tag `flowable-6.8.0` of [flowable/flowable-engine](https://github.com/flowable/flowable-engine/tree/flowable-6.8.0/modules/flowable-ui)).
Flowable discontinued these UI applications after version 6.8.0.
Flowlane is not affiliated with or endorsed by Flowable AG.
"Flowable" is used only to describe the origin of this project and the engine it works with.

## Status

Flowlane is in an early state.
It currently still embeds the Flowable engine libraries in version 6.8.0 (see the property `flowable.engine.version` in `pom.xml`).
The goal is that Flowlane only contains the user interface and uses an externally hosted Flowable engine via its REST API.

## Prerequisites

- A JDK (the build was tested with JDK 21; the original Flowable UI 6.8.0 supports JDK 8 or newer)
- Maven (or the included Maven wrapper `mvnw`)

## Build

```text
mvnw -B install
```

The runnable application is created in `flowlane-app/target`.

## Configuration

The settings of Flowlane itself use the prefixes `flowlane.common.app`, `flowlane.admin.app`, `flowlane.idm.app`, `flowlane.task.app` and `flowlane.modeler.app`.
Settings of the Flowable engine keep their original `flowable.` prefixes (for example `flowable.idm.ldap` or `flowable.mail.server`).

## License

Flowlane is distributed under the Apache License 2.0, see [LICENSE](LICENSE).
It is based on source code of the Flowable project, which is distributed under the same license.
The source code of Flowable was modified for Flowlane, for example by renaming packages, artifacts, configuration keys and user interface texts.
The origin and the modifications are listed in [NOTICE](NOTICE).
