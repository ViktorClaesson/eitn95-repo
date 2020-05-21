package task2;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import sim.*;
import sim.Signal.Type;

public class Student extends Proc {

    private static final double diagonalLength = Math.hypot(0.5, 0.5);

    enum Direction {
        N, NE, E, SE, S, SW, W, NW
    }

    private BiFunction<Square, Direction, Square> changeSquare;
    private BiPredicate<Square, Direction> isAtWall;
    private Square currentSquare;
    private final double moveStraightTime, moveDiagonallyTime;
    private Direction direction;
    private int squaresToMove = 0;
    private boolean atEdge = false;
    private boolean isTalking = false;

    public Student(Square startSquare, BiFunction<Square, Direction, Square> changeSquare, double movingSpeed) {
        this.currentSquare = startSquare;
        this.changeSquare = changeSquare;
        moveStraightTime = 0.5 / movingSpeed;
        moveDiagonallyTime = diagonalLength / movingSpeed;
    }

    public void pickNewDirection() {
        squaresToMove = (int) Global.uniRandom(0, 10) + 1;
        int key = (int) Global.uniRandom(0, 8);
        direction = Direction.values()[key];
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
                    if (isAtWall.test(currentSquare, direction)) {
                        squaresToMove = 0;
                    }
                    if (squaresToMove == 0) {
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
                    currentSquare = changeSquare.apply(currentSquare, direction);
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