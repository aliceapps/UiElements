package com.aliceapps.uielements;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.ViewInteraction;

import com.aliceapps.uielements.databinding.FragmentTestBinding;
import com.aliceapps.uielements.spinners.ImageSpinnerView;
import com.aliceapps.uielements.spinners.SimpleSpinnerView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = {28})
public class ImageSpinnerViewTest {
    private ViewInteraction field;
    private Random random;
    private CharSequence[] values;
    private CharSequence[] labels;
    private TestObject object;

    @Before
    public void setUp() {
        random = new Random();
        //Create fragment factory
        TestFactory factory = new TestFactory();
        FragmentScenario<TestFragment> scenario =
                FragmentScenario.launchInContainer(TestFragment.class, null, R.style.Theme_AppCompat,
                        factory);


        scenario.onFragment((FragmentScenario.FragmentAction<TestFragment>) fragment -> {
            FragmentTestBinding binding = fragment.getBinding();
            ImageSpinnerView view = binding.imageSpinner;
            labels = view.getEntries();
            values = view.getValues();
            field = onView(withId(binding.imageSpinner.getId()));
            field.perform(scrollTo());
            object = fragment.getTestObject();
        });
    }

    @Test
    public void selectItem() {
        //prepare data
        int position = random.nextInt(values.length);
        //click on field
        field.perform(click());
        //select value
        onView(allOf(withId(R.id.spinner_text), withText((String) labels[position])))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.spinner_text), withText((String) labels[position])))
                .perform(scrollTo(), click());
        //Check value is selected
        Assert.assertEquals(values[position], object.getTestStringImage());
    }
}
