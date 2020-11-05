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

    private CustomRunnable() {
        generateId();
    }

    private CustomRunnable(Builder builder) {
        repeated = builder.repeated;
        runnable = builder.runnable;
        delayTime = builder.delayTime;
        canceled = builder.canceled;
    }

    private void generateId() {
        id = HandlerWhatCreator.getInstance().getNextId();
    }

    public boolean isRepeated() {
        return repeated;
    }

    public Runnable getRunnable() {
        return runnable;
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

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void setDelayTime(int timeOut) {
        this.delayTime = timeOut;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }


    public static final class Builder {
        private boolean repeated;
        private Runnable runnable;
        private long delayTime;
        private boolean canceled;

        public Builder() {
        }

        public Builder repeated(boolean val) {
            repeated = val;
            return this;
        }

        public Builder runnable(Runnable val) {
            runnable = val;
            return this;
        }

        public Builder delayTime(long val) {
            delayTime = val;
            return this;
        }

        public Builder canceled(boolean val) {
            canceled = val;
            return this;
        }

        public CustomRunnable build() {
            return new CustomRunnable(this);
        }
    }
}
