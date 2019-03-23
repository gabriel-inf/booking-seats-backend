package com.booking.booking.service;

import java.util.ArrayList;
import java.util.List;

import com.booking.booking.model.Booking;
import com.booking.booking.repository.BookingData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class Service {

    @Autowired
    BookingData repBookingData;
    

    @GetMapping("/lockedSeats")
    public List<String> getLockedSeats() {

        List<String> lockedSeats = new ArrayList<>();

        Iterable<Booking> actualBookings = repBookingData.findAll();
        for (Booking bk : actualBookings) {
            lockedSeats.add(bk.getSeat());
        }
        return lockedSeats;
    }

    @PutMapping("/book")
    public void putBooking(@RequestBody Booking bk) {

        if (!(bk.getName().equals("") 
                || bk.getCpf().equals("")
                || bk.getSeat().equals(""))) {

            repBookingData.save(bk);

        }

    }



}