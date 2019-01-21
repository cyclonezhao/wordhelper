import re
from collections import Counter
import argparse
from pip._vendor.distlib.compat import raw_input
from os import path

# adding arguments description which is shown in command line by typing "-h"
parser = argparse.ArgumentParser()
parser.add_argument("file", help="the fullpath of the file which gonna be interpreted as input")
parser.add_argument("-inorder", help="if true it will show each exam item in original order.", action="store_true")
parser.add_argument("-rate", help="indicate the rate which calculate the count of letters that will be replaced by '*' by multipling the length of the word.", default="0.4", type=float)
parser.add_argument("-extractword", help="run in Extract Word Mode rather than Exam Mode as default.", action="store_true")

# parsing arguments from the user command line
args = parser.parse_args()
fileInput = args.file
inorder = args.inorder
rate = args.rate
extractword = args.extractword

if extractword:
    def extractWord(sentence):
        fileExclusive = open(path.sep.join((path.pardir,"resources","extractWords_exclusive")), "rt")
        wordsExclusive = fileExclusive.read().split("\n")
        wordsInput = re.compile(r"[\s+-/]").split(sentence)
    
        reWordPurifier = re.compile(r"[^a-zA-Z']")
        wordsInput = list(map(lambda v: reWordPurifier.sub("", v).lower(), wordsInput))
        wordsInput = list(filter(lambda v: len(v.strip())>0 and (v not in wordsExclusive), wordsInput))
        return wordsInput
    
    wordLst = extractWord(fileInput.read())
    wordGrp = Counter(wordLst)
    sortedByFreq = sorted(wordGrp.items(), key=lambda v: v[1])
    formatted = map(lambda v: "%s : %s" % (v[0], v[1]), sortedByFreq)
    fileOutput = open("%s%swordExtracted" % (path.dirname(fileInput), path.sep), "w")
    fileOutput.write("\n".join(formatted))
    fileOutput.close()
else:
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
            hidedPhrases.append(" ".join(list(map(hideSingleWord, wordArr))))
        return ",".join(hidedPhrases)
    
    def genQuestion(wordBox, hidedWords):
        sentence = wordBox.get("sentence")
        wordArr = wordBox.get("word").split(",")
        hidedWordArr = hidedWords.split(",")
        for idx, word in enumerate(wordArr):
            sentence = sentence.replace(word, hidedWordArr[idx])
            
        tips = "%s\n-------------------------\n%s" % (sentence, hidedWords)
        if 'desc' in wordBox and len(wordBox.get("desc")) > 0:
            "%s\n%s\n-------------------------\n%s" % (sentence,wordBox.get("desc"), hidedWords)
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
                if not inputWord.lower() == word.lower():
                    errs.append(wordBox)
                    choose = raw_input("Wrong spelling, please try again, or type 0 and press ENTER to show the right answer.\n")
                    if("0" == choose):
                        raw_input("%s\nPress any key to continue." % word)
                        stillTestThisWord = False
                    else:
                        inputWord = choose
                else:
                    stillTestThisWord = False
        
    
    words = json.loads(open(fileInput).read())
    while len(testWords(words)) > 0:
        choose = raw_input("You have %s errors, prepare to review. Typing 0 and pressing ENTER if you want to exit.")
        if "0" == choose:
            break
    raw_input("Test finished, press any key to exit, See you!")