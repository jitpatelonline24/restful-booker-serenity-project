package com.restful.booker.bookinginfo;


import com.restful.booker.constants.EndPoints;
import com.restful.booker.model.AuthoPojo;
import com.restful.booker.model.BookingDates;
import com.restful.booker.model.BookingPojo;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

/**
 * Created by Jay
 */

public class BookingSteps {

    @Step("Generating authorisation token with Username : admin and Password : password123")
    public String test01createAuthorisationToken() {

        AuthoPojo authorizationPojo = new AuthoPojo();
        authorizationPojo.setUsername("admin");
        authorizationPojo.setPassword("password123");

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .when()
                .body(authorizationPojo)
                .post()
                .then().log().all().statusCode(200)
                .extract().path("token");
    }

    @Step("Creating booking with firstName : {0}, lastName : {1}, totalPrice : {2}, depositPaid : {3}, additionalNeeds : {4} and bookingDates : {5}")
    public ValidatableResponse test02createBooking(String firstname, String lastname, int totalprice,
                                                    boolean depositpaid, String additionalneeds, BookingDates bookingdates) {

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstName(firstname);
        bookingPojo.setLastName(lastname);
        bookingPojo.setTotalPrice(totalprice);
        bookingPojo.setDepositPaid(depositpaid);
        bookingPojo.setAdditionalNeeds(additionalneeds);
        bookingPojo.setBookingDates(bookingdates);

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer c3cf9df32fa20c9")
                .body(bookingPojo)
                .when()
                .post()
                .then().log().all();
    }
    @Step("Updating booking with firstName : {0}, lastName : {1}, totalPrice : {2}, depositPaid : {3}, additionalNeeds : {4}, bookingDates : {5} and token : {6}")
    public ValidatableResponse test03updateBookingDetails(String firstname, String lastname, int totalprice, boolean depositpaid,
                                                           String additionalneeds, BookingDates dates, String token, int bookingID) {

        BookingPojo bookingPojo = new BookingPojo();

        bookingPojo.setFirstName(firstname);
        bookingPojo.setLastName(lastname);
        bookingPojo.setTotalPrice(totalprice);
        bookingPojo.setDepositPaid(depositpaid);
        bookingPojo.setAdditionalNeeds(additionalneeds);
        bookingPojo.setBookingDates(dates);

        System.out.println("Token: " + token);


        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .pathParam("bookingID", bookingID)
                .body(bookingPojo)
                .when()
                .put(EndPoints.UPDATE_BOOKING_BY_ID)
                .then().log().all();
    }

    @Step("Partially Update booking with firstName : {0}, lastName : {1},  bookingDates : {2} and token : {3}")
    public ValidatableResponse test04partialUpdateBookingDetails(String firstname, String lastname, BookingDates bookingdates, String token, int bookingID) {

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstName(firstname);
        bookingPojo.setLastName(lastname);
        bookingPojo.setBookingDates(bookingdates);

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .pathParam("bookingID", bookingID)
                .body(bookingPojo)
                .when()
                .patch(EndPoints.UPDATE_BOOKING_BY_ID)
                .then().log().all();
    }

    @Step("Deleting the booking information with token : {0}")
    public ValidatableResponse test05deleteBooking(String token, int bookingID) {

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .pathParam("bookingID", bookingID)
                .when()
                .delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then().log().all();
    }

}


