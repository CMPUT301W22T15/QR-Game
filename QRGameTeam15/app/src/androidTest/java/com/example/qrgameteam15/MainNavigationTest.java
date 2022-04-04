package com.example.qrgameteam15;

import android.app.Activity;
import android.view.KeyEvent;
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

import java.util.List;
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

    /**
     * scan a qrcode, view myscan, check it is the qrcode we just scan, delete it, check that it's
     * been deleted
     */
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
        QRCodeEditor qrCodeEditorA = (QRCodeEditor) solo.getCurrentActivity();

        String scoreTextVofScannedCode = ((TextView)(qrCodeEditorA.findViewById(R.id.score))).getText().toString();

        solo.clickOnView(solo.getView(R.id.save));  // go back to main menu
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", UserMenu.class);  // assert we in menu

        solo.clickInList(3); // click myscan
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", MyScans.class);  // assert we in myscan

        // we check if the score value is the same of the qrcode we just clicked.
        // go to the 1st qrcode in myscans
        ListView qrcodeList = (ListView) solo.getView(R.id.scan_list);
        View firstItem = qrcodeList.getChildAt(0);  //
        solo.clickOnView(firstItem);
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", ViewQRCode.class);  // assert we in Viewqrcode

        ViewQRCode  viewQRCode = (ViewQRCode) solo.getCurrentActivity();
        TextView viewQRscoreText = viewQRCode.findViewById(R.id.score_text);
        String viewQRscoreTextStr = viewQRscoreText.getText().toString();

        // test if the qrcode we just scan matches the one display in the myscan

        assertEquals(scoreTextVofScannedCode,  viewQRscoreTextStr);

        // and delete qrcode?
        viewQRCode.finish();  // go back to myscan
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", MyScans.class);  // assert we in myscan


        // delete the qrocde we just scanned
        qrcodeList = (ListView) solo.getView(R.id.scan_list);
        firstItem = qrcodeList.getChildAt(0);  // we wa
        solo.clickLongOnView(firstItem);
        solo.sleep(2000);
        solo.clickOnView(solo.getView(android.R.id.button1));  // click ok button to delete
        solo.sleep(2000);
        // check that the listview is empty
        qrcodeList = (ListView) solo.getView(R.id.scan_list);
        int listviewsize = qrcodeList.getAdapter().getCount();
        assertEquals(0, listviewsize);  // assert we have 0 qrcode in myscan

    }

    @Test
    public void testOtherPlayer() {
        // test other player
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
