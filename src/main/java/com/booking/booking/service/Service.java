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
     * Oly one seat can be associated with a CPF if user has one seat piked but not
     * confirmed, he can pick another but the current will be unpicked
     * 
     * @param bk
     * @return
     * @throws Exception
     */
    @PutMapping("/pick")
    public Booking pickSeat(@RequestBody Booking bk) throws Exception {

        if (!(bk.getName().equals("") || bk.getCpf().equals("") || bk.getSeat().equals(""))) {

            Booking seatCpfAssociated = repBookingData.findByCpf(bk.getCpf());
            Booking seatLabelAssociated = repBookingData.findBySeat(bk.getSeat());

            if (seatLabelAssociated == seatCpfAssociated && seatCpfAssociated != null) {
                repBookingData.delete(seatCpfAssociated);
            } else if (seatCpfAssociated != null && seatLabelAssociated == null) {
                repBookingData.delete(seatCpfAssociated);
                repBookingData.save(bk);
            } else if (seatCpfAssociated == seatLabelAssociated && seatCpfAssociated == null) {
                repBookingData.save(bk);
            } else if (seatCpfAssociated == null && seatLabelAssociated != null) {
                throw new Exception("SEAT_UNAVAILABLE");
            } else if (seatCpfAssociated != null && seatLabelAssociated != null
                    && seatCpfAssociated != seatLabelAssociated) {
                throw new Exception("SEAT_UNAVAILABLE");
            } else {

            }
            return bk;

        } else
            throw new Exception("MISSING_INFO");
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
        } else
            throw new Exception("MISSING_INFO");
    }

    @PutMapping("/reserveMultiple")
    public void reserveMultiple(@RequestBody ArrayList<Booking> bk_list) throws Exception {
        for (Booking element : bk_list) {
            pickSeat(element);
        }
    }

    /**
     * This true if this cpf is locking a seat
     */
    @GetMapping("/checkCpfUsage")
    public Boolean checkCpfUsage(@RequestBody String cpf) throws Exception {
        Booking found = this.repBookingData.findByCpf(cpf);
        if(found != null) {
            return false;
        } else {
            return true;
        }
    }

}