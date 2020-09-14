package com.aliceapps.uielements.utility.di;

public class DaggerWrapper {
    private static MainComponent mComponent;

    public static MainComponent getComponent() {
        if (mComponent == null) {
            initComponent();
        }
        return mComponent;
    }

    private static void initComponent () {
        mComponent = DaggerMainComponent
                .builder()
                .moduleUtil(new ModuleUtil())
                .build();
    }

}
