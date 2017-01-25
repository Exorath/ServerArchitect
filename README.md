# ServerArchitect [![Docker Automated build](https://img.shields.io/docker/automated/exorath/serverarchitect.svg)](https://hub.docker.com/r/exorath/serverarchitect/)
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
    
```

