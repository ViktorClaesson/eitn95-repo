package task1;

import java.util.List;

class SmartSensorTower extends Proc {
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
                double sleep = others.stream().anyMatch(sst -> sst.isTransmitting) ? uniRandom(lb, ub) : 0;
                SignalList.SendSignal(BEGIN_TRANSMISSION, this, time + sleep);
                break;
            case BEGIN_TRANSMISSION:
                isTransmitting = true;
                state.transmissions++;
                if (gateway != null) {
                    SignalList.SendSignal(BEGIN_TRANSMISSION, gateway, time);
                }
                SignalList.SendSignal(END_TRANSMISSION, this, time + tp);
                break;
            case END_TRANSMISSION:
                isTransmitting = false;
                if (gateway != null) {
                    SignalList.SendSignal(END_TRANSMISSION, gateway, time);
                }
                SignalList.SendSignal(CHECK_NETWORK, this, time + expRandom(ts));
                break;
        }
    }
}