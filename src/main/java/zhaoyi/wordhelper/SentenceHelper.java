
package zhaoyi.wordhelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SentenceHelper extends WordHelper {
	String CR = System.lineSeparator();

	@Override
	Collection<Word> buildWords(List<Map<String, String>> raw_words) {
		List<Word> words = raw_words.stream().map(v -> {
			if(randomWord){
				String sentence = v.get("sentence");
				List<String> wordLst = extractWord(sentence);
				int len = wordLst.size();
				int n = new Double(Math.ceil(len * 0.4)).intValue();
				int k = len;
				int[] indexes = Util.nkRandom(n, k, true);
				Set<String> wordSet = new HashSet();
				for(int i : indexes){
					wordSet.add(wordLst.get(i));
				}
				String wordStr = Util.join(wordSet, ",");
				return new Sentence(wordStr, 
						v.get("sound"),
						v.get("desc"), 
						v.get("sentence"));
			}else{
				if(!v.containsKey("word")){
					return null;
				}
				return new Sentence(v.get("word"), 
						v.get("sound"),
						v.get("desc"), 
						v.get("sentence"));
			}
		}).filter(v->v != null).collect(Collectors.toList());
		return words;
	}
	
	public List extractWord(String sentence){
		
		byte[] bytes=sentence.getBytes();
		
		List list=new ArrayList<String>();
		
		StringBuffer buffer=new StringBuffer();
		
		for (int i = 0; i <bytes.length ; i++) {
			
			if ((bytes[i]>=65 && bytes[i]<=90)||(bytes[i]>=97 && bytes[i]<=122)){
				
				buffer.append(sentence.charAt(i));
				
			}else {
				
				list.add(buffer.toString());
				
				buffer=new StringBuffer();
				
			}
			
		}
		
		return list;
		
	}

	String genQuestion(Word wordBox, String desc, String hideWords) {
		Sentence sentenceBox = (Sentence) wordBox;
		String sentence = sentenceBox.sentence;

		String[] words = sentenceBox.getWord().split(",");
		String[] hideWordArr = hideWords.split(",");
		for (int tip = 0; tip < words.length; ++tip) {
			String word = words[tip];
			sentence = sentence.replace(word, hideWordArr[tip]);
		}

		String arg9 = "";
		if (this.talkmode) {
			arg9 = sentence;
			if (sentenceBox.getDesc() != null && !sentenceBox.getDesc().trim().equals("")) {
				arg9 = String.format("%s" + this.CR + "%s", new Object[] { sentence, sentenceBox.getDesc() });
			}
		} else {
			arg9 = String.format("%s" + this.CR + "---------------------" + this.CR + "%s",
					new Object[] { sentence, hideWords });
			if (sentenceBox.getDesc() != null && !sentenceBox.getDesc().trim().equals("")) {
				arg9 = String.format("%s" + this.CR + "%s" + this.CR + "---------------------" + this.CR + "%s",
						new Object[] { sentence, sentenceBox.getDesc(), hideWords });
			}
		}

		return arg9;
	}

	@Override
	String hideWordGroup(String word) {
		String[] arr = word.split(",");
		return Stream.of(arr).map(string -> super.hideWordGroup(string)).collect(Collectors.joining(","));
	}

	@Override
	boolean assertAnswer(String inputWord, Word wordBox) {
		if (this.talkmode) {
			Sentence sentenceBox = (Sentence) wordBox;
			return inputWord.equalsIgnoreCase(sentenceBox.sentence);
		}
		return super.assertAnswer(inputWord, wordBox);
	}

	@Override
	void showAnswer(Word wordBox) {
		if (this.talkmode) {
			Sentence sentenceBox = (Sentence) wordBox;
			System.out.println(sentenceBox.sentence);
			return;
		}
		super.showAnswer(wordBox);
	}
}
