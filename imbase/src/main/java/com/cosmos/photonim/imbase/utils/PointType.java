package com.cosmos.photonim.imbase.utils;

/**
 * Created by fanqiang on 2019/3/28.
 */
public class PointType {
    public static final int TYPE_NULL = 0;
    public static final int TYPE_MOVE = 1;
    public static final int TYPE_LINE = 2;
    public static final int TYPE_CUBIC = 3;
    private int type;
    private float x;
    private float y;

    private float fristXControl, fristYControl, secondXControl,
            secondYControl;

    public static PointType createMoveType() {
        return new PointType(TYPE_MOVE);
    }

    public static PointType createLineType() {
        return new PointType(TYPE_LINE);
    }

    public static PointType createCubicType() {
        return new PointType(TYPE_CUBIC);
    }

    public PointType() {
        this.type = TYPE_NULL;
    }

    public PointType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public float getFristXControl() {
        return fristXControl;
    }

    public void setFristXControl(float fristXControl) {
        this.fristXControl = fristXControl;
    }

    public float getFristYControl() {
        return fristYControl;
    }

    public void setFristYControl(float fristYControl) {
        this.fristYControl = fristYControl;
    }

    public float getSecondXControl() {
        return secondXControl;
    }

    public void setSecondXControl(float secondXControl) {
        this.secondXControl = secondXControl;
    }

    public float getSecondYControl() {
        return secondYControl;
    }

    public void setSecondYControl(float secondYControl) {
        this.secondYControl = secondYControl;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    @Override
    public String toString() {
        return "PointType{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", fristXControl=" + fristXControl +
                ", fristYControl=" + fristYControl +
                ", secondXControl=" + secondXControl +
                ", secondYControl=" + secondYControl +
                '}';
    }
}
