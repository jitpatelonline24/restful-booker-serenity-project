package com.restful.booker.bookinginfo;


import com.restful.booker.constants.Path;
import com.restful.booker.model.BookingDates;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.RestAssured;

import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


/**
 * Created by Jay
 */

@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookingCRUDTest extends TestBase {

    static int bookingID;
    static String token;

    @Steps
    BookingSteps Steps;


    @Title("This will create an authorisation token")
    @Test
    public void test001() {

        RestAssured.basePath = Path.AUTHENTICATION;
        token = Steps.test01createAuthorisationToken();
        System.out.println("Token:  " + token);
    }

    @Title("This will create a new booking for user")
    @Test
    public void test002() {

        RestAssured.basePath = Path.BOOKING;

        BookingDates dates = new BookingDates();
        dates.setCheckin("2024-04-04");
        dates.setCheckout("2024-05-05");

        String firstName = TestUtils.getRandomValue() + "Prime";
        String lastName = TestUtils.getRandomValue() + "Testing";
        int totalPrice = 500;
        boolean depositPaid = true;
        String additionalNeeds = "Lunch";

        ValidatableResponse response = Steps.test02createBooking(firstName,lastName,totalPrice,depositPaid,additionalNeeds,dates);
        response.statusCode(200);

        bookingID = response.extract().path("bookingid");
    }

    @Title("Update the user information the verify the updated information")
    @Test
    public void test003() {

        RestAssured.basePath = Path.BOOKING;

        BookingDates dates = new BookingDates();
        dates.setCheckin("2024-04-04");
        dates.setCheckout("2024-05-05");

        String firstName = TestUtils.getRandomValue() + "Rest Assured";
        String lastName = TestUtils.getRandomValue() + "Testing";
        int totalPrice = 500;
        boolean depositPaid = true;
        String additionalNeeds = "Lunch";

        ValidatableResponse response = Steps.test03updateBookingDetails(firstName, lastName,totalPrice,depositPaid,additionalNeeds,dates, token, bookingID);
        response.statusCode(200);


    }

    @Title("Partially Update the booking information and verify the updated information")
    @Test
    public void test004() {

        String firstname = TestUtils.getRandomValue() + "PrimeTest";
        String lastname = TestUtils.getRandomValue() + "Testing";

        BookingDates dates = new BookingDates();
        dates.setCheckin("2024-03-04");
        dates.setCheckout("2024-04-04");

        ValidatableResponse response = Steps.test04partialUpdateBookingDetails(firstname, lastname, dates, token, bookingID);
        response.statusCode(200);
    }

    @Title("This will delete the booking")
    @Test
    public void test005() {

        ValidatableResponse response = Steps.test05deleteBooking(token, bookingID);
        response.statusCode(201);
    }


}
