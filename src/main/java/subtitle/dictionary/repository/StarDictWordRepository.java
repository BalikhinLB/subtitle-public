package subtitle.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import subtitle.dictionary.model.StarDictWord;

public interface StarDictWordRepository extends JpaRepository<StarDictWord, Long> {
	
	StarDictWord findByWord(String word);
	boolean existsByWord(String word);

}
