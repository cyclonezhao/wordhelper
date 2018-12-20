
package zhaoyi.wordhelper;

public class Word {
	private String word;
	private String desc;
	private String sound;

	public Word(String word, String sound, String desc) {
		this.word = word;
		this.sound = sound;
		this.desc = desc;
	}

	public String getWord() {
		return this.word;
	}

	public String getDesc() {
		return this.desc;
	}

	public String getSound() {
		return this.sound;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
}
