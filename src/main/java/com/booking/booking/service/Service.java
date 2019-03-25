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
    public Booking pickSeat(@RequestBody Booking bk) throws Exception {
        if (!(bk.getName().equals("") || bk.getCpf().equals("") || bk.getSeat().equals(""))) {

            Booking existent = repBookingData.findBySeat(bk.getSeat());
            if (existent == null) {
                bk.setConfirmed(false);
                repBookingData.save(bk);
                return bk;
            } else {
                repBookingData.delete(existent);
                return null;
            }
        }
        return null;
    }

    /**
     * This method confirms a reservation
     */
    @PutMapping("/confirm")
    public Booking confirmReservation(@RequestBody Booking bk) throws Exception {
        if (!(bk.getName().equals("") || bk.getCpf().equals("") || bk.getSeat().equals(""))) {

            Booking existent = repBookingData.findByCpf(bk.getCpf());
            if (existent == null) {
                return null;
            } else {
                existent.setConfirmed(true);
                repBookingData.save(existent);
                return existent;
            }
        }
        return null;
    }

}