# AuthlibProxyForServer

[![License](https://img.shields.io/static/v1?label=License&message=gpl-v3.0&color=red)](http://www.gnu.org/licenses/gpl-3.0.txt)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/authlibproxyforserver?label=Modrinth%20Downloads)](https://modrinth.com/mod/authlibproxyforserver)
[![CurseForge Downloads](https://cf.way2muchnoise.eu/full_authlibproxyforserver_CurseForge%20Downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)
[![GitHub Downloads](https://img.shields.io/github/downloads/AyakaCraft/AuthlibProxyForServer/total?label=Github%20Downloads&logo=github)](https://github.com/AyakaCraft/AuthlibProxyForServer/releases)
[![MC Versions](https://cf.way2muchnoise.eu/versions/MC%20Version_authlibproxyforserver_all.svg)](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)
[![Build & Publish](https://github.com/AyakaCraft/AuthlibProxyForServer/actions/workflows/release.yml/badge.svg)](https://github.com/AyakaCraft/AuthlibProxyForServer/actions/workflows/release.yml)
[![Release](https://img.shields.io/github/v/release/AyakaCraft/AuthlibProxyForServer?label=Release&include_prereleases)](https://github.com/AyakaCraft/AuthlibProxyForServer/releases)

[简体中文](README_zh.md) ｜ English

Copyright (c) 2025  Calboot and contributors

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>

## Conclusion

Authlib proxy for server.

Mostly based on [Fallen-Breath's template](https://github.com/Fallen-Breath/fabric-mod-template)

## Links

- [Github](https://github.com/AyakaCraft/AuthlibProxyForServer)
- [Discord](https://discord.gg/r2WyWrx5vw)
- [Modrinth](https://modrinth.com/project/authlibproxyforserver)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)

## Usage

- Install this mod to your server (dedicated only)
- Start the server once
- Edit `$SERVER_ROOT$/config/authlib.proxy.json`, it should look like below
```json5
{
  "enabled": false, // set to true to enable
  "type": "HTTP", // or DIRECT, SOCKS
  "host": "127.0.0.1", // either IP or host name
  "port": 7897 // [0,65536)
}
```
