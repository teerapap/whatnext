package net.teerapap.whatnext.model;

/**
 * Created by teerapap on 10/26/13.
 */
public class WhenCondition implements When {

    private boolean mAtHome;
    private boolean mFreeTime;
    private boolean mAtWork;
    private boolean mShopping;

    public WhenCondition() {
    }

    @Override
    public boolean isAtHome() {
        return mAtHome;
    }

    public WhenCondition atHome(boolean atHome) {
        mAtHome = atHome;
        return this;
    }

    @Override
    public boolean isFreeTime() {
        return mFreeTime;
    }

    public WhenCondition freeTime(boolean freeTime) {
        mFreeTime = freeTime;
        return this;
    }

    @Override
    public boolean isAtWork() {
        return mAtWork;
    }

    public WhenCondition atWork(boolean atWork) {
        mAtWork = atWork;
        return this;
    }

    @Override
    public boolean isShopping() {
        return mShopping;
    }

    public WhenCondition shopping(boolean shopping) {
        mShopping = shopping;
        return this;
    }
}
