# AuthlibProxyForServer

[![License](https://img.shields.io/static/v1?label=License&message=gpl-v3.0&color=red&logo=gnu)](https://www.gnu.org/licenses/gpl-3.0.txt)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/authlibproxyforserver?label=Modrinth%20Downloads)](https://modrinth.com/mod/authlibproxyforserver)
[![GitHub Downloads](https://img.shields.io/github/downloads/AyakaCraft/AuthlibProxyForServer/total?label=Github%20Downloads&logo=github)](https://github.com/AyakaCraft/AuthlibProxyForServer/releases)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1282523?label=CurseForge%20Downloads&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)
[![MC Versions](https://cf.way2muchnoise.eu/versions/MC%20Version_authlibproxyforserver_all.svg)](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)
[![Build & Publish](https://github.com/AyakaCraft/AuthlibProxyForServer/actions/workflows/release.yml/badge.svg)](https://github.com/AyakaCraft/AuthlibProxyForServer/actions/workflows/release.yml)
[![Release](https://img.shields.io/github/v/release/AyakaCraft/AuthlibProxyForServer?label=Release&include_prereleases)](https://github.com/AyakaCraft/AuthlibProxyForServer/releases)

[简体中文](README_zh.md) ｜ English

Copyright (c) 2025 Calboot and contributors

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

Authlib proxy for and only for servers

Sets the proxy for contacting Mojang server

Mostly based on [Fallen-Breath's template](https://github.com/Fallen-Breath/fabric-mod-template)

## Links

- [Github](https://github.com/AyakaCraft/AuthlibProxyForServer)
- [Discord](https://discord.gg/r2WyWrx5vw)
- [Modrinth](https://modrinth.com/project/authlibproxyforserver)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)

## Usage

- Install this mod to your server (dedicated only)
- Start the server
- Run `/authproxy` to configure the proxy

```json5
{
  "enabled": false, // set to true to enable
  "type": "HTTP", // or SOCKS
  "host": "127.0.0.1", // either IP or host name
  "port": 7897 // [0,65536)
}
```

## EOL

### Currently supported versions

Currently, the following Minecraft versions are actively supported with new features and bug fixes

| Minecraft Version | Support Until                      |
|-------------------|------------------------------------|
| 1.14.4            | ✔️ Long Term Support               |
| 1.15.2            | ✔️ Long Term Support               |
| 1.16.5            | ✔️ Long Term Support               |
| 1.17.1            | ✔️ Long Term Support               |
| 1.18.2            | ✔️ Long Term Support               |
| 1.19.4            | ✔️ Long Term Support               |
| 1.20.1            | ✔️ Long Term Support               |
| 1.20.6            | ✔️ Long Term Support               |
| 1.21.1, 1.21.4-5  | 🕒 When Minecraft 1.22 is released |
| 1.21.6-7          | 🕒 To be determined                |

### End-of-life versions

The following Minecraft versions are out of the support range. There's no support for these Minecraft versions, unless some critical bugs occur

| Minecraft Version | Last Version | Release Date |
|-------------------|--------------|--------------|

### Scheduled to be supported versions

The following Minecraft versions are scheduled to be supported in the later versions

| Minecraft Version | Support Starts |
|-------------------|----------------|
