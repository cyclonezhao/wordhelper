package zhaoyi.wordhelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
		FileUtils.write(new File("e:\\wordExtracted"), result, "utf-8");
	}

	public List extractWord(String sentence){
		
		byte[] bytes=sentence.getBytes();
		
		List list=new ArrayList<String>();
		
		StringBuffer buffer=new StringBuffer();
		
		for (int i = 0; i <bytes.length ; i++) {
			
			if ((bytes[i]>=65 && bytes[i]<=90)||(bytes[i]>=97 && bytes[i]<=122)){
				
				buffer.append(sentence.charAt(i));
				
			}else {
				
				list.add(buffer.toString().toLowerCase());
				
				buffer=new StringBuffer();
				
			}
			
		}
		
		return list;
		
	}
}
