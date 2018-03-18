package nl.ru.hardware_security.feb2018.group7.ewallet;

/**
 * STATE MACHINE
 */

import javacard.framework.*;

class StateMachine {

	/**
	 * CONSTANTS
	 */
	final static byte INITIALIZED                = (byte) 0x10;
	final static byte SECURE_CHANNEL_ESTABLISHED = (byte) 0x20;
	final static byte TERMINAL_AUTHENTICATED     = (byte) 0x30;
	final static byte CARD_AUTHENTICATED         = (byte) 0x40;
	final static byte USER_AUTHENTICATED         = (byte) 0x50; //#!TODO unclear whether we need this
	
	final static byte MGMT_AUTHENTICATED          = (byte) 0x60; // #!TODO do we need this??

	// the only way to go back is to RST (kill session, create new session - that makes programming easier)
	final static byte END                        = (byte) 0x90;
	final static byte ERROR                      = (byte) 0x00;

	/**
	 * VARIABLES
	 */
	private static byte state = INITIALIZED;
	//private static LifeCyclePhaseMachine phaseMachine;

	private void StateMachine() {
		state = INITIALIZED;
	}

	static final byte getState() { // no modifier so only visible to package but not subclass/world
		return state;
	}

	static final boolean isAllowed(byte apduCommandAction) {
		byte phase = LifeCyclePhaseMachine.getPhase();
		
		switch (apduCommandAction) {
		case EWallet.BALANCE:
			// #!TODO implement
			break;
		case EWallet.PAY:
		// #!TODO implement
			break;
		case EWallet.CHARGE:
			if (phase == LifeCyclePhaseMachine.PERSONALIZED && state == CARD_AUTHENTICATED) {
				return true;
			}
			break;
		case EWallet.HISTORY:
			if ((phase == LifeCyclePhaseMachine.PERSONALIZED && state == USER_AUTHENTICATED) ||
				phase == LifeCyclePhaseMachine.LOCKED_PIN && state == MGMT_AUTHENTICATED) {
				return true;
			}
			break;
		default:
			return false;
		}
		return false;
	}

	final boolean changeStateInitialized() {
		// current state always allows transfer to new state
		state = INITIALIZED; // new state
		return true;
	}

	final boolean changeStateSecureChannelEstablished() {
		if (state == INITIALIZED) { // current state allows transfer to new state
			state = SECURE_CHANNEL_ESTABLISHED;
			return true;
		} else {
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // #!TODO: is that OK?
			return false;
		}
	}

	final boolean changeStateTerminalAuthenticated() {
		if (state == SECURE_CHANNEL_ESTABLISHED) { // current state allows transfer to new state
			state = TERMINAL_AUTHENTICATED;
			return true;
		} else {
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // #!TODO is that OK?
			return false;
		}
	}

	final boolean changeStateCardAuthenticated() {
		if (state == TERMINAL_AUTHENTICATED) { // current state allows transfer to new state
			state = CARD_AUTHENTICATED;
			return true;
		} else {
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // #!TODO is that OK?
			return false;
		}
	}

	final boolean changeStateUserAuthenticated() {
		if (state == CARD_AUTHENTICATED) { // current state allows transfer to new state
			state = USER_AUTHENTICATED;
			return true;
		} else {
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // #!TODO is that OK
			return false;
		}
	}

	final boolean changeStateEnd() {
		state = END; //always possible
		return true;
	} 

}
