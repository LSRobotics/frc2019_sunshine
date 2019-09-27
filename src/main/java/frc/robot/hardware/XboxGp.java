package frc.robot.hardware;

import java.util.Arrays;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.software.*;

final public class XboxGp extends XboxController {

    final private static int NUM_KEYS = 20;
    private Timer p = new Timer("Gamepad");
    private boolean isDebug = false;

    public enum Key {
        J_LEFT_X, J_LEFT_Y, J_RIGHT_X, J_RIGHT_Y, LT, RT, J_LEFT_DOWN, J_RIGHT_DOWN, A, B, X, Y, LB, RB, BACK, START,
        DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT
    }

    public boolean[] states = new boolean[NUM_KEYS];
    public double[] values = new double[NUM_KEYS];
    final private static Key key_index[] = Key.values();

    public XboxGp(int xboxPort) {
        super(xboxPort);
        // Initialize Arrays
        Arrays.fill(states, false);
        Arrays.fill(values, 0);
    }

    public void setDebugMode(boolean isDebugMode) {
        isDebug = isDebugMode;
    }

    public double getValue(Key key) {
        return values[key.ordinal()];
    }

    public double getRawReading(Key key) {

        int pov = getPOV();

        switch (key) {
        case J_LEFT_X:
            return getX(Hand.kLeft);
        case J_LEFT_Y:
            return getY(Hand.kLeft);
        case J_RIGHT_X:
            return getX(Hand.kRight);
        case J_RIGHT_Y:
            return getY(Hand.kRight);
        case LT:
            return getTriggerAxis(Hand.kLeft);
        case RT:
            return getTriggerAxis(Hand.kRight);
        case A:
            return getAButton() ? 1 : 0;
        case B:
            return getBButton() ? 1 : 0;
        case X:
            return getXButton() ? 1 : 0;
        case Y:
            return getYButton() ? 1 : 0;
        case LB:
            return getBumper(Hand.kLeft) ? 1 : 0;
        case RB:
            return getBumper(Hand.kRight) ? 1 : 0;
        case BACK:
            return getBackButton() ? 1 : 0;
        case START:
            return getStartButton() ? 1 : 0;
        case J_LEFT_DOWN:
            return getStickButton(Hand.kLeft) ? 1 : 0;
        case J_RIGHT_DOWN:
            return getStickButton(Hand.kRight) ? 1 : 0;
        case DPAD_RIGHT:
            return pov < 0 ? 0 : ((pov >= 45 && pov <= 135) ? 1 : 0);
        case DPAD_DOWN:
            return pov < 0 ? 0 : ((pov >= 135 && pov <= 225) ? 1 : 0);
        case DPAD_LEFT:
            return pov < 0 ? 0 : ((pov >= 225 && pov <= 315) ? 1 : 0);
        case DPAD_UP:
            return pov < 0 ? 0 : ((pov == 315 && pov <= 45) ? 1 : 0);
        default:
            return 0;
        }
    }

    public boolean isKeyChanged(Key key) {
        return states[key.ordinal()];
    }

    public boolean isKeyHeld(Key key) {
        return getValue(key) > 0;
    }

    public boolean isGamepadChanged() {
        for (boolean i : states) {
            if (i) {
                return true;
            }
        }
        return false;
    }

    public boolean isKeyToggled(Key key) {
        return isKeyChanged(key) && (getValue(key) > 0);
    }

    public void fetchData() {

        if (isDebug)
            p.start();

        for (int i = 0; i < NUM_KEYS; ++i) {

            double tempVal = getRawReading(key_index[i]);

            if (tempVal != values[i]) {
                states[i] = true;
                values[i] = tempVal;
            } else {
                states[i] = false;
            }
        }

        if (isDebug) {
            p.stop();
            RobotUtil.report(p.toString());
        }
    }
}
