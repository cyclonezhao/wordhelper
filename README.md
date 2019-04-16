wordhelper
===

这个项目存放本人为了学习英语单词，所编写的一些小工具。一开始是用java写的，后来觉得用java写这些有点杀鸡用牛刀，所以改用python编写。java和python版本的源码分别放在src和py目录中，其中src目录仅用作备份而保留着，不再更新。

py/wordtrainer.py
---

这是命令行程序，设计初衷是利用碎片化时间检测自己对单词的掌握程度。可以运行`python wordtrainer.py -h`查看相信用法，下面举一例：

首先维护一个json文件，例子可参考 examples/20190408-SteveJobs.json，然后参考此命令行输入： `python3 wordtrainer.py ../examples/20190408-SteveJobs.json`

程序会读取json文件的每一项，在sentence中搜索word中的内容，并随机把单词的一些字幕替换为*，用户按照自己记忆拼写单词然后回车即可，下面展示了当拼写正确和拼写错误时，程序的反应。

```
1/37
A sp*k* is a long piece of metal with a sharp point.
-------------------------
sp*k*
spike

2/37
If you de*ei** someone, you make them believe something that is not true, usually in order to get some advantage for yourself.
-------------------------
de*ei**
deceiva
Wrong spelling, please try again, or type 0 and press ENTER to show the right answer.

deceive
Press any key to continue.

3/37
In his presence, reality is m*lle**l*.
-------------------------
m*lle**l*


```

py/wordextractor.py
---

这是命令行程序，设计目的是读取一个英文文本文件，将其中的单词抽取出来并统计出现频率。用户可以浏览输出文件，将不认识的词挑取出来，那么剩下的词就都是认识的词了，可将这些词维护到排除文件 `examples/extractWords_exclusive` 中，每次抽取单词时都会跳过排除文件中存在的单词。

py/wordcrawler.py
---

这是一个命令行程序，设计目的是读取一个单词文件，爬取单词网站中的释义，并输出一个结果文件。文件内容可以是从运行 wordextractor.py 所输出的文件中挑取出的生词，参考 `examples/words`。

目前爬取的单词网站是有道词典，爬取其中的**柯林斯英汉双解大词典**注释，因为个人觉得这个英英注释简单易懂，而且能兼顾单词含义和造句用法。后续如果需要增加其它爬取策略再作模式重构。

输出的结果文件部分内容是这样的:

```
[{"word":"veterans","sentence":"[N-COUNT] /ˈvɛtərən/ A veteran is someone who has served in the armed forces of their country, especially during a war. 退伍军人"}
,{"word":"veterans","sentence":"[N-COUNT] /ˈvɛtərən/ You use veteran to refer to someone who has been involved in a particular activity for a long time. 经验丰富的人"}
,{"word":"reacted","sentence":"[V-I] /rɪˈækt/ When you react to something that has happened to you, you behave in a particular way because of it. 作出反应"}
(以下省略)
```

由于网站内容的多样化难以全面预料，解析逻辑不免有bug，我至少知道有一个，且不知道怎么解决。因为出bug的频率不高，若碰上了，可手工修正输出文件。

活到老，学到老，共勉！