from collections import Counter
from os import path
import re
import argparse

def extractWord(sentence):
    fileExclusive = open(path.sep.join((path.pardir,"resources","extractWords_exclusive")), "rt")
    wordsExclusive = fileExclusive.read().split("\n")
    wordsInput = re.compile(r"[\s+-/]").split(sentence)

    reWordPurifier = re.compile(r"[^a-zA-Z']")
    wordsInput = list(map(lambda v: reWordPurifier.sub("", v).lower(), wordsInput))
    wordsInput = list(filter(lambda v: len(v.strip())>0 and (v not in wordsExclusive), wordsInput))
    return wordsInput

# adding arguments description which is shown in command line by typing "-h"
parser = argparse.ArgumentParser()
parser.add_argument("file", help="the full path of the file which gonna be interpreted as input")

# parsing arguments from the user command line
args = parser.parse_args()
fileInput = args.file

wordLst = extractWord(fileInput.read())
wordGrp = Counter(wordLst)
sortedByFreq = sorted(wordGrp.items(), key=lambda v: v[1])
formatted = map(lambda v: "%s : %s" % (v[0], v[1]), sortedByFreq)
fileOutput = open("%s%swordExtracted" % (path.dirname(fileInput), path.sep), "w")
fileOutput.write("\n".join(formatted))
fileOutput.close()