package application.internal;

/**
 * Created by Lukas Pihl
 */
public class GridProfile {
    private String myName;
    private int myNumber;
    private boolean myHorizontal;
    private boolean myVertical;
    private boolean myDirection;

    public GridProfile(String name, int num, boolean hor, boolean ver, boolean dir) {
        myName = name;
        myNumber = num;
        myHorizontal = hor;
        myVertical = ver;
        myDirection = dir;
    }

    public void setAll(String name, int num, boolean hor, boolean ver, boolean dir) {
        myName = name;
        myNumber = num;
        myHorizontal = hor;
        myVertical = ver;
        myDirection = dir;
    }

    public String getName() {
        return myName;
    }

    public int getNumber() {
        return myNumber;
    }

    public boolean getHorizontal() {
        return myHorizontal;
    }

    public boolean getVertical() {
        return myVertical;
    }

    public boolean getDirection() {
        return myDirection;
    }
}
