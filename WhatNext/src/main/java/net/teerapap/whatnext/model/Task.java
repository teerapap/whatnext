package net.teerapap.whatnext.model;

/**
 * A task data model.
 * Created by teerapap on 10/25/13.
 */
public class Task implements When {

    private long mId;
    private String mTitle;
    private When mWhen;

    public Task(String title, When condition) {
        mTitle = title;
        mWhen = condition;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public boolean isAtHome() {
        return this.mWhen.isAtHome();
    }

    @Override
    public boolean isFreeTime() {
        return this.mWhen.isFreeTime();
    }

    @Override
    public boolean isAtWork() {
        return this.mWhen.isAtWork();
    }

    @Override
    public boolean isShopping() {
        return this.mWhen.isShopping();
    }
}
