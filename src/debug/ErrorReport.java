package debug;
import debug.infos.CallStack;
import debug.infos.Context;
import debug.infos.SavedRegisters;

public class ErrorReport {
    private Context context;
    private SavedRegisters savedRegisters;
    private Integer cfsr;
    private Integer hfsr;
    private CallStack callStack;

    public ErrorReport(Context context, SavedRegisters savedRegisters, Integer cfsr, Integer hfsr, CallStack callStack) {
        this.context = context;
        this.savedRegisters = savedRegisters;
        this.cfsr = cfsr;
        this.hfsr = hfsr;
        this.callStack = callStack;
    }

    public Context getContext() {
        return context;
    }

    public SavedRegisters getSavedRegisters() {
        return savedRegisters;
    }

    public Integer cfsr() {
        return cfsr;
    }

    public Integer hfsr() {
        return hfsr;
    }

    public CallStack getCallStack() {
        return callStack;
    }
}
