package com.cosmos.photonim.imbase.utils.looperexecute;

/**
 * Created by fanqiang on 2018/12/11.
 */
public class CustomRunnable {
    public static final long NO_DELAY = -1l;
    public static final int ID_ILLEGAL = -1;
    private boolean repeated = false;
    private Runnable runnable;
    private int id = 0;
    private long delayTime = NO_DELAY;
    private boolean canceled = false;

    public CustomRunnable() {
        generateId();
    }

    private void generateId() {
        id = HandlerWhatCreator.getInstance().getNextId();
    }

    public boolean isRepeated() {
        return repeated;
    }

    public CustomRunnable setRepeated(boolean repeated) {
        this.repeated = repeated;
        return this;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public CustomRunnable setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomRunnable) {
            if (((CustomRunnable) obj).id == this.id) {
                return true;
            } else {
                return false;
            }
        }
        return super.equals(obj);
    }

    public long getDelayTime() {
        return delayTime;
    }

    public CustomRunnable setDelayTime(long delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public CustomRunnable setCanceled(boolean canceled) {
        this.canceled = canceled;
        return this;
    }
}
