![banner](https://cdn.nat.gg/projects/chromaanvils/banner.png?v=1)

<hr/>

<div align="center">



![GitHub licence](https://img.shields.io/github/license/NATroutter/ChromaAnvils?style=for-the-badge&logo=github&foceupdate=1)
![Discord](https://img.shields.io/discord/256776363892015115?style=for-the-badge&label=Discord&logo=discord&foceupdate=1)  

![GitHub release (with filter)](https://img.shields.io/github/v/release/NATroutter/ChromaAnvils?style=for-the-badge&label=Latest%20Release&logo=github&foceupdate=4)
![Modrinth Version](https://img.shields.io/modrinth/v/chromaanvils?style=for-the-badge)
![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/chromaanvils?style=for-the-badge)


![Modrinth Downloads](https://img.shields.io/modrinth/dt/chromaanvils?style=for-the-badge&label=Modrinth%20Downloads)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/NATroutter/ChromaAnvils/total?style=for-the-badge&label=Github%20Downloads)

![GitHub issues](https://img.shields.io/github/issues/NATroutter/ChromaAnvils?style=for-the-badge&logo=github&foceupdate=1)
![GitHub last commit](https://img.shields.io/github/last-commit/NATroutter/ChromaAnvils?style=for-the-badge&logo=github&foceupdate=1)

<hr/>

</div>

<div align="center" style="text-align: center;display:block; margin:auto;">

![Divider-about](https://cdn.nat.gg/projects/chromaanvils/divider/about.png)

</div>

With this simple fabric mod you can add a little color to your items with ChromaAnvils. This mod lets you style and color your items however you want, enabling you to have cool and unique items.

<div align="center" style="text-align: center;display:block; margin:auto;">

![Divider-features](https://cdn.nat.gg/projects/chromaanvils/divider/features.png)

</div>

🟢 **No mandatory client installation.**  
🟢 **MiniMessage formating.**  
🟢 **Permission support for server installation.**  
🟢 **Ability to blacklist items.**  
🟢 **Extensive configs.**

<div align="center" style="text-align: center;display:block; margin:auto;">

![Divider-showcase](https://cdn.nat.gg/projects/chromaanvils/divider/showcase.png)

![Demo 1](https://cdn.nat.gg//projects/chromaanvils/demo.png?v=3)
![Demo 2](https://cdn.nat.gg//projects/chromaanvils/demo2.png?v=3)

</div>

<div align="center" style="text-align: center;display:block; margin:auto;">

![Divider-usage](https://cdn.nat.gg/projects/chromaanvils/divider/usage.png?v=1)

</div>

## 🔒 Permissions
If you have a permission plugin/mod like [LuckPerms](https://luckperms.net/), you are required to have the permission ``chromaanvils.use`` to be able to use it. If you don't have any permission plugins/mods, all players are able to use it.

Additionally, you will also need to have the following permissions, corresponding to the formatting you want to allow:

```properties
chromaanvils.colors
chromaanvils.decorations
chromaanvils.font
chromaanvils.gradient
chromaanvils.rainbow
chromaanvils.transition
chromaanvils.reset
```

## ✏️ Formating

**Named colors**: black, dark_blue, dark_green, dark_aqua, dark_red, dark_purple, gold, gray, dark_gray, blue, green, aqua, red, light_purple, yellow, or white.

You can also use the [MiniMessage Viewer](https://webui.advntr.dev/) to "generate" the styling so that you dont have to fiddle with the tags


| Tag                                            | Example                                                                     | Description                                                                                                                                   |
|------------------------------------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| ```<named color>``` ```<hex color>```          | ```<dark_red>``` ```<#ff0000```                                             | Changes the color for next part of the text                                                                                                   |
| ```<reset>```                                  | ```<reset>```                                                               | Close all currently open tags, resetting color/decoration/etc.                                                                                |
| ```<rainbow:[!][phase]>```                     | ```<rainbow>``` ```<rainbow:!>``` ```<rainbow:2>``` ```<rainbow:!2>```      | ```!``` reverses the rainbow direction and ```phase``` shifts the rainbow starting point                                                      |
| ```<gradient:[color1]:[color...]>```           | ```<gradient:#ff0000:#00ff00>Example</gradient>```                          | Transitions between colors.                                                                                                                   |
| ```<transition:[color1]:[color...]:[phase]>``` | ```<transition:#ffffff:#000000:0>Example</transition>```                    | Transitions between colors. Similar to a gradient, but everything is the same color and the phase chooses that color                          |
| ```<font:[key]>```                             | ```<font:alt>Example</font>```                                              | Allows to change the font of the text                                                                                                         |
| ```<[deocraion]>```                            | ```<bold>``` ```<underlined>``` ```<st>``` ```<obf>``` ```<i>``` ```<!i>``` | Decorate the next part of the text. You can use ```!``` to reverse the decoration. Look at the decoration table below for all available tags. |

## ✨ Decorations tags
| Decoraions           | Alias                |
|----------------------|----------------------|
| ```bold```           | ```b```              |
| ```italic```         | ```em``` or ```i```  |
| ```underlined```     | ```u```              |
| ```strikethrough```  | ```st```             |
| ```obfuscated```     | ```obf```            |

For more information about you can refer minimessages own [documentation](https://docs.advntr.dev/minimessage/format.html) but keep in mind that all the tags provided by minimessage doesn't work in item names


<div align="center" style="text-align: center;display:block; margin:auto;">

![Divider-requirements](https://cdn.nat.gg/projects/chromaanvils/divider/requirements.png)

</div>

- [Fabric API](https://modrinth.com/mod/fabric-api) | 0.100.4 (or newer)
- [Cloth Config](https://modrinth.com/mod/cloth-config) | 15.0.127 (or newer)

<div align="center" style="text-align: center;display:block; margin:auto;">

![Divider-download](https://cdn.nat.gg/projects/chromaanvils/divider/download.png)
[![Available on modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/project/chromaanvils/versions)
[![Available on github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg)](https://github.com/NATroutter/ChromaAnvils/releases)

</div>

