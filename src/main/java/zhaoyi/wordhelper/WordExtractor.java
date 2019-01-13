package zhaoyi.wordhelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class WordExtractor {

	private String file;

	public void setFile(String file) {
		this.file = file;
	}

	public void exec() throws Exception {
		String raw = FileUtils.readFileToString(new File(this.file), "utf-8");
		List<String> words = extractWord(raw);
		Map<String, Long> wordGroup = words.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
		String result = wordGroup.entrySet().stream()
				.sorted(Map.Entry.<String,Long>comparingByValue())
				.map(v->String.format("%s : %s", v.getKey(),v.getValue()))
				.collect(Collectors.joining(System.lineSeparator()));
		FileUtils.write(new File("wordExtracted"), result, "utf-8");
	}

	private List extractWord(String sentence) throws Exception {
		String exclusive = FileUtils.readFileToString(new File("extractWords_exclusive"), "utf-8");
		Set<String> wordKnownSet = new HashSet(Arrays.asList(exclusive.split(System.lineSeparator())));
		List list=new ArrayList<String>();
		String[] split = sentence.split("[\\s+-]");
		list = Stream.of(split).map(v->v.replaceAll("[^a-zA-Z']", "").toLowerCase())
				.filter(v->v.trim().length() > 0 && !wordKnownSet.contains(v))
				.collect(Collectors.toList());
		return list;
		
	}
}
