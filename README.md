# ServerArchitect [![CircleCI](https://circleci.com/gh/Exorath/ServerArchitect.svg?style=svg)](https://circleci.com/gh/Exorath/ServerArchitect) 
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

