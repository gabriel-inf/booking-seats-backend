package com.booking.booking.repository;

import com.booking.booking.model.Booking;

import org.springframework.data.repository.CrudRepository;

public interface BookingData extends CrudRepository<Booking, Integer>{


}