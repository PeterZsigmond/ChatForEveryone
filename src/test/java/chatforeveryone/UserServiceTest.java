package chatforeveryone;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import chatforeveryone.service.UserService;

public class UserServiceTest
{
	@Test
	public void isValidEmailTest()
	{
		ArrayList<String> wrongEmails = new ArrayList<>();
		ArrayList<String> goodEmails = new ArrayList<>();
		
		// Wrong emails, for test cases.
		wrongEmails.add("a");
		wrongEmails.add("hellobello");
		wrongEmails.add("hello@world");
		wrongEmails.add("hello@world.");
		wrongEmails.add("hello@world.h");
		wrongEmails.add("@world.hu");
		wrongEmails.add("hello.hu");
		wrongEmails.add("hello@s.abcdefg");
		wrongEmails.add("hello@world.123");
		
		// Good emails, for test cases.
		goodEmails.add("chat@everyone.hu");
		goodEmails.add("chat@noone.com");
		goodEmails.add("chat@80.80.80.80.hu");
		goodEmails.add("cHAT123@foreveryone.co.uk");
		
		for(String email : wrongEmails)
		{
			assertFalse(UserService.isValidEmail(email));
		}
		
		for(String email : goodEmails)
		{
			assertTrue(UserService.isValidEmail(email));
		}
	}
}