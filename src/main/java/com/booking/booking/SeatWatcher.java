package com.booking.booking;

import java.util.HashMap;
import java.util.Map;

import com.booking.booking.model.Booking;
import com.booking.booking.repository.BookingData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SeatWatcher {

    @Autowired
    BookingData repBookingData;
    HashMap<String, Integer> timeoutSchedule;

    SeatWatcher() {
        timeoutSchedule = new HashMap();
    }

    @Scheduled(fixedRate = 5000)
    public void whatchSeatsNotConfirmed() {

        Iterable<Booking> booking = repBookingData.findAll();
        if (booking != null) {
            for (Booking bk : booking) {
                if (bk.getConfirmed() && timeoutSchedule.get(bk.getSeat()) != null) {
                    timeoutSchedule.remove(bk.getSeat());
                } else if (!bk.getConfirmed()) {
                    if (timeoutSchedule.get(bk.getSeat()) == null) {
                        timeoutSchedule.put(bk.getSeat(), 0);
                        System.out.println("Added seat " + bk.getSeat() + "to Watcher");
                    } else if (timeoutSchedule.get(bk.getSeat()) > 120) {
                        repBookingData.delete(bk);
                        timeoutSchedule.remove(bk.getSeat());
                        System.out.println("Remove seat " + bk.getSeat() + " from reserve and the Watcher");
                    } else if (!bk.getConfirmed()) {
                        timeoutSchedule.put(bk.getSeat(), timeoutSchedule.get(bk.getSeat()) + 1);
                        System.out.println("5 sec for seat " + bk.getSeat() + " it is being watched");
                    }
                }
            }
        }

    }

}