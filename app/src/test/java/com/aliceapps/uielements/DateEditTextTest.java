package com.aliceapps.uielements;

import android.widget.DatePicker;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;

import com.aliceapps.uielements.databinding.FragmentTestBinding;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.text.DateFormat;
import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = {28})
public class DateEditTextTest {
    private DateFormat sdf;
    private FragmentTestBinding binding;

    @Before
    public void setUp() {
        //Create fragment factory
        TestFactory factory = new TestFactory();
        FragmentScenario<TestFragment> scenario = FragmentScenario.launchInContainer(TestFragment.class, null, R.style.Theme_AppCompat, factory);
        sdf = DateFormat.getDateInstance(DateFormat.SHORT);

        scenario.onFragment(fragment -> binding = ((TestFragment) fragment).getBinding());
    }

    @Test
    public void showDatePicker() {
        //Click on date field
        ViewInteraction dateField = onView(withId(binding.DateEditText.getId()));
        dateField.perform(click());
        //check dialog is visible
        ViewInteraction datePicker = onView(withClassName(equalTo(DatePicker.class.getName()))).inRoot(RootMatchers.isDialog());
        datePicker.check(matches(isDisplayed()));
    }

    @Test
    public void loadDatePickerListener() {
        //Click on date field
        int year = 2020;
        int month = 2;
        int day = 21;
        ViewInteraction dateField = onView(withId(binding.DateEditText.getId()));
        dateField.perform(click());
        //select date
        ViewInteraction datePicker = onView(withClassName(equalTo(DatePicker.class.getName()))).inRoot(RootMatchers.isDialog());
        ViewInteraction okButton = onView(withId(android.R.id.button1)).inRoot(RootMatchers.isDialog());
        datePicker.perform(PickerActions.setDate(year, month, day));
        okButton.perform(click());
        //check date is filled
        Calendar c = Calendar.getInstance(); // Get Calendar Instance
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        String text = sdf.format(c.getTime());
        dateField.check(matches(withText(text)));
    }

    @Test
    public void secondSelectionTest() {
        //Click on date field
        int year = 2020;
        int month = 2;
        int day = 21;
        ViewInteraction dateField = onView(withId(binding.DateEditText.getId()));
        dateField.perform(click());
        //select date
        ViewInteraction datePicker = onView(withClassName(equalTo(DatePicker.class.getName()))).inRoot(RootMatchers.isDialog());
        ViewInteraction okButton = onView(withId(android.R.id.button1)).inRoot(RootMatchers.isDialog());
        datePicker.perform(PickerActions.setDate(year, month, day));
        okButton.perform(click());
        //click on date field again
        dateField.perform(click());
        okButton.perform(click());
        //check date is still the same
        Calendar c = Calendar.getInstance(); // Get Calendar Instance
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        String text = sdf.format(c.getTime());
        dateField.check(matches(withText(text)));
    }
}
