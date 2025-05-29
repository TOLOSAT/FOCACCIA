package model.infos;
public class CallStack {
    private Call[] calls;

    public CallStack(Integer lastIndex, Call[] calls) {
        this.calls = new Call[lastIndex];
        for (int i = 0; i < lastIndex; i++) {
            this.calls[i] = calls[i];
        }
    }

    public Call[] calls() {
        return calls;
    }
}
