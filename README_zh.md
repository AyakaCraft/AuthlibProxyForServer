# AuthlibProxyForServer

[![许可证](https://img.shields.io/static/v1?label=License&message=gpl-v3.0&color=red)](https://www.gnu.org/licenses/gpl-3.0.txt)
[![Modrinth 下载量](https://img.shields.io/modrinth/dt/authlibproxyforserver?label=Modrinth%20Downloads)](https://modrinth.com/mod/authlibproxyforserver)
[![CurseForge 下载量](https://cf.way2muchnoise.eu/full_authlibproxyforserver_CurseForge%20Downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)
[![GitHub 下载量](https://img.shields.io/github/downloads/AyakaCraft/AuthlibProxyForServer/total?label=Github%20Downloads&logo=github)](https://github.com/AyakaCraft/AuthlibProxyForServer/releases)
[![MC 版本](https://cf.way2muchnoise.eu/versions/MC%20Version_authlibproxyforserver_all.svg)](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)
[![Build & Publish](https://github.com/AyakaCraft/AuthlibProxyForServer/actions/workflows/release.yml/badge.svg)](https://github.com/AyakaCraft/AuthlibProxyForServer/actions/workflows/release.yml)
[![Release](https://img.shields.io/github/v/release/AyakaCraft/AuthlibProxyForServer?label=Release&include_prereleases)](https://github.com/AyakaCraft/AuthlibProxyForServer/releases)

简体中文 ｜ [English](README.md)

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

## 综述

服务器专用的 authlib 代理

设置连接到 Mojang 服务器的代理

大部分基于 [Fallen Breath 的模板](https://github.com/Fallen-Breath/fabric-mod-template)

## 链接

- [Github](https://github.com/AyakaCraft/AuthlibProxyForServer)
- [Discord](https://discord.gg/r2WyWrx5vw)
- [Modrinth](https://modrinth.com/mod/authlibproxyforserver)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/authlibproxyforserver)

## 用法

- 安装此模组到你的服务器（仅限物理服务端）
- 启动一次服务器
- 编辑 `$SERVER_ROOT$/config/authlib.proxy.json`
```json5
{
  "enabled": false, // set to true to enable
  "type": "HTTP", // or DIRECT, SOCKS
  "host": "127.0.0.1", // either IP or host name
  "port": 7897 // [0,65536)
}
```

## 生命周期计划

### 当前支持的版本

目前，以下 Minecraft 版本正在积极地获得新功能和错误修复的支持

| Minecraft 版本   | **支持至**               |
|----------------|-----------------------|
| 1.14.4         | ✔️ 长期支持               |
| 1.15.2         | ✔️ 长期支持               |
| 1.16.5         | ✔️ 长期支持               |
| 1.17.1         | ✔️ 长期支持               |
| 1.18.2         | ✔️ 长期支持               |
| 1.19.4         | ✔️ 长期支持               |
| 1.20.1         | ✔️ 长期支持               |
| 1.20.6         | ✔️ 长期支持               |
| 1.21.1, 1.21.4 | 🕒 Minecraft 1.22 发布时 |
| 1.21.5, 1.21.6 | 🕒 待定                 |

### 已结束支持的版本

以下 Minecraft 版本已不在支持范围内。 除非出现了严重的漏洞，否则这些 Minecraft 版本将不再获得支持

| Minecraft 版本 | **最后的版本**                                                                         | **发布日期**       |
|--------------|-----------------------------------------------------------------------------------|----------------|

### 计划支持的版本

以下 Minecraft 版本计划在未来的版本中得到支持

| Minecraft 版本 | 支持时间 |
|--------------|------|
