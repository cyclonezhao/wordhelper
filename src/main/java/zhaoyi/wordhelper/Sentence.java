
package zhaoyi.wordhelper;

import zhaoyi.wordhelper.Word;

public class Sentence extends Word {
	public String sentence;

	public Sentence(String word, String sound, String desc, String sentence) {
		super(word, sound, desc);
		this.sentence = sentence;
	}
}
