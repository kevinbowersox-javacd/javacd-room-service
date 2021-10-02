package com.linkedin.javacd.services;

import java.util.List;
import java.util.Optional;

import com.linkedin.javacd.entities.Room;

public interface RoomInventoryService {
	
	public Room create(Room room);

	public List<Room> list();
	
	public List<Room> list(List<Long> ids);
	
	public Optional<Room> findById(Long roomId);

	public List<Room> findByType(String type);

	public void applyDiscount(int percentage);
	
	public void delete(Long roomId);

}
