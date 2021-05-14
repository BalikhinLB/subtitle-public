package subtitle.nlp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

@Log
class MatchesTest {

	@Test
	void test() {
		String string = "1332";
		assertTrue(string.matches("^([0-9]*)$"));
		
		string = "...Adsaff";
		assertTrue(string.matches("^.{3}(.)*"));
		
		String reg = "(.)*[.]{3}$";
		
		string = "If that dolt of a cousin of yours,";
		assertFalse(string.matches(reg));
		string = "If that dolt of a cousin of yours...";
		assertTrue(string.matches(reg));
		string = "...If that dolt of a cousin of yours...";
		assertTrue(string.matches(reg));
		
	}
	
}
