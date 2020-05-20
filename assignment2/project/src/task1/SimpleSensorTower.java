package task1;

import sim.*;

class SimpleSensorTower extends Proc {
    private int ts, tp;
    private Gateway gateway;

    public SimpleSensorTower(Gateway gateway, int ts, int tp) {
        this.gateway = gateway;
        this.ts = ts;
        this.tp = tp;
    }

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case BEGIN_TRANSMISSION:
                Data.transmissions++;
                if (gateway != null) {
                    Global.SendSignal(Signal.Type.BEGIN_TRANSMISSION, gateway, 0);
                }
                Global.SendSignal(Signal.Type.END_TRANSMISSION, this, tp);
                break;
            case END_TRANSMISSION:
                if (gateway != null) {
                    Global.SendSignal(Signal.Type.END_TRANSMISSION, gateway, 0);
                }
                Global.SendSignal(Signal.Type.BEGIN_TRANSMISSION, this, Global.expRandom(ts));
                break;
            default:
                TypeNotImplemented();
                break;
        }
    }
}