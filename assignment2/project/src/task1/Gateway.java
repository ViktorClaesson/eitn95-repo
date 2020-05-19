package task1;

import java.util.*;
import java.util.stream.*;

class Gateway extends Proc {
    private LinkedList<Boolean> currentRec = new LinkedList<>();

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case BEGIN_TRANSMISSION:
                if (currentRec.size() == 0) {
                    currentRec.add(true);
                } else {
                    currentRec = currentRec.stream().map(b -> false).collect(Collectors.toCollection(LinkedList::new));
                    currentRec.add(false);
                }
                break;
            case END_TRANSMISSION:
                if (currentRec.pop()) {
                    state.successful_transmissions++;
                }
                break;
        }
    }
}