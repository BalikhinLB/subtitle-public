package subtitle.users.words.model;

import lombok.Getter;

public enum Tags {
    SYM("symbol", false),
    UNKNOWN("unknown"),
    CC("Coordinating conjunction", false),
	CD("Cardinal number"),
    DT("Determiner"),
    EX("Existential there"),
	FW("Foreign word"),
	IN("Preposition or subordinating conjunction", false),
	JJ("Adjective"),
	JJR("Adjective, comparative"),
	JJS("Adjective, superlative"),
	LS("List item marker"),
	MD("Modal"),
	NN("Noun, singular or mass"),
	NNS("Noun, plural"),
	NNP("Proper noun, singular", false),
	NNPS("Proper noun, plural", false),
	PDT("Predeterminer"),
	POS("Possessive ending", false),
	PRP("Personal pronoun", false),
	PRP$("Possessive pronoun", false),
	RB("Adverb"),
	RBR("Adverb, comparative"),
	RBS("Adverb, superlative"),
	RP("Particle", false),
	TO("to", false),
	UH("Interjection", false),
	VB("Verb, base form"),
	VBD("Verb, past tense"),
	VBG("Verb, gerund or present participle"),
	VBN("Verb, past participle"),
	VBP("Verb, non-3rd person singular present"),
	VBZ("Verb, 3rd person singular present"),
	WDT("Wh-determiner"),
	WP("Wh-pronoun"),
	WP$("Possessive wh-pronoun"),
	WRB("Wh-adverb");

    @Getter
    private String definition;
    @Getter
    private boolean impotent;

    Tags(String definition){
    	this.impotent = true;
        this.definition = definition;
    }
	Tags(String definition, boolean impotent){
		this.impotent = impotent;
		this.definition = definition;
	}
}
