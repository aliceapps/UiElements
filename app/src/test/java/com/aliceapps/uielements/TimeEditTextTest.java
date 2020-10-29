package com.aliceapps.uielements;

import android.widget.TimePicker;

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
import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = {28})
public class TimeEditTextTest {
    private DateFormat sdf;
    private ViewInteraction dateField;
    private Random random;

    @Before
    public void setUp() {
        random = new Random();
        //Create fragment factory
        TestFactory factory = new TestFactory();
        FragmentScenario<TestFragment> scenario = FragmentScenario.launchInContainer(TestFragment.class, null, R.style.Theme_AppCompat, factory);
        sdf = DateFormat.getTimeInstance(DateFormat.SHORT);

        scenario.onFragment((FragmentScenario.FragmentAction<TestFragment>) fragment -> {
            FragmentTestBinding binding = ((TestFragment) fragment).getBinding();
            dateField = onView(withId(binding.TimeEditText.getId()));
            dateField.perform(scrollTo());
        });
    }

    @Test
    public void showTimePicker() {
        //Click on date field
        dateField.perform(click());
        //check dialog is visible
        ViewInteraction datePicker = onView(withClassName(equalTo(TimePicker.class.getName()))).inRoot(RootMatchers.isDialog());
        datePicker.check(matches(isDisplayed()));
    }

    @Test
    public void loadDatePickerListener() {
        //Click on date field
        int hour = random.nextInt(24);
        int minutes = random.nextInt(60);
        dateField.perform(click());
        //select date
        ViewInteraction datePicker = onView(withClassName(equalTo(TimePicker.class.getName()))).inRoot(RootMatchers.isDialog());
        ViewInteraction okButton = onView(withId(android.R.id.button1)).inRoot(RootMatchers.isDialog());
        datePicker.perform(PickerActions.setTime(hour, minutes));
        okButton.perform(click());
        //check date is filled
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        String text = sdf.format(calendar.getTime());
        dateField.check(matches(withText(text)));
    }

    @Test
    public void secondSelectionTest() {
        //Click on date field
        int hour = random.nextInt(24);
        int minutes = random.nextInt(60);
        dateField.perform(click());
        //select date
        ViewInteraction datePicker = onView(withClassName(equalTo(TimePicker.class.getName()))).inRoot(RootMatchers.isDialog());
        ViewInteraction okButton = onView(withId(android.R.id.button1)).inRoot(RootMatchers.isDialog());
        datePicker.perform(PickerActions.setTime(hour, minutes));
        okButton.perform(click());
        //click on date field again
        dateField.perform(click());
        okButton.perform(click());
        //check date is still the same
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        String text = sdf.format(calendar.getTime());
        dateField.check(matches(withText(text)));
    }
}
