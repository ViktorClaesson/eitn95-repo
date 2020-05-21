package task2;

import util.*;
import util.Signal.Type;

public class Student extends Proc {

    private static final double diagonalLength = Math.hypot(0.5, 0.5);

    enum Direction {
        N, NE, E, SE, S, SW, W, NW
    }

    private final int ID;
    private Square currentSquare;
    private final double moveStraightTime, moveDiagonallyTime;
    private Direction direction;
    private int squaresToMove = 0;
    private boolean state = true;
    private boolean isTalking = false;

    public static String studentKey(Student s1, Student s2) {
        return s1.ID < s2.ID ? String.format("(%d, %d)", s1.ID, s2.ID) : String.format("(%d, %d)", s2.ID, s1.ID);
    }

    public Student(int id, Square startSquare, double movingSpeed) {
        this.ID = id;
        this.currentSquare = startSquare;
        moveStraightTime = 0.5 / movingSpeed;
        moveDiagonallyTime = diagonalLength / movingSpeed;
        pickNewDirection();
        currentSquare.studentEnter(this);
    }

    public void pickNewDirection() {
        squaresToMove = (int) Global.uniRandom(0, 10) + 1;
        do {
            int key = (int) Global.uniRandom(0, 8);
            direction = Direction.values()[key];
        } while (currentSquare.next(direction) == null);
    }

    public boolean isTalking() {
        return isTalking;
    }

    public void startTalking(Student other) {
        isTalking = true;
        Global.SendSignal(Type.STOP_TALKING, this, 1);
    }

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case AT_MIDDLE:
                if (!isTalking) {
                    state = false;
                    if (squaresToMove == 0 || currentSquare.next(direction) == null) {
                        pickNewDirection();
                    }
                    switch (direction) {
                        case N:
                        case E:
                        case S:
                        case W:
                            Global.SendSignal(Type.AT_EDGE, this, moveStraightTime);
                            break;
                        case NE:
                        case SE:
                        case SW:
                        case NW:
                            Global.SendSignal(Type.AT_EDGE, this, moveDiagonallyTime);
                            break;
                    }
                    squaresToMove--;
                }
                break;
            case AT_EDGE:
                if (!isTalking) {
                    state = true;
                    currentSquare.studentLeave(this);
                    currentSquare = currentSquare.next(direction);
                    currentSquare.studentEnter(this);
                    switch (direction) {
                        case N:
                        case E:
                        case S:
                        case W:
                            Global.SendSignal(Type.AT_MIDDLE, this, moveStraightTime);
                            break;
                        case NE:
                        case SE:
                        case SW:
                        case NW:
                            Global.SendSignal(Type.AT_MIDDLE, this, moveDiagonallyTime);
                            break;
                    }
                }
                break;
            case STOP_TALKING:
                isTalking = false;
                Global.SendSignal(state ? Type.AT_MIDDLE : Type.AT_EDGE, this, 0);
                break;
            default:
                TypeNotImplemented();
                break;
        }
    }

}