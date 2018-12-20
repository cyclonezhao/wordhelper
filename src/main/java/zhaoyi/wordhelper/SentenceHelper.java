
package zhaoyi.wordhelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SentenceHelper extends WordHelper {
	String CR = System.lineSeparator();

	@Override
	Collection<Word> buildWords(List<Map<String, String>> raw_words) {
		List<Word> words = raw_words.stream().map(v -> new Sentence((String) v.get("word"), (String) v.get("sound"),
				(String) v.get("desc"), (String) v.get("sentence"))).collect(Collectors.toList());
		if (!this.inorder) {
			return new HashSet<Word>(words);
		}
		return words;
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
