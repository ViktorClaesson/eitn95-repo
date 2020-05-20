package task1;

import sim.*;
import java.util.List;

class SmartSensorTower extends SignalTreater {
    private int ts, tp;
    private double lb, ub;
    private Gateway gateway;
    private List<SmartSensorTower> others;

    public boolean isTransmitting = false;

    public SmartSensorTower(Gateway gateway, int ts, int tp, double lb, double ub) {
        this.gateway = gateway;
        this.ts = ts;
        this.tp = tp;
        this.lb = lb;
        this.ub = ub;
    }

    public void addTowersWithinRadius(List<SmartSensorTower> others) {
        this.others = others;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case CHECK_NETWORK:
                double sleep = others.stream().anyMatch(sst -> sst.isTransmitting) ? Global.uniRandom(lb, ub) : 0;
                Global.SendSignal(Signal.Type.BEGIN_TRANSMISSION, this, sleep);
                break;
            case BEGIN_TRANSMISSION:
                isTransmitting = true;
                Data.transmissions++;
                if (gateway != null) {
                    Global.SendSignal(Signal.Type.BEGIN_TRANSMISSION, gateway, 0);
                }
                Global.SendSignal(Signal.Type.END_TRANSMISSION, this, tp);
                break;
            case END_TRANSMISSION:
                isTransmitting = false;
                if (gateway != null) {
                    Global.SendSignal(Signal.Type.END_TRANSMISSION, gateway, 0);
                }
                Global.SendSignal(Signal.Type.CHECK_NETWORK, this, Global.expRandom(ts));
                break;
            default:
                TypeNotImplemented();
                break;
        }
    }
}