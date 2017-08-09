package enums;

public enum ProductEnum {
	ALL(new String[] {"*"}),
	NONE(new String[] {}),
	COLAS(new String[] {"Coca-Cola","COCA-COLA", "cc", "CC"}),
	FAMRET(new String[] {"RGB", "REF PET"});
	
	private String[] synonyms;
	
	ProductEnum(final String[] synonyms) {
		this.synonyms = synonyms;
	}

	public String[] getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String[] synonyms) {
		this.synonyms = synonyms;
	}
	
}