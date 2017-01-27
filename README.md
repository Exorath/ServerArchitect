# ServerArchitect [![Build Status](https://travis-ci.org/Exorath/ServerArchitect.svg?branch=master)](https://travis-ci.org/Exorath/ServerArchitect)  [![Docker Automated build](https://img.shields.io/docker/automated/exorath/serverarchitect.svg)](https://hub.docker.com/r/exorath/serverarchitect/)
The ServerArchitect consumes a template file and produces a ready-to-operate spigot/bungee server!


##config

```yaml
loaders:
  github:
    plugins:
      token: <oathToken>
      connector:
        name: ConnectorPlugin
        jar: ConnectorPlugin-*.jar
  mapservice:
    address: <http address>
    maps:
      env: prod
      prefix: hubs.lobby
  amazons3:
    jar:
      bucketName: exospigotjars
      objectId: spigot.jar
      accessKeyId:
        env: AWS_ACCESS_KEY_ID
      secretKey:
        env: AWS_SECRET_KEY
      region:
        env: AWS_REGION
```

