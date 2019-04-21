from collections import Counter
from os import path
import re
import argparse

# adding arguments description which is shown in command line by typing "-h"
parser = argparse.ArgumentParser()
parser.add_argument("file", help="the full path of the file which gonna be interpreted as input")
parser.add_argument("exclusive", help="the full path of the file which gonna be interpreted as exclusive word file")
parser.add_argument("-freq", help="if true it will statistic the frequence of the count of each word appears.", action="store_true")

# parsing arguments from the user command line
args = parser.parse_args()
fileInput = args.file
exclusive = args.exclusive
freq = args.freq

def extractWord(sentence):
    fileExclusive = open(exclusive, "rt")
    wordsExclusive = fileExclusive.read().split("\n")
    wordsInput = re.compile(r"[\s+-/]").split(sentence)

    reWordPurifier = re.compile(r"[^a-zA-Z']")
    wordsInput = list(map(lambda v: reWordPurifier.sub("", v).lower(), wordsInput))
    wordsInput = list(filter(lambda v: len(v.strip())>0 and (v not in wordsExclusive), wordsInput))
    return wordsInput

wordLst = extractWord(open(fileInput).read())
fileOutput = open("%s%swordExtracted" % (path.dirname(fileInput), path.sep), "w")
wordGrp = Counter(wordLst)
sortedByFreq = sorted(wordGrp.items(), key=lambda v: v[1])
if freq:
    formatted = map(lambda v: "%s : %s" % (v[0], v[1]), sortedByFreq)
    fileOutput.write("\n".join(formatted))
else:
    formatted = map(lambda v: v[0], sortedByFreq)
    fileOutput.write("\n".join(formatted))
    
fileOutput.close()