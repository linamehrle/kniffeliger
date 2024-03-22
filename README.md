
<!---
This is a readme file. It typically includes some information about your project.
For more information about readmes, you can either [read a guide](https://github.com/18F/open-source-guide/blob/18f-pages/pages/making-readmes-readable.md) or have a look at the readmes of popular open-source projects such as [Swift by Apple](https://github.com/apple/swift) or [Tensorflow](https://github.com/tensorflow/tensorflow).

Readme files are typically formatted in Markdown.
However, there are platform-specific flavors, so for this project, you can make full use of the [Gitlab markdown syntax](https://docs.gitlab.com/ee/user/markdown.html), for example when talking about a :bug: (bug) or if your code is slow like a :snail:.
You can also tag people using @username and reference issues using '#1', where 1 is the issue number. For more features, consult the linked Gitlab syntax guide.

If you don't like reading documentation, [here's a cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet).
-->
<!-- insert team / project logo? -->

# Kniffeliger

Kniffeliger is a multiplayer computer game inspired by the classic [Kniffel](https://de.wikipedia.org/wiki/Kniffel) (also known as [Yahtzee](https://en.wikipedia.org/wiki/Yahtzee) ). Apart from being fully computer-based and offering remote multiplayer capability, it offers additional game rules compared to its analogue antetype.

1. [Getting Started](#gettingstarted)
    * [System Requirements](#systemrequirements)
    * [Installation](#installation)
    Build instructions will be added here.
2. [Gameplay](#gameplay)
3. [Implementation](#implementation)
4. [Resources](#resources)
5. [License](#license)
6. [Authors](#authors)

## Getting Started <a name="gettingstarted"></a>

### System Requirements <a name="systemrequirements"></a>

* Java 19.0 or later <!-- is this correct? -->

### Installation <a name="Installation"></a>
1. Clone the repository
2. Build the JAR-file with the command:
```sh
gradlew task build-cs108
```
3. Run the JAR-file with the command:
```sh
java -jar <location>
```
where &lt;location&gt; is a placeholder for the path of the JAR-file.

## Gameplay <a name="gameplay"></a>
This only gives a quick overview about starting a game, changing the username and the basic flow of the game. Detailled game rules are available in the [documentation]((/docs/About%20a%20Game%20(advanced))).


## Implementation <a name="implementation"></a>

Kniffeliger is implemented in [Java 19.0](https://dev.java) in a [client-server architecture](https://en.wikipedia.org/wiki/Client–server_model). The server handles the game logic and tracks the state of (multiple) games of a maximum of 4 players. The client provides a graphical user interface (GUI) and handles communication with the server in the background.



## Resources <a name="resources"></a>
* [Specification Catalogue (Requirements)](/docs/requirements.txt)
* [Technical Documentation](TODO)
* [Network protocol](/docs/networkProtocol.md)
* [Rules of the Game](/docs/About%20a%20Game%20(advanced))



## License <a name="license"></a>
Not yet public.

## Authors <a name="authors"></a>
<!-- insert team cartoons? -->
* Anisja Mayer
* Lina Mehrle
* Riccardo Grieco
* Dominique Ostermayer

