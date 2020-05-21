package task1;

import util.*;

class Gateway extends Proc {
    private int transsions = 0;
    private boolean interference = false;

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case BEGIN_TRANSMISSION:
                transsions++;
                interference = transsions > 1;
                break;
            case END_TRANSMISSION:
                if (transsions == 1 && !interference) {
                    Data.successful_transmissions++;
                }
                interference = transsions > 1;
                transsions--;
                break;
            default:
                TypeNotImplemented();
                break;
        }
    }
}