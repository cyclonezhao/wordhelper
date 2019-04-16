# -*- coding: utf-8 -*-
import re
import argparse
from pip._vendor.distlib.compat import raw_input

# adding arguments description which is shown in command line by typing "-h"
parser = argparse.ArgumentParser()
parser.add_argument("file", help="the full path of the file which gonna be interpreted as input")
parser.add_argument("-inorder", help="if true it will show each exam item in original order.", action="store_true")
parser.add_argument("-rate", help="indicate the rate which calculate the count of letters that will be replaced by '*' by multiplying the length of the word.", default="0.4", type=float)

# parsing arguments from the user command line
args = parser.parse_args()
fileInput = args.file
inorder = args.inorder
rate = args.rate

import json
import random
import math

def nkrandom(begin, end, count):
    _set = set()
    while len(_set) < count:
        _set.add(random.randint(begin, end))
    return _set

def hideSingleWord(singleWord):
    if rate == 1:
        return '*'
    letterCount = len(singleWord)
    indexCount = math.ceil(letterCount * rate)
    indexSet = nkrandom(0, letterCount-1, indexCount)
    for index in indexSet:
        singleWord = singleWord[:index] + '*' + singleWord[index+1:letterCount]
    return singleWord

def hideWord(phrases):
    arr = phrases.split(",")
    hidedPhrases = []
    for phrase in arr:
        wordArr = phrase.split(" ")
        if rate == 1:
            hidedPhrases.append("*")
        else:
            hidedPhrases.append(" ".join(list(map(hideSingleWord, wordArr))))
    return ",".join(hidedPhrases)

def genQuestion(wordBox, hidedWords):
    sentence = wordBox.get("sentence")
    wordArr = wordBox.get("word").split(",")
    hidedWordArr = hidedWords.split(",")
    for idx, word in enumerate(wordArr):
        insensitive_hippo = re.compile(re.escape(word), re.IGNORECASE)
        sentence = insensitive_hippo.sub(hidedWordArr[idx], sentence)
        
    tips = "%s\n-------------------------\n%s" % (sentence, hidedWords)
    if 'desc' in wordBox and len(wordBox.get("desc")) > 0:
        tips = "%s\n%s\n-------------------------\n%s" % (sentence,wordBox.get("desc"), hidedWords)
    return tips

def testWords(words):
    if not inorder:
        random.shuffle(words)
    index = 0
    count = len(words)
    errs = []
    
    for wordBox in words:
        index += 1
        word = wordBox.get("word")
        hidedWord = hideWord(word)
        
        tips = genQuestion(wordBox, hidedWord)
        ques = "\n%s/%s\n%s\n" % (index, count, tips)
        inputWord = raw_input(ques)
        
        stillTestThisWord = True
        while(stillTestThisWord):
            if inputWord == "":
                if wordBox not in errs:
                    errs.append(wordBox)
                raw_input("%s\nPress any key to continue." % word)
                stillTestThisWord = False
            elif not inputWord.lower() == word.lower():
                if wordBox not in errs:
                    errs.append(wordBox)
                choose = raw_input("Wrong spelling, please try again, or type 0 and press ENTER to show the right answer.\n")
                if("0" == choose):
                    raw_input("%s\nPress any key to continue." % word)
                    stillTestThisWord = False
                else:
                    inputWord = choose
            else:
                stillTestThisWord = False
    return errs

words = json.loads(open(fileInput).read())
while True:
    words = testWords(words)
    errCount = len(words)
    if errCount > 0:
        choose = raw_input("\nYou have %s errors, prepare to review. Typing 0 and pressing ENTER if you want to exit." % errCount)
        if "0" == choose:
            break
    else:
        break
raw_input("\nTest finished, press any key to exit, See you!")