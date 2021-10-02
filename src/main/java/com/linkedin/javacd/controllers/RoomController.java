package com.linkedin.javacd.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkedin.javacd.entities.Room;
import com.linkedin.javacd.services.RoomInventoryService;

@RestController
@RequestMapping("/room")
public class RoomController {
	
	private RoomInventoryService roomService;
	
	@Autowired
	public RoomController(RoomInventoryService roomService) {
		this.roomService = roomService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Room>> requestGuests(){
		return ResponseEntity.ok(this.roomService.list());
	}
	
	@GetMapping(params="ids")
	public ResponseEntity<List<Room>> requestGuests(@RequestParam List<Long> ids){
		return ResponseEntity.ok(this.roomService.list(ids));
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<Room> requestRoomById(@PathVariable Long roomId){
		
		Optional<Room> room = this.roomService.findById(roomId);
		
		if(room.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		
		return ResponseEntity.ok().body(room.get());
	}

}
