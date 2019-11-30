package lib.clearclass.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "words")
public class Word {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String word;
	
	public Word(String word) {
		this.word = word;
	}
	
	public Word() {}

	@Override
	public String toString() {
		return word;
	}
}