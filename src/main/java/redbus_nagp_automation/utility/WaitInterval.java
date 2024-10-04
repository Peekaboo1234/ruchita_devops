package redbus_nagp_automation.utility;

/**
 * Represents Wait intervals used across tests.
 * 
 * @author ruchitapathak
 */
public enum WaitInterval {
	OneSecond(1), OneMinute(60), TwentySecond(20), TenSecond(10);

    private final int seconds;

    /**
     * Constructor for WaitInterval enum.
     * 
     * @param seconds the duration in seconds
     */
    WaitInterval(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Get the duration in seconds.
     * 
     * @return the duration in seconds
     */
    public int getSeconds() {
        return seconds;
    }
}
