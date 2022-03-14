package com.example.qrgameteam15;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import static org.junit.Assert.*;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;


public class MainNavigationTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        // initialize
        // Instrumententation allow...
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * navigate to main menu
     */
    @Test
    public void navigateToMainMenu() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("New User"); //Click ADD CITY Button
        solo.sleep(2000); // 2 pause
        // enter a different user each test
        solo.assertCurrentActivity("Wrong Activity", NewUser.class);
        Random random = new Random();
        int randomint = random.nextInt(100 - 0) + 0;
        String enterUsername = "AndroidTestUser" + Integer.toString(randomint);
        solo.enterText((EditText) solo.getView(R.id.username_text), enterUsername);
        solo.enterText((EditText) solo.getView(R.id.name_text), "1");
        solo.enterText((EditText) solo.getView(R.id.email_text), "1");
        solo.enterText((EditText) solo.getView(R.id.city_region), "1");
        solo.clickOnButton("Create Account"); //Click ADD CITY Button
        solo.sleep(2000); // 2 pause
        solo.assertCurrentActivity("Wrong Activity", UserMenu.class);
    }

    @Test
    public void scanAQRcode() {
        
    }



    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
