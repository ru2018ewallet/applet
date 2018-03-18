package nl.ru.hardware_security.feb2018.group7.ewallet;


/**
 * LIFECYCLE PHASES (second state machine)
 */

import javacard.framework.*;

class LifeCyclePhaseMachine {
	/**
	 * CONSTANTS
	 */
	final static byte ERROR        = (byte) 0x00;
	final static byte INITIALIZED  = (byte) 0x10;
	final static byte PERSONALIZED = (byte) 0x20;
	final static byte LOCKED_PIN   = (byte) 0x30;
	final static byte END_OF_LIFE  = (byte) 0x90;

	/**
	 * VARIABLES
	 */
	private static byte phase = INITIALIZED;


	private void LifeCyclePhaseMachine() {
		phase = INITIALIZED;
	}


	static final byte getPhase() { // no modifier so only visible to package but not subclass/world
		return phase;
	}

	private final boolean changePhasePersonalized() { // #!TODO security check!
		if (phase == INITIALIZED) {
			phase = PERSONALIZED;
			return true;
		} else {
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // !TODO
			return false;
		}
	}

	private final boolean changePhaseLockPin() { // #!TODO security check!
		// #!TODO tis is a security relevant operation: Card skimming!!
		if (phase == PERSONALIZED) {
			phase = LOCKED_PIN;
			return true;
		}
		return false;
	}

	private final boolean changePhaseEndOfLife() { // #!TODO security check!
		// all states allows transfer to EOL
		// #!TODO if state machine says we are at a mgmt terminal and authenticated
		// !TODO make sure keys are deleted!
		phase = END_OF_LIFE;
		return true;
	}
}
