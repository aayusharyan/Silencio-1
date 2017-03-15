package com.teamshunya.silencio.ShowActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.teamshunya.silencio.R;
import com.teamshunya.silencio.ShowActivity.Fragments.Arrival;
import com.teamshunya.silencio.ShowActivity.Fragments.Departure;
import com.teamshunya.silencio.ShowActivity.Fragments.Feedback;
import com.teamshunya.silencio.ShowActivity.Fragments.Profile;

public class ShowActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    CleverTapAPI cleverTap;
    final int[] flag = {0};//global varibale used for cheking flight no. is given or not
                            //0 = given and 1 = not-given
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        //open app
        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.bottom_menu);
        final View includedLayout = findViewById(R.id.cardBoarding);
        fragmentManager = getSupportFragmentManager();
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);

        showDialogBox();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_search:
                        includedLayout.setVisibility(View.VISIBLE);
                        fragment = new Arrival();
                        break;
                    case R.id.action_cart:
                        if (flag[0] == 1) {
                            //if user didnt give a flight no. at prompt then cardboarding departure will not be visible
                            includedLayout.setVisibility(View.GONE);
                        } else {
                            includedLayout.setVisibility(View.VISIBLE);
                        }
                        fragment = new Departure();
                        break;
                    case R.id.action_hot_deals:
                        if (flag[0] == 1) {
                            includedLayout.setVisibility(View.GONE);
                        } else {
                            includedLayout.setVisibility(View.VISIBLE);
                        }

                        fragment = new Feedback();
                        break;

                    case R.id.action_feedback:
                        includedLayout.setVisibility(View.GONE);
                        fragment = new Feedback();
                        break;
                    case R.id.action_profile:
                        includedLayout.setVisibility(View.GONE);
                        fragment = new Profile();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
        // bottomNavigation.setBackgroundColor(getResources().getColor(R.color.bb_darkBackgroundColor));


        try {
            cleverTap = CleverTapAPI.getInstance(getApplicationContext());
        } catch (CleverTapMetaDataNotFoundException e) {
            // thrown if you haven't specified your CleverTap Account ID or Token in your AndroidManifest.xml
        } catch (CleverTapPermissionsNotSatisfied e) {
            // thrown if you haven’t requested the required permissions in your AndroidManifest.xml
        }


        //back button
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    }


    private void showDialogBox() {

        final Context context = this;
        final String[] userInputFlightNumber = new String[1];
        final View includedLayout = findViewById(R.id.cardBoarding);


        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.fragment_dialog, null);
        //promptsView contains fragment_dialog.xml for infalting/setView into dialog

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(promptsView);    // set fragment_dialog.xml to alertdialog builder

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.UserFlightNumber);

        // set dialog message

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                userInputFlightNumber[0] = userInput.getText().toString().trim();
                                if (userInputFlightNumber[0].isEmpty()) {
                                    Toast.makeText(ShowActivity.this, "Please enter Flight Number!", Toast.LENGTH_LONG).show();
                                    //when empty edittext field is OKed then again dialog box should open
                                    showDialogBox();    //againcal the same method is called

                                } else if (userInputFlightNumber[0].length() == 5) {
                                    //here we'll add the condtition for valid flight no!
                                    //from firebase
                                    Toast.makeText(ShowActivity.this, "" + userInputFlightNumber[0], Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ShowActivity.this, "Invalid flight Number! Try again!", Toast.LENGTH_LONG).show();
                                    showDialogBox();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                flag[0] = 1;
                                //means if flag[0] = 1 on departure fragment cardboarding will not be visible!
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//feedback
            case R.id.menu_bookmark:


        }
        return true;
    }


}
