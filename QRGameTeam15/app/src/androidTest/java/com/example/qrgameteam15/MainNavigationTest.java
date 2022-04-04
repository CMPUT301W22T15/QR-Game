package com.example.qrgameteam15;

import android.app.Activity;
import android.view.View;
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
     * Create a new user, navigate to main menu and log out test.
     */
    @Test
    public void navigateToMainMenu() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("New User"); //Click ADD CITY Button
        solo.sleep(2000); // 2 pause

        // create a random user each time
        solo.assertCurrentActivity("Wrong Activity", NewUser.class);
        Random random = new Random();
        int randomint = random.nextInt(10000 - 0) + 0;
        String enterUsername = "AndroidTestUser" + Integer.toString(randomint);
        solo.enterText((EditText) solo.getView(R.id.username_text), enterUsername);
        solo.enterText((EditText) solo.getView(R.id.name_text), "1");
        solo.enterText((EditText) solo.getView(R.id.email_text), "1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.city_region), "edmonton");
        //solo.clickOnButton("CREATE ACCOUNT");  // Click ADD CITY Button

        solo.clickOnView(solo.getView(R.id.create_userBtn));

        solo.sleep(2000); // 2 pause
        solo.assertCurrentActivity("Wrong Activity", UserMenu.class);
        // go to

        // test logout functionaly
        solo.assertCurrentActivity("Wrong Activity", UserMenu.class);
        // click userprofile
        solo.clickInList(1); // click "user profile
        solo.sleep(2000); // 2 pause
        solo.assertCurrentActivity("Wrong Activity", PlayerProfile.class);
        solo.clickOnView(solo.getView(R.id.logout));

        solo.sleep(2000); // 2 pause
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);  // we back to login screen

    }

    @Test
    public void scanAQRcode() {
        // click scannerView
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("New User"); //Click ADD CITY Button
        solo.sleep(2000); // 2 pause

        // create a random user each time
        solo.assertCurrentActivity("Wrong Activity", NewUser.class);
        Random random = new Random();
        int randomint = random.nextInt(10000 - 0) + 0;
        String enterUsername = "AndroidTestUser" + Integer.toString(randomint);
        solo.enterText((EditText) solo.getView(R.id.username_text), enterUsername);
        solo.enterText((EditText) solo.getView(R.id.name_text), "1");
        solo.enterText((EditText) solo.getView(R.id.email_text), "1@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.city_region), "edmonton");
        //solo.clickOnButton("CREATE ACCOUNT");  // Click ADD CITY Button

        solo.clickOnView(solo.getView(R.id.create_userBtn));

        solo.sleep(2000); // 2 pause
        solo.assertCurrentActivity("Wrong Activity", UserMenu.class);

        // scan a code
        solo.clickInList(2); // click scan new code
        solo.sleep(10000); // 10s  pause to let user/emu scan a code
        solo.assertCurrentActivity("Wrong Activity", QRCodeEditor.class);  // assert we in editor

        // get the score textview
        String scoreTextV = solo.getText(1).getText().toString();

        solo.clickOnView(solo.getView(R.id.save));  // go back to main menu
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", UserMenu.class);  // assert we in menu

        solo.clickInList(3); // click myscan
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", MyScans.class);  // assert we in menu
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
