
package zhaoyi.wordhelper;

import zhaoyi.wordhelper.SentenceHelper;
import zhaoyi.wordhelper.WordHelper;

public class Main {
	public static void main(String[] args) {
		try {
			int e = args.length;
			int i = 0;
			String file = null;
			boolean sound = false;
			boolean sentence = false;
			boolean inorder = false;
			boolean talkmode = false;
			boolean randomword = false;
			boolean extractword = false;
			double rate;
			String arg;
			for (rate = 0.4D; i < e; ++i) {

				arg = args[i];
				if ("-f".equalsIgnoreCase(arg)) {
					++i;
					file = args[i];
				} else if ("sound".equalsIgnoreCase(arg)) {
					sound = true;
				} else if ("sentence".equalsIgnoreCase(arg)) {
					sentence = true;
				} else if ("inorder".equalsIgnoreCase(arg)) {
					inorder = true;
				} else if ("-rate".equalsIgnoreCase(arg)) {
					++i;
					rate = Double.valueOf(args[i]).doubleValue();
				} else if ("talkmode".equalsIgnoreCase(arg)) {
					talkmode = true;
					inorder = true;
				} else if ("randomword".equalsIgnoreCase(arg)) {
					randomword = true;
				} else if ("-h".equalsIgnoreCase(arg)) {
					System.out.println("-f file [extractword] [sound] [sentence] [inorder] [-rate ratevalue] [talkmode] [randomword] [-h]");
					return;
				} else if("extractword".equalsIgnoreCase(arg)) {
					extractword = true;
				}
			}

			if(extractword) {
				WordExtractor wordExtractor = new WordExtractor();
				wordExtractor.setFile(file);
				wordExtractor.exec();
			}else {
				WordHelper wordhelper;
				if (sentence) {
					wordhelper = new SentenceHelper();
				} else { 
					wordhelper = new WordHelper();
					wordhelper.setSound(sound);
				}
				wordhelper.setFile(file);
				wordhelper.setInorder(inorder);
				wordhelper.setRate(rate);
				wordhelper.setTalkmode(talkmode);
				wordhelper.setRandomWord(randomword);
				wordhelper.exec();
			}
		} catch (Exception arg10) {

			arg10.printStackTrace();
		}
	}
}
