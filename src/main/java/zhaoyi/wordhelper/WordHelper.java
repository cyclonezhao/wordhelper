
package zhaoyi.wordhelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class WordHelper {
	String CR = System.lineSeparator();
	int state = 0;
	boolean sound;
	String file;
	boolean inorder;
	double rate = 0.4;
	boolean talkmode;

	public void exec() throws Exception {
		block1: {
			String raw = FileUtils.readFileToString(new File(this.file), "utf-8");
			List raw_words = new Gson().fromJson(raw, List.class);
			Collection<Word> words = this.buildWords(raw_words);
			while ((words = this.testWords(words)).size() > 0) {
				String choose = this.read(String.format("存在%s个错误，准备重新复习，若需直接退出请输入0", words.size()));
				if (!"0".equals(choose))
					continue;
				break block1;
			}
			this.read("测试完成，回车退出");
		}
	}

	Collection<Word> buildWords(List<Map<String, String>> raw_words) {
		Set<Word> words = raw_words.stream()
				.map(v -> new Word((String) v.get("word"), (String) v.get("sound"), (String) v.get("desc")))
				.collect(Collectors.toSet());
		return words;
	}

	private Set<Word> testWords(Collection<Word> wordSet) throws Exception {
		ArrayList wordLst = new ArrayList(wordSet);
		if (!this.inorder) {
			Collections.shuffle(wordLst);
		}
		HashSet errs = new HashSet();
		Iterator arg4 = wordLst.iterator();
		int count = wordLst.size();
		int index = 0;
		while (arg4.hasNext()) {
			index++;
			Word wordBox = (Word) arg4.next();
			String word = wordBox.getWord();
			String desc = wordBox.getDesc();
			String hideWord = this.hideWordGroup(word);

			this.state = 1;
			String tip = this.genQuestion(wordBox, desc, hideWord);
			String ques = String.format("%s/%s" + CR + "%s", index,count,tip);
			String inputWord = this.read(ques);

			if (inputWord == null) {
				break;
			}

			while (this.state == 1) {
				if (!this.assertAnswer(inputWord, wordBox)) {
					errs.add(wordBox);
					String choose = this.read("拼写错误，请再尝试，开估输入0");
					if ("0".equals(choose)) {

						this.showAnswer(wordBox);
						this.read("请输入任意键继续...");
						this.state = 0;
					} else {
						inputWord = choose;
					}
				} else {
					this.state = 0;
				}
			}

			System.out.println();
		}
		return errs;
	}

	void showAnswer(Word wordBox) {
		System.out.println(wordBox.getWord());
	}

	boolean assertAnswer(String inputWord, Word wordBox) {
		return inputWord.equalsIgnoreCase(wordBox.getWord());
	}

	String genQuestion(Word wordBox, String desc, String hideWord) {
		String tip = String.format("%s\t\t%s", hideWord, desc);
		if (this.sound) {
			tip = String.format("%s\t\t%s %s", hideWord, wordBox.getSound(), desc);
		}
		return tip;
	}

	String hideWordGroup(String word) {
		String[] arr = word.split(" ");
		return Stream.of(arr).map(this::hideWord).collect(Collectors.joining(" "));
	}

	String hideWord(String word) {
		int[] replaceIndex;
		int len = word.length();
		int[] arrn = replaceIndex = Util.nkRandom(new Double(Math.ceil((double) len * this.rate)).intValue(), len,
				true);
		int n = arrn.length;
		int n2 = 0;
		while (n2 < n) {
			int i = arrn[n2];
			word = String.valueOf(word.substring(0, i)) + "*" + word.substring(i + 1);
			++n2;
		}
		return word;
	}

	private String read(String tip) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		System.out.println(tip);
		str = br.readLine();
		return str;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setInorder(boolean inorder) {
		this.inorder = inorder;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setTalkmode(boolean talkmode) {
		this.talkmode = talkmode;
	}
}