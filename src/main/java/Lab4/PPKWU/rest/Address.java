package Lab4.PPKWU.rest;

public class Address {
	private String streetAddress;
	private String addressLocality;
	private String postalCode;

	@Override
	public String toString() {
		return streetAddress + " " +
			addressLocality + " " +
			postalCode + " ";
	}
}
