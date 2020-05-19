package task1;

class SimpleSensorTower extends Proc {
    private int ts, tp;
    private Gateway gateway;

    public SimpleSensorTower(Gateway gateway, int ts, int tp) {
        this.gateway = gateway;
        this.ts = ts;
        this.tp = tp;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case BEGIN_TRANSMISSION:
                state.transmissions++;
                if (gateway != null) {
                    SignalList.SendSignal(BEGIN_TRANSMISSION, gateway, time);
                }
                SignalList.SendSignal(END_TRANSMISSION, this, time + tp);
                break;
            case END_TRANSMISSION:
                if (gateway != null) {
                    SignalList.SendSignal(END_TRANSMISSION, gateway, time);
                }
                SignalList.SendSignal(BEGIN_TRANSMISSION, this, time + expRandom(ts));
                break;
        }
    }
}