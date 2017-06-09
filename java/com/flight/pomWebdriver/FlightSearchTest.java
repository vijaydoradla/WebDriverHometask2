package com.flight.pomWebdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FlightSearchTest {

	private static final String applicationURL = "https://www.easyjet.com/en/";

	WebDriver driver;
	private static final String homepage = "Book direct for our guaranteed cheapest prices | easyJet.com";
	private static final String departureCity = "Stockholm";
	private static final String expDepartureName = "Arlanda";

	private static final int adultsDisplayedTickets = 1;
	private static final String searchResultsPage = "Book cheap flights and find last minute flight deals – easyJet.com";

	private static final String currencyType = "kr";

	@Test
	public void flightTicketsBooking() throws Exception {

		FlightTicketsBookingPage ticketsBookingPage = PageFactory.initElements(driver, FlightTicketsBookingPage.class);
		// Closing Cookie policy Pop up
		ticketsBookingPage.cookiePolicyPopup();
		// Verifying Home Page opened
		Assert.assertEquals(ticketsBookingPage.getPageTitle(), homepage, "Flight Tickets booking page not found");

		// Under "Flights" tab, in "From" field type city "Stockholm"
		ticketsBookingPage.orginCity(departureCity);

		// Verify the value of "From" field contains airport name "Arlanda"
		Assert.assertTrue(ticketsBookingPage.getDepatureCity().contains(expDepartureName));
		// In "To" field choose city "London" using custom selector (button at
		// the right side from input field)
		ticketsBookingPage.destinationAirport();

		// Choose any departure dates
		ticketsBookingPage.depatureDate();
		// Choose any return dates
		ticketsBookingPage.returnDate();
		// Verify 1 adult passenger is added automatically
		Assert.assertEquals(ticketsBookingPage.defaultSelectedAdultsTickets(), adultsDisplayedTickets,
				"Default Adults tickets not match");
		// Add 1 one more passenger of type "Children"
		ticketsBookingPage.addChildPassenger();
		// Click "Show flights" button
		ticketsBookingPage.showFlights();

	}

	@Test(dependsOnMethods = { "flightTicketsBooking" })
	public void verifyingSearchResults() throws Exception {

		FlightSearchResultsPage flightSearchResultsPage = PageFactory.initElements(driver,
				FlightSearchResultsPage.class);

		// Verify that search results page is opened
		Assert.assertEquals(flightSearchResultsPage.getPageTitle(), searchResultsPage, "Search Results page not found");

		// Verify that search results contain prices for out bound flights
		Assert.assertTrue(flightSearchResultsPage.getOutboundFlightsDetails().contains(currencyType),
				"Outbound Flights details not contain prices details");
		// Verify that search results contain prices for return flights
		Assert.assertTrue(flightSearchResultsPage.getReturnFlightsDetails().contains(currencyType),
				"Return Flights details not contain prices details");

	}

	@BeforeClass
	public void launchBrowser() {

		System.setProperty("webdriver.chrome.driver", "Resources\\chromedriver.exe");
		driver = new ChromeDriver();
		// Open main application page https://www.easyjet.com/en/
		driver.get(applicationURL);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

	}

	@AfterClass
	public void closeBrowser() {
		driver.close();
	}

}//
