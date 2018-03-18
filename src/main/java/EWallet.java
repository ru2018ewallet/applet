package nl.ru.hardware_security.feb2018.group7.ewallet;

import javacard.framework.*;

public class EWallet extends Applet {
	
	// #!TODO
	// MAPPING of INS byte in command APDU
	final static byte VERIFY  = (byte) 0x20;
	final static byte CHARGE  = (byte) 0x30;
	final static byte PAY     = (byte) 0x40;
	final static byte BALANCE = (byte) 0x50;
	final static byte HISTORY = (byte) 0x60;

	/* MAPPING of returns to byte code for APDU response */
	// #!TODO

	// maximum number of incorrect tries before the PIN is blocked
	final static byte PIN_TRY_LIMIT = (byte) 0x03;

	// PIN size limitation
	final static byte PIN_SIZE_MAX = (byte) 0x08;
	final static byte PIN_SIZE_MIN = (byte) 0x04;

	/* VARIABLES */
	private OwnerPIN pin;
	private short balance; //#!TODO keep in mind that this is "only" a short
	//private LifeCyclePhaseMachine phaseMachine;
	//private StateMachine stateMachine;

	private PIN userPIN;

	/**
	 * Constructor
	 */
	public EWallet(byte[] buffer, short offset, byte length) {
		// #!TODO personalize: keys and PIN
		balance = 0; // init with no money
		//stateMachine = new StateMachine();
		register();
	}

	/**
	 * install the applet
	 */
	public static void install(byte[] bArray, short bOffset, byte bLength) {
		new EWallet(bArray, bOffset, bLength);
	}


    /**
     * called on every incoming APDU.
     * 
     * @param apdu the JCRE-owned APDU object.
     * @throws ISOException any processing error
     */
	public void process(APDU apdu) {
		byte[] apduBuffer = apdu.getBuffer();
		byte cla = apduBuffer[ISO7816.OFFSET_CLA];
		byte ins = apduBuffer[ISO7816.OFFSET_INS];
		short lc = (short)apduBuffer[ISO7816.OFFSET_LC];
		short p1 = (short)apduBuffer[ISO7816.OFFSET_P1];
		short p2 = (short)apduBuffer[ISO7816.OFFSET_P2];

		// #!TODO do some stuff with the input; currently just returns random
		
		/* Main SWITCH to call methods */
		switch (ins) {
		case BALANCE:
			balance(apdu);
			break;
		case PAY:
			pay(apdu);
			break;
		case CHARGE:
			charge(apdu);
			break;
		case HISTORY:
			history(apdu);
			break;
		default:
			ISOException.throwIt (ISO7816.SW_INS_NOT_SUPPORTED);
			break;
		}

		// #!TODO answer via secure channel
		apdu.setOutgoingAndSend((short)0, (short)0);
	}

	public boolean select(boolean b) {
		return true;
	}

	public void deselect(boolean b) {

	}

	/* METHODS for switch-case */
	
	private void balance(APDU apdu){
		
	}

	private void pay(APDU apdu){
		
	}

	private void charge(APDU apdu){
		
	}

	private void history(APDU apdu){
		
	}

	/* HELPER */
	
	/**
	 * helper to make sure that we ignore the sign-bit of shorts
	 */ 
	private short unsign(short s) {
		return (short) (s & (short) 0xFF);
	}
}
