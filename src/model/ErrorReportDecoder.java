package model;
import java.io.FileInputStream;

import model.infos.Call;
import model.infos.CallStack;
import model.infos.Context;
import model.infos.SavedRegisters;
import model.infos.SoftwareState;
import model.infos.SoftwareVersion;

public class ErrorReportDecoder {
    private static byte[] buffer = new byte[4];
    private String errorReportPath;

    private static Integer intFromBytes(byte[] bytes) {
        return (
            ((bytes[3] << 24) & 0xff000000)
            | ((bytes[2] << 16) & 0x00ff0000)
            | ((bytes[1] << 8) & 0x0000ff00)
            | (bytes[0] & 0x000000ff)
        );
    }

    public ErrorReportDecoder(String errorReportPath) {
        this.errorReportPath = errorReportPath;
    }

    public ErrorReport decodeErrorReport() {
        try {
            FileInputStream reportFile = new FileInputStream(errorReportPath);

            SoftwareVersion version = new SoftwareVersion(
                reportFile.read(),
                reportFile.read(),
                reportFile.read(),
                reportFile.read()
            );

            SoftwareState state = SoftwareState.values()[reportFile.read()];

            reportFile.read(buffer);
            Integer bootCount = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer failedBootCount = intFromBytes(buffer);

            Context context = new Context(version, state, bootCount, failedBootCount);

            reportFile.read(buffer);
            Integer r0 = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer r1 = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer r2 = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer r3 = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer r12 = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer lr = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer pc = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer xpsr = intFromBytes(buffer);

            SavedRegisters savedRegisters = new SavedRegisters(r0, r1, r2, r3, r12, lr, pc, xpsr);

            reportFile.read(buffer);
            Integer cfsr = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer hfsr = intFromBytes(buffer);

            reportFile.read(buffer);
            Integer lastIndex = intFromBytes(buffer) + 1;

            Call[] callStackArray = new Call[lastIndex + 1];
            for (int i = 0; i <= lastIndex; i++) {
                reportFile.read(buffer);
                Integer clr = intFromBytes(buffer);
                reportFile.read(buffer);
                Integer cfp = intFromBytes(buffer);
                callStackArray[i] = new Call(clr, cfp);
            }

            CallStack callStack = new CallStack(lastIndex, callStackArray);

            ErrorReport errorReport = new ErrorReport(context, savedRegisters, cfsr, hfsr, callStack);

            // System.out.println("Error Report:");
            // System.out.println("  - Version: " + errorReport.getContext().getVersion().toString());
            // System.out.println("  - State: " + errorReport.getContext().getState());
            // System.out.println("  - Boot Count: " + errorReport.getContext().getBootCount());
            // System.out.println("  - Failed Boot Count: " + errorReport.getContext().getFailedBootCount());
            // System.out.println("  - R0: 0x" + Integer.toHexString(errorReport.getSavedRegisters().r()[0]));
            // System.out.println("  - R1: 0x" + Integer.toHexString(errorReport.getSavedRegisters().r()[1]));
            // System.out.println("  - R2: 0x" + Integer.toHexString(errorReport.getSavedRegisters().r()[2]));
            // System.out.println("  - R3: 0x" + Integer.toHexString(errorReport.getSavedRegisters().r()[3]));
            // System.out.println("  - R12: 0x" + Integer.toHexString(errorReport.getSavedRegisters().r12()));
            // System.out.println("  - LR: 0x" + Integer.toHexString(errorReport.getSavedRegisters().lr()));
            // System.out.println("  - PC: 0x" + Integer.toHexString(errorReport.getSavedRegisters().pc()));
            // System.out.println("  - xPSR: 0x" + Integer.toHexString(errorReport.getSavedRegisters().xpsr()));
            // System.out.println("  - CFSR: 0x" + Integer.toHexString(errorReport.cfsr()));
            // System.out.println("  - HFSR: 0x" + Integer.toHexString(errorReport.hfsr()));
            // System.out.println("  - Call Stack:");
            // for (Call call : errorReport.getCallStack().calls()) {
            //     System.out.println("    - CLR: 0x" + Integer.toHexString(call.lr()));
            //     System.out.println("    - CFP: 0x" + Integer.toHexString(call.fp()));
            // }
            reportFile.close();

            return errorReport;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }
}
