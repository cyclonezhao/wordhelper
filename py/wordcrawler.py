# -*- coding: utf-8 -*-
import urllib.request
import re
import argparse
from builtins import map
from os import path

def getHtml(url):
    # 获取网页内容
    page = urllib.request.urlopen(url)
    html = page.read()
    return html.decode('utf-8')

def getContent(word):
    url = "https://dict.youdao.com/w/%s" % word
    html = getHtml(url)
    # 最终结果
    results = []
    # 获取音标
    sound = re.compile(r'<em class="additional spell phonetic">(.+?)</em>').findall(html)
    if len(sound) > 0:
        sound = sound[0]
    # 分割内容
    while True:
        content = html.partition('<div class="collinsMajorTrans">')
        if len(content) < 3:
            return results
        content = content[2]
        if len(content.strip()) < 1:
            return results
        
        beginIndex = content.index("<p>")
        endIndex = content.index("</p", beginIndex)
        html = content[endIndex:]
        content = content[beginIndex:endIndex]
        
        # 获取词性
        vt_re = re.compile(r"<span .+?>(.+?)</span>")
        vt = vt_re.findall(content)
        if len(vt) > 0:
            vt = "[" + vt_re.findall(content)[0] + "]"
        
        #  过滤词性 span
        beginIndex = content.find("</span")
        if beginIndex > 0:
            beginIndex += 7
            content = content[beginIndex:]
            endIndex = content.find("<span")
            if endIndex > 0:
                content = content[:endIndex]
        
        # 去掉关键词的 b 标签
        content = re.compile(r"</?b>").sub("", content)
        # 去掉中文注释
#         content = content[:content.index(".")+1]

        # 特殊情况跳过
        if content.find("see also") > 0:
            continue
        
        results.append({"vt": vt, "keyword": word, "desc": content.strip(), "sound": sound})

# 输入文件
parser = argparse.ArgumentParser()
parser.add_argument("file", help="the full path of the file which gonna be interpreted as input")
args = parser.parse_args()
fileNameInput = args.file
    
# 读取要查的词
fileinput = open(fileNameInput, "rt")
words = fileinput.read().split("\n")

#解析
import operator
from functools import reduce

contents = reduce(operator.add, map(lambda v: getContent(v), words))

#格式化转换
contentFormatted = map(lambda v: '{"word":"%s","sentence":"%s %s %s"}' % (v.get("keyword"),v.get("vt"),v.get("sound"),v.get("desc")), contents)
output = '[' + '\n,'.join(contentFormatted) + ']'

# 输出
dirName = path.dirname(fileNameInput)
outputName = "wordsFromYoudao.json"
if len(dirName) > 0:
    outputName = "%s%swordsFromYoudao.json" % (dirName, path.sep)
fileOutput = open(outputName, "w", encoding='utf-8')
fileOutput.write(output)
fileOutput.close()
# print(output)