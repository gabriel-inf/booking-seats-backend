package com.booking.booking.service;

import java.util.ArrayList;
import java.util.List;

import com.booking.booking.model.Booking;
import com.booking.booking.repository.BookingData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/movie")
public class Service {

    @Autowired
    BookingData repBookingData;

    @GetMapping("/movieState")
    public Iterable<Booking> getMovieState() {
        return repBookingData.findAll();
    }

    @GetMapping("/lockedSeats")
    public List<String> getLockedSeats() {
        List<String> lockedSeats = new ArrayList<>();
        Iterable<Booking> actualBookings = repBookingData.findAll();
        for (Booking bk : actualBookings) {
            lockedSeats.add(bk.getSeat());
        }
        return lockedSeats;
    }

    /**
     * This function picks and unpick and seat
     */
    @PutMapping("/pick")
    public String pickSeat(@RequestBody Booking bk) throws Exception {
        if (!(bk.getName().equals("") || bk.getCpf().equals("") || bk.getSeat().equals(""))) {

            Booking existent = repBookingData.findBySeat(bk.getSeat());
            if (existent == null) {
                bk.setConfirmed(false);
                repBookingData.save(bk);
                return "Picked seat " + bk.getSeat();
            } else {
                repBookingData.delete(existent);
                return "Unpicked seat " + bk.getSeat();
            }
        }
        return "Error booking is missing data";
    }

    /**
     * This method confirms a reservation
     */
    @PutMapping("/confirm")
    public String confirmReservation(@RequestBody Booking bk) throws Exception {
        if (!(bk.getName().equals("") || bk.getCpf().equals("") || bk.getSeat().equals(""))) {

            Booking existent = repBookingData.findBySeat(bk.getSeat());
            if (existent == null) {
                return "Some error occurred, couldn't make the reservation " + bk.toString();
            } else {
                existent.setConfirmed(true);
                repBookingData.save(existent);
                return "Confirmed seat " + existent.getSeat();
            }
        }
        return "Error booking is missing data";
    }

}