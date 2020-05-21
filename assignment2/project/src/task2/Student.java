package task2;

import sim.*;
import sim.Signal.Type;

public class Student extends Proc {

    private static final double diagonalLength = Math.hypot(0.5, 0.5);

    enum Direction {
        N, NE, E, SE, S, SW, W, NW
    }

    private Square currentSquare;
    private final double moveStraightTime, moveDiagonallyTime;
    private Direction direction;
    private int squaresToMove = 0;
    private boolean atEdge = true;
    private boolean isTalking = false;

    public Student(Square startSquare, double movingSpeed) {
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

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case MOVE_TO_EDGE:
                if (!isTalking) {
                    atEdge = false;
                    if (squaresToMove == 0 || currentSquare.next(direction) == null) {
                        pickNewDirection();
                    }
                    switch (direction) {
                        case N:
                        case E:
                        case S:
                        case W:
                            Global.SendSignal(Type.MOVE_TO_MIDDLE, this, moveStraightTime);
                            break;
                        case NE:
                        case SE:
                        case SW:
                        case NW:
                            Global.SendSignal(Type.MOVE_TO_MIDDLE, this, moveDiagonallyTime);
                            break;
                    }
                    squaresToMove--;
                }
                break;
            case MOVE_TO_MIDDLE:
                if (!isTalking) {
                    atEdge = true;
                    currentSquare.studentLeave(this);
                    System.out.printf("%s : %s -> ", currentSquare, direction);
                    currentSquare = currentSquare.next(direction);
                    System.out.println(currentSquare);
                    currentSquare.studentEnter(this);
                    switch (direction) {
                        case N:
                        case E:
                        case S:
                        case W:
                            Global.SendSignal(Type.MOVE_TO_EDGE, this, moveStraightTime);
                            break;
                        case NE:
                        case SE:
                        case SW:
                        case NW:
                            Global.SendSignal(Type.MOVE_TO_EDGE, this, moveDiagonallyTime);
                            break;
                    }
                }
                break;
            case START_TALKING:
                isTalking = true;
                Global.SendSignal(Type.STOP_TALKING, this, 1);
                break;
            case STOP_TALKING:
                isTalking = false;
                Global.SendSignal(atEdge ? Type.MOVE_TO_EDGE : Type.MOVE_TO_MIDDLE, this, 0);
                break;
            default:
                TypeNotImplemented();
                break;
        }
    }

}