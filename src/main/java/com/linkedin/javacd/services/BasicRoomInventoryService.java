package com.linkedin.javacd.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkedin.javacd.entities.Room;
import com.linkedin.javacd.repositories.RoomRepository;

@Service
public class BasicRoomInventoryService implements RoomInventoryService {

	private RoomRepository repository;

	private int discount = 0;

	@Autowired
	public BasicRoomInventoryService(RoomRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Room create(Room room) {

		Room result = this.repository.save(room);
		this.discount(result);

		return room;
	}

	@Override
	public Optional<Room> findById(Long roomId) {

		Optional<Room> result = this.repository.findById(roomId);
		this.discount(result);
		
		return result;
	}

	@Override
	public List<Room> findByType(String type) {

		List<Room> rooms = this.repository.findByType(type);
		this.discount(rooms);

		return rooms;
	}

	@Override
	public void applyDiscount(int percentage) {
		this.discount = percentage;
	}

	@Override
	public void delete(Long roomId) {
		this.repository.deleteById(roomId);
	}

	private void discount(Optional<Room> room) {

		if (room.isPresent()) {
			this.discount(room.get());
		}
	
	}
	
	private void discount(List<Room> rooms){
		rooms.forEach(this::discount);
	}
	
	private void discount(Room room) {
		if (this.discount != 0) {
			room.setRate(room.getRate().multiply(new BigDecimal(((100.0 - Double.valueOf(discount)) / 100.0))).setScale(2, RoundingMode.FLOOR));
		}
	}

	@Override
	public List<Room> list() {
		return this.repository.findAll(); 
	}

	@Override
	public List<Room> list(List<Long> ids) {
		return this.repository.findAllById(ids); 
	}
}
